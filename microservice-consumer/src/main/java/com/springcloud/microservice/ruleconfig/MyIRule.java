package com.springcloud.microservice.ruleconfig;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自定义 负载均衡 模式
 * @author luzg
 * @date 2020-08-27 17:32
 */
@Configuration
public class MyIRule {

    @Bean
    public IRule getIRule(){
        return new RandomRule();//达到的目的，用我们重新选择的随机算法替代默认的轮询。
    }
}
