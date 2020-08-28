package com.springcloud.microservice.microserviceprovider.controller;

import com.microserviceapi.entities.Dept;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.springcloud.microservice.microserviceprovider.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author luzg
 * @date 2020-08-26 19:55
 */
@RestController
public class DeptController {

    @Autowired
    private DeptService deptService;

    @RequestMapping("/list")
    public List<Dept> list() {
        return deptService.list();
    }

    @RequestMapping("/find/{id}")
    @HystrixCommand(fallbackMethod = "fallBack_find")//熔断  发生异常时执行fallBack_find方法
    public Dept find(@PathVariable("id") Long id) {
        Dept dept = deptService.find(id);
        if(dept == null){
            throw new RuntimeException("查询异常！");
        }
        return dept;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public boolean add(@RequestBody Dept dept) {
        System.out.println("=====" + dept);
        return true;
    }

    public Dept fallBack_find(@PathVariable("id") Long id){
        Dept dept = new Dept();
        dept.setDeptNo(id);
        dept.setDeptName("程序异常或中断！！");
        dept.setDbSource("未查到任务数据！");
        return dept;
    }

}
