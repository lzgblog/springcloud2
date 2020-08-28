package com.springcloud.microservice.microserviceconsumer.service;

import com.microserviceapi.entities.Dept;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * feign负载均衡--接口开发
 * @author luzg
 * @date 2020-08-26 19:50
 *
 * MICROSERVICE-PROVIDER  微服务应用名
 * ClientFallBackFactory   hystrix降级的具体实现类
 */
@FeignClient(value = "MICROSERVICE-PROVIDER",fallbackFactory = ClientFallBackFactory.class)
//@FeignClient(value = "MICROSERVICE-PROVIDER")
public interface DeptService {
    @RequestMapping("/list")
    public List<Dept> list() ;

    @RequestMapping("/find/{id}")
    public Dept find(@PathVariable("id") Long id);

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public boolean add(Dept dept);
}
