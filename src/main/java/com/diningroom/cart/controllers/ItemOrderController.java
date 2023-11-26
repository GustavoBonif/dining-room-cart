package com.diningroom.cart.controllers;

import com.diningroom.cart.dto.ItemOrderDTO;
import com.diningroom.cart.services.ItemOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/itemsOrder")
public class ItemOrderController {
    private ItemOrderService service;

    @Autowired
    public ItemOrderController(ItemOrderService service) {
        this.service = service;
    }

    @GetMapping("/listAll")
    public List<ItemOrderDTO> listAll() {
        return service.listAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        ResponseEntity<ItemOrderDTO> response = service.findById(id);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response;
        } else {
            return ResponseEntity.status(response.getStatusCode()).body("Registro não encontrado");
        }
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody ItemOrderDTO itemOrderDTO) {
        return service.mountNewItemOrder(itemOrderDTO);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody ItemOrderDTO itemOrderDTO) {
        return service.update(id, itemOrderDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        return service.delete(id);
    }
}
