package com.example.chapter02.mapper;

import com.example.chapter02.entity.Dept;
import com.example.chapter02.entity.Emp;

import java.util.List;

/** 员工与部门的多表映射接口。 */
public interface EmpDeptMapper {

    List<Emp> findAllEmpWithDept();

    Emp findEmpWithDeptById(Integer empno);

    List<Dept> findAllDeptWithEmps();

    Dept findDeptWithEmpsById(Integer deptno);
}
