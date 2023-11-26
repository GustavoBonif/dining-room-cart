package com.diningroom.cart.repository;

import com.diningroom.cart.entities.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
    List<Orders> findByClientIdAndPaid(Long clientId, boolean isPaid);
}
