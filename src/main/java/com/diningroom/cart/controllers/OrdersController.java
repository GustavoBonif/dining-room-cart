package com.diningroom.cart.controllers;

import com.diningroom.cart.dto.OrdersDTO;
import com.diningroom.cart.enums.PaymentMethod;
import com.diningroom.cart.services.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrdersController {

    private OrdersService service;

    @Autowired
    public OrdersController(OrdersService service) {
        this.service = service;
    }

    @GetMapping("/listAll")
    public List<OrdersDTO> listAll() {
        return service.listAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        ResponseEntity<OrdersDTO> response = service.findById(id);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response;
        } else {
            return ResponseEntity.status(response.getStatusCode()).body("Registro não encontrado");
        }
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody OrdersDTO ordersDTO) {
        Long newOrderId = service.create(ordersDTO);

        return new ResponseEntity<>("Sucesso ao criar o pedido de ID: " + newOrderId, HttpStatus.CREATED);
    }

    @PutMapping("/pay/{orderId}")
    public ResponseEntity<String> payOrder(@PathVariable Long orderId, @RequestBody Map<String, String> requestBody) {
        String paymentMethodStr = requestBody.get("paymentMethod");

        PaymentMethod paymentMethod = PaymentMethod.valueOf(paymentMethodStr);
        return service.payOrder(orderId, paymentMethod);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        return service.delete(id);
    }

    @GetMapping(value = "/listUnpaidOrders/{clientId}")
    public List<OrdersDTO> listUnpaidOrders(@PathVariable Long clientId) {
        return service.listUnpaidOrders(clientId);
    }

    @GetMapping(value = "/listPaidOrders/{clientId}")
    public List<OrdersDTO> listPaidOrders(@PathVariable Long clientId) {
        return service.listPaidOrders(clientId);
    }
}
