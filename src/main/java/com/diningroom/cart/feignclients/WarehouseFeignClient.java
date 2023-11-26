package com.diningroom.cart.feignclients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "dining-room-warehouse", path = "/stocks", configuration = FeignConfiguration.class)
public interface WarehouseFeignClient {
    @RequestMapping(value = "/updateStockByProduct", method = RequestMethod.PUT)
    ResponseEntity<String> updateStockByProduct(
            @RequestParam("productId") Long productId,
            @RequestParam("quantityOrdered") int quantityOrdered);
}

