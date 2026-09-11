package org.example.springboot.repository;

import org.example.springboot.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("""
            SELECT DISTINCT o
              FROM Order o
              LEFT JOIN FETCH o.items i
              LEFT JOIN FETCH i.product
              WHERE o.id = :id
            """)
    Optional<Order> findByIdFetchItems(@Param("id") Long id);
}
