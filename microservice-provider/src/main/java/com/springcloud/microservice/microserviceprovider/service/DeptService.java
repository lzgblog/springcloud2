package com.springcloud.microservice.microserviceprovider.service;

import com.microserviceapi.entities.Dept;

import java.util.List;
import java.util.Map;

/**
 * @author luzg
 * @date 2020-08-26 19:50
 */
public interface DeptService {
    public List<Dept> list();

    public Dept find(Long deptNo);

    public boolean add(String dept);
}
