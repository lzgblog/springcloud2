package com.springcloud.microservice.microserviceconsumer.service;

import com.microserviceapi.entities.Dept;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author luzg
 * @date 2020-08-28 14:15
 *
 * 实现FallbackFactory接口  泛型为DeptService
 */
@Component  //**必须要将该类加入到容器中管理
public class ClientFallBackFactory implements FallbackFactory {
    public DeptService create(Throwable throwable) {
        /**
         * 当服务端停掉服务后 请求不到provider服务端 则会执行这里
         */
        return new DeptService() {
            public List<Dept> list() {
                ArrayList<Dept> list = new ArrayList<Dept>();
                Dept dept = new Dept();
                dept.setDeptNo(11l);
                dept.setDeptName("服务器宕机！暂停服务！");
                list.add(dept);
                return list;
            }

            public Dept find(Long id) {//需要处理的方法
                Dept dept = new Dept();
                dept.setDeptNo(id);
                dept.setDeptName("服务器宕机！暂停服务！");
                return dept;
            }

            public boolean add(Dept dept) {
                return false;
            }
        };
    }
}
