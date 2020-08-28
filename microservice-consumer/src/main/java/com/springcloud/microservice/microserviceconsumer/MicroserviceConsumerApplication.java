package com.springcloud.microservice.microserviceconsumer;

import com.springcloud.microservice.ruleconfig.MyIRule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
//@RibbonClient(value = "MICROSERVICE-PROVIDER",configuration = {MyIRule.class})//自定义负载均衡
//@EnableFeignClients(basePackages= {"com.microserviceapi.service"})//使用Feign负载均衡
@EnableFeignClients(basePackages= {"com.springcloud.microservice.microserviceconsumer.service"})//使用Feign负载均衡
public class MicroserviceConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroserviceConsumerApplication.class, args);
    }

}
