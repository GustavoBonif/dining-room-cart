package com.diningroom.cart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class DiningRoomCartApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiningRoomCartApplication.class, args);
    }

}
