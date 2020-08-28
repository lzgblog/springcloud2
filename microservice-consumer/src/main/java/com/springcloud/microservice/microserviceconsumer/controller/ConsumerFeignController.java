package com.springcloud.microservice.microserviceconsumer.controller;

import com.microserviceapi.entities.Dept;
import com.netflix.discovery.converters.Auto;
import com.springcloud.microservice.microserviceconsumer.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * feign负载均衡
 * @author luzg
 * @date 2020-08-27 17:40
 */
@RestController
public class ConsumerFeignController {
    @Autowired
    private DeptService deptService;

    @RequestMapping("/feign/list")
    public List<Dept> list() {
        return deptService.list();
    }

    @RequestMapping("/feign/add")
    public boolean add(Dept dept) {
        return deptService.add(dept);
    }

    @RequestMapping("/feign/find/{id}")
    public Dept find(@PathVariable("id") Long id) {
        return deptService.find(id);
    }
}
