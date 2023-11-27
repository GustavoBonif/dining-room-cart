package com.diningroom.cart.feignclients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "dining-room-user-registry", path = "/clients")
public interface ClientFeignClient {
    @GetMapping(value = "/exists/{id}")
    ResponseEntity<Boolean> exists(@PathVariable Long id);
}
