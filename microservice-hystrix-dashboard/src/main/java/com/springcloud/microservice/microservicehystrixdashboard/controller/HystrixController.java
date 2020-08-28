package com.springcloud.microservice.microservicehystrixdashboard.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author luzg
 * @date 2020-08-28 17:06
 */
@RestController
public class HystrixController {

    @RequestMapping("/hello")
    @HystrixCommand(fallbackMethod = "demo")
    public String getDemo(){
        return "hello world";
    }

    public String demo(){
        return "hystrixCommand";
    }
}
