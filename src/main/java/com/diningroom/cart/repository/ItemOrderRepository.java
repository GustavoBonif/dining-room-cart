package com.diningroom.cart.repository;

import com.diningroom.cart.entities.ItemOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemOrderRepository extends JpaRepository<ItemOrder, Long> {
    List<ItemOrder> findByOrdersId(Long orderId);
}
