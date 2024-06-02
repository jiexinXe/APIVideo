package com.apivideo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
@MapperScan("com.apivideo.mapper")
public class ApiVideoApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiVideoApplication.class, args);
    }
}
