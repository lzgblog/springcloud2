package com.springcloud.microservice.microserviceprovider.mapper;

import com.microserviceapi.entities.Dept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author luzg
 * @date 2020-08-26 19:42
 */
@Repository
@Mapper
public interface DeptMapper {
    //@Select("select * from dept where dept_no=#{deptNo}")
    public Dept findById(@Param("deptNo") Long deptNo);

    //@Select("select * from dept")
    public List<Dept> findAll();

    //@Insert("INSERT INTO dept(dept_name,db_source) VALUES(#{dept},DATABASE())")
    public boolean addDept(@Param("dept") String dept);

}
