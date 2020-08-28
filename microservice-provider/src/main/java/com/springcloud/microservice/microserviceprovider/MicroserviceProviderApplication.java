package com.springcloud.microservice.microserviceprovider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

@SpringBootApplication
@EnableEurekaClient//开启eureka客户端
@EnableHystrix//开启断路器注解
@EnableCircuitBreaker//开启断路器功能
public class MicroserviceProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroserviceProviderApplication.class, args);
    }

}
