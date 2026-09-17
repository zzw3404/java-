package com.example.chapter02;

import com.example.chapter02.entity.Dept;
import com.example.chapter02.entity.Emp;
import com.example.chapter02.mapper.EmpDeptMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/** 员工部门 association/collection 映射测试。 */
public class EmpDeptMapperTest {

    private SqlSessionFactory sqlSessionFactory;

    @Before
    public void init() throws Exception {
        try (InputStream inputStream = Resources.getResourceAsStream("chapter02/mybatis-config.xml")) {
            assertNotNull("找不到 chapter02/mybatis-config.xml", inputStream);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        }
    }

    @Test
    public void testFindEmpWithDept() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            Emp emp = sqlSession.getMapper(EmpDeptMapper.class).findEmpWithDeptById(1);
            assertNotNull(emp);
            assertEquals("SMITH", emp.getEname());
            assertNotNull("员工的部门对象应该完成 association 映射", emp.getDept());
            assertEquals("RESEARCH", emp.getDept().getDname());
        }
    }

    @Test
    public void testFindAllEmpWithDept() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            List<Emp> emps = sqlSession.getMapper(EmpDeptMapper.class).findAllEmpWithDept();
            assertEquals(14, emps.size());
            assertTrue(emps.stream().allMatch(emp -> emp.getDept() != null));
        }
    }

    @Test
    public void testFindDeptWithEmps() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            Dept dept = sqlSession.getMapper(EmpDeptMapper.class).findDeptWithEmpsById(2);
            assertNotNull(dept);
            assertEquals("RESEARCH", dept.getDname());
            assertEquals(5, dept.getEmps().size());
            assertEquals("SMITH", dept.getEmps().get(0).getEname());
        }
    }

    @Test
    public void testFindAllDeptWithEmps() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            List<Dept> depts = sqlSession.getMapper(EmpDeptMapper.class).findAllDeptWithEmps();
            assertEquals(4, depts.size());
            assertEquals(5, depts.get(1).getEmps().size());
        }
    }
}
