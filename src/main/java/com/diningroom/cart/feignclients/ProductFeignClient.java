package com.diningroom.cart.feignclients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;

@FeignClient(name = "dining-room-catalog", path = "/products")
public interface ProductFeignClient {

    @GetMapping(value = "/exists/{id}")
    ResponseEntity<Boolean> exists(@PathVariable Long id);

    @GetMapping(value = "/getPrice/{id}")
    ResponseEntity<BigDecimal> getPrice(@PathVariable Long id);
}
