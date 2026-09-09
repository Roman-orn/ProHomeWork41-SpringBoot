package org.example.springboot.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(length = 100, nullable = false, unique = true)
    public String name;

    @Column(nullable = false)
    public int quantity;

    @Column(nullable = false, precision = 10, scale = 2)
    public BigDecimal price;

    public Product() {

    }

    public Product(String name, int quantity, BigDecimal price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }
}
