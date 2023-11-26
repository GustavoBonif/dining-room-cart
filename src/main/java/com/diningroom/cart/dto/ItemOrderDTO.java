package com.diningroom.cart.dto;


import com.diningroom.cart.entities.ItemOrder;

import java.math.BigDecimal;

public class ItemOrderDTO {

    private Long id;
    private Long productId;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal subTotalPrice;
    private Long ordersId;
    private Long clientId;

    public ItemOrderDTO() {

    }

    public ItemOrderDTO(Long id, Long productId, int quantity, Long clientId) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
        this.clientId = clientId;
    }

    public ItemOrderDTO(ItemOrder itemOrders) {
        this.id = itemOrders.getId();
        this.productId = itemOrders.getProductId();
        this.quantity = itemOrders.getQuantity();
        this.unitPrice = itemOrders.getUnitPrice();
        this.subTotalPrice = itemOrders.getSubTotalPrice();
        this.clientId = itemOrders.getClientId();
        this.ordersId = itemOrders.getOrders().getId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getSubTotalPrice() {
        return subTotalPrice;
    }

    public void setSubTotalPrice(BigDecimal subTotalPrice) {
        this.subTotalPrice = subTotalPrice;
    }

    public Long getOrdersId() {
        return ordersId;
    }

    public void setOrdersId(Long order_id) {
        this.ordersId = order_id;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }
}
