package com.car.onlinecarselectionsystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.car.onlinecarselectionsystem.mapper")
public class OnlineCarSelectionSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineCarSelectionSystemApplication.class, args);
    }

}
