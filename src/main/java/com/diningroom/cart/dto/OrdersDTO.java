package com.diningroom.cart.dto;

import com.diningroom.cart.entities.Orders;
import com.diningroom.cart.enums.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrdersDTO {
    private Long id;
    private boolean paid;
    private PaymentMethod paymentMethod;
    private LocalDateTime dateTime;
    private BigDecimal totalPrice;
    private List<ItemOrderDTO> itemsOrder;
    private Long clientId;

    public OrdersDTO() {

    }

    public OrdersDTO(Long id, boolean paid, PaymentMethod paymentMethod, LocalDateTime dateTime, BigDecimal totalPrice, List<ItemOrderDTO> itemsOrder, Long clientId) {
        this.id = id;
        this.paid = paid;
        this.paymentMethod = paymentMethod;
        this.dateTime = dateTime;
        this.totalPrice = totalPrice;
        this.itemsOrder = itemsOrder;
        this.clientId = clientId;
    }


    public OrdersDTO (Orders orders) {
        this.id = orders.getId();
        this.paid = orders.isPaid();
        this.paymentMethod = orders.getPaymentMethod();
        this.dateTime = orders.getDateTime();
        this.totalPrice = orders.getTotalPrice();
        this.clientId = orders.getClientId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<ItemOrderDTO> getItemsOrder() {
        return itemsOrder;
    }

    public void setItemsOrder(List<ItemOrderDTO> itemsOrder) {
        this.itemsOrder = itemsOrder;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }
}
