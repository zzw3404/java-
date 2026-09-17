package com.example.chapter02;

import com.example.chapter02.entity.Vo;
import com.example.chapter02.mapper.UserMapper;
import com.example.chapter02.mapper.UserMapperAnnotation;
import com.example.entity.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/** 迁移自下载的 UserMapperAnnotationTest.java，已适配当前项目包结构。 */
public class UserMapperAnnotationTest {

    private SqlSessionFactory sqlSessionFactory;

    @Before
    public void init() throws Exception {
        try (InputStream inputStream = Resources.getResourceAsStream("chapter02/mybatis-config.xml")) {
            assertNotNull("找不到 chapter02/mybatis-config.xml", inputStream);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        }
    }

    @Test
    public void testFindAllByAnnotation() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            List<User> users = sqlSession.getMapper(UserMapperAnnotation.class).findAll();
            assertEquals(10, users.size());
        }
    }

    @Test
    public void testFindAllByResultType() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            List<User> users = sqlSession.getMapper(UserMapper.class).selectAll();
            assertEquals(10, users.size());
        }
    }

    @Test
    public void testFindAllByResultMap() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            List<Vo> users = sqlSession.getMapper(UserMapper.class).selectAllByVo();
            assertEquals(10, users.size());
            assertEquals(Integer.valueOf(1), users.get(0).getP1());
            assertEquals("testuser1", users.get(0).getP2());
        }
    }

    @Test
    public void testFindAllByAliasResultType() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            List<Vo> users = sqlSession.getMapper(UserMapper.class).selectAllVO();
            assertTrue(users.size() > 0);
            assertEquals(Integer.valueOf(1), users.get(0).getP1());
        }
    }

    @Test
    public void testAddUserByAnnotation() {
        String suffix = UUID.randomUUID().toString().replace("-", "");
        User user = new User();
        user.setUsername("chapter02-annotation-" + suffix);
        user.setPassword("123456");
        user.setEmail("chapter02-annotation-" + suffix + "@example.com");

        try (SqlSession sqlSession = sqlSessionFactory.openSession(true)) {
            int rows = sqlSession.getMapper(UserMapperAnnotation.class).addUser(user);
            assertEquals(1, rows);
            assertNotNull(user.getId());
        } finally {
            if (user.getId() != null) {
                try (SqlSession sqlSession = sqlSessionFactory.openSession(true)) {
                    sqlSession.getMapper(UserMapper.class).deleteUser(user.getId());
                }
            }
        }
    }
}
