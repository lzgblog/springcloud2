package com.springcloud.microservice.microserviceconsumer.controller;

import com.microserviceapi.entities.Dept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author luzg
 * @date 2020-08-26 20:24
 */
@RestController
public class ConsumerController {
    //private static final String REST_PREFIX = "http://localhost:8001";
    private static final String REST_PREFIX = "http://MICROSERVICE-PROVIDER";//使用ribbon后 通过eureka注册中心查找发布的微服务

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/consumer/list")
    public List<Dept> list() {
        return restTemplate.getForObject(REST_PREFIX + "/list", List.class);
    }

    @RequestMapping("/consumer/add")
    public boolean add(Dept dept) {
        System.out.println("------" + dept);
        return restTemplate.postForObject(REST_PREFIX + "/add", dept, boolean.class);
    }

    @RequestMapping("/consumer/find/{id}")
    public Dept find(@PathVariable("id") Long id) {
        return restTemplate.getForObject(REST_PREFIX + "/find/" + id, Dept.class);
    }
}
