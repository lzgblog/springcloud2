package com.springcloud.microservice.microserviceconsumer.config;

import com.netflix.hystrix.config.HystrixCommandConfiguration;
import com.netflix.hystrix.contrib.javanica.aop.aspectj.HystrixCommandAspect;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author luzg
 * @date 2020-08-26 20:25
 */
@Configuration
public class ConfigBean {

    @Bean
    @LoadBalanced//Spring Cloud Ribbon是基于Netflix Ribbon实现的一套客户端       负载均衡的工具。
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
    //定义负载均衡模式
    @Bean
    public IRule getIRule(){
        //return new RoundRobinRule();
        return new RandomRule();//达到的目的，用我们重新选择的随机算法替代默认的轮询。
        //return new RetryRule();
    }
}
