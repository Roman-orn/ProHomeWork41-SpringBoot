package org.example.springboot.service;

import org.example.springboot.dto.OrderItemResponseDto;
import org.example.springboot.dto.OrderRequestDto;
import org.example.springboot.dto.OrderResponseDto;
import org.example.springboot.dto.ProductOrderRequestDto;
import org.example.springboot.entity.Order;
import org.example.springboot.entity.OrderItem;
import org.example.springboot.entity.Product;
import org.example.springboot.exception.BadRequestException;
import org.example.springboot.exception.DataNotFoundException;
import org.example.springboot.exception.ErrorCode;
import org.example.springboot.repository.OrderRepository;
import org.example.springboot.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }


    public OrderResponseDto save(OrderRequestDto request) {
        if (request.products() == null || request.products().isEmpty()) {
            throw new BadRequestException(ErrorCode.INPUT_PARAMETERS_VALIDATION_FAILED, "Parameter [products] must not be null!");
        }

        Order order = new Order();
        order.creationDate = LocalDateTime.now();

        orderHandler(order, request);

        orderRepository.save(order);

        return toResponse(order);
    }


    public OrderResponseDto findById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(
                        ErrorCode.ORDER_NOT_FOUND,
                        "Order with id %d not found.".formatted(id)));

        return toResponse(order);
    }


    public OrderResponseDto update(Long id, OrderRequestDto request) {
        if (request.products() == null || request.products().isEmpty()) {
            throw new BadRequestException(ErrorCode.INPUT_PARAMETERS_VALIDATION_FAILED, "Parameter [products] must not be null!");
        }

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(
                        ErrorCode.ORDER_NOT_FOUND,
                        "Order with id %d not found.".formatted(id)));

        for (OrderItem oldItem : order.items) {
            Product product = oldItem.product;
            product.quantity += oldItem.quantity;
        }

        order.items.clear();

        orderHandler(order, request);

        orderRepository.save(order);

        return toResponse(order);
    }


    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }


    public List<OrderResponseDto> findAll() {
        return orderRepository.findAll().stream()
                .map(this::toResponse).toList();
    }


    private void orderHandler(Order order, OrderRequestDto request) {
        BigDecimal totalCost = BigDecimal.ZERO;

        for (ProductOrderRequestDto requestItem : request.products()) {
            Product product = productRepository.findById(requestItem.productId())
                    .orElseThrow(() ->
                            new DataNotFoundException(
                                    ErrorCode.ORDER_NOT_FOUND,
                                    "Product with id %d not found.".formatted(requestItem.productId()))
                    );

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


    private OrderResponseDto toResponse(Order order) {
        List<OrderItemResponseDto> itemResponses = order.items.stream()
                .map(item -> new OrderItemResponseDto(
                        item.product.id,
                        item.product.name,
                        item.quantity,
                        item.product.price
                ))
                .toList();

        return new OrderResponseDto(
                order.id,
                order.creationDate,
                itemResponses,
                order.totalCost
        );
    }
}
