package org.example.springboot.controller;

import org.example.springboot.dto.OrderRequestDto;
import org.example.springboot.dto.OrderResponseDto;
import org.example.springboot.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody OrderRequestDto request) {
        OrderResponseDto orderResponseDto = orderService.save(request);
        return ResponseEntity.ok().body(orderResponseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> findById(@PathVariable("id") Long id) {
        OrderResponseDto orderResponseDto = orderService.findById(id);
        return ResponseEntity.ok().body(orderResponseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponseDto> update(@PathVariable("id") Long id, @RequestBody OrderRequestDto requestDto) {
        OrderResponseDto orderResponseDto = orderService.update(id, requestDto);
        return ResponseEntity.ok().body(orderResponseDto);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") Long id){
        orderService.deleteById(id);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> findAll(){
        List<OrderResponseDto> orderResponseDtoList = orderService.findAll();
        return ResponseEntity.ok().body(orderResponseDtoList);
    }
}
