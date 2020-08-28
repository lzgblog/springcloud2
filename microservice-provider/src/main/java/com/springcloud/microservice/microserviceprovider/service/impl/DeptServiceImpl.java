package com.springcloud.microservice.microserviceprovider.service.impl;

import com.microserviceapi.entities.Dept;
import com.springcloud.microservice.microserviceprovider.mapper.DeptMapper;
import com.springcloud.microservice.microserviceprovider.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author luzg
 * @date 2020-08-26 19:52
 */
@Service
public class DeptServiceImpl implements DeptService {

    @Autowired
    private DeptMapper deptMapper;

    @Override
    public List<Dept> list() {
        List<Dept> list = deptMapper.findAll();
        return list;
    }

    @Override
    public Dept find(Long deptNo) {
        Dept one = deptMapper.findById(deptNo);
        return one;
    }

    @Override
    public boolean add(String dept) {
        boolean b = deptMapper.addDept(dept);
        return b;
    }
}
