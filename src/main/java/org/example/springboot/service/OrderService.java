package org.example.springboot.service;

import org.example.springboot.dto.OrderRequestDto;
import org.example.springboot.dto.OrderResponseDto;
import org.example.springboot.dto.ProductOrderRequestDto;
import org.example.springboot.entity.Order;
import org.example.springboot.entity.OrderItem;
import org.example.springboot.entity.Product;
import org.example.springboot.exception.BadRequestException;
import org.example.springboot.exception.DataNotFoundException;
import org.example.springboot.exception.ErrorCode;
import org.example.springboot.mapper.OrderDtoMapper;
import org.example.springboot.repository.OrderRepository;
import org.example.springboot.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderDtoMapper orderDtoMapper;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository, OrderDtoMapper orderDtoMapper) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.orderDtoMapper = orderDtoMapper;
    }


    public OrderResponseDto save(OrderRequestDto request) {

        Order order = new Order();
        order.creationDate = LocalDateTime.now();

        processOrder(order, request);

        orderRepository.save(order);

        return orderDtoMapper.toResponse(order);
    }


    @Transactional(readOnly = true)
    public OrderResponseDto findById(Long id) {
        Order order = orderRepository.findByIdFetchItems(id)
                .orElseThrow(() -> new DataNotFoundException(
                        ErrorCode.ORDER_NOT_FOUND,
                        "Order with id %d not found.".formatted(id)));

        return orderDtoMapper.toResponse(order);
    }


    public OrderResponseDto update(Long id, OrderRequestDto request) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(
                        ErrorCode.ORDER_NOT_FOUND,
                        "Order with id %d not found.".formatted(id)));

        for (OrderItem oldItem : order.items) {
            Product product = oldItem.product;
            product.quantity += oldItem.quantity;
        }

        order.items.clear();

        processOrder(order, request);

        return orderDtoMapper.toResponse(order);
    }


    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }


    public List<OrderResponseDto> findAll() {
        return orderRepository.findAll().stream()
                .map(orderDtoMapper::toResponse).toList();
    }


    private void processOrder(Order order, OrderRequestDto request) {
        List<Long> productIds = request.products().stream()
                .map(ProductOrderRequestDto::productId)
                .toList();

        List<Product> products = productRepository.findAllById(productIds);

        Map<Long, Product> productsById = products.stream()
                .collect(Collectors.toMap(
                        product -> product.id,
                        product -> product
                ));

        BigDecimal totalCost = BigDecimal.ZERO;

        for (ProductOrderRequestDto requestItem : request.products()) {
            Product product = productsById.get(requestItem.productId());

            if (product == null) {
                throw new DataNotFoundException(
                        ErrorCode.PRODUCT_NOT_FOUND,
                        "Product with id %d not found.".formatted(requestItem.productId()));
            }

            if (requestItem.quantity() > product.quantity) {
                throw new BadRequestException(
                        ErrorCode.INPUT_PARAMETERS_VALIDATION_FAILED,
                        "Product [%s] — %d in stock.".formatted(product.name, product.quantity));
            }

            product.quantity -= requestItem.quantity();

            OrderItem orderItem = new OrderItem();
            orderItem.order = order;
            orderItem.product = product;
            orderItem.quantity = requestItem.quantity();

            order.items.add(orderItem);

            totalCost = totalCost.add(
                    product.price.multiply(
                            BigDecimal.valueOf(requestItem.quantity())));
        }

        order.totalCost = totalCost;
    }
}
