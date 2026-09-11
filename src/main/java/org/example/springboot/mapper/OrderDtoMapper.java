package org.example.springboot.mapper;

import org.example.springboot.dto.OrderItemResponseDto;
import org.example.springboot.dto.OrderResponseDto;
import org.example.springboot.entity.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderDtoMapper {

    public OrderResponseDto toResponse(Order order) {

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
