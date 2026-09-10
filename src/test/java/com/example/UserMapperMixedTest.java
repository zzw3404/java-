package com.example;

import com.example.entity.User;
import com.example.mapper.UserMapperMixed;
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

/** XML 与注解混合 Mapper 集成测试。 */
public class UserMapperMixedTest {

    private SqlSessionFactory sqlSessionFactory;

    @Before
    public void init() throws Exception {
        try (InputStream inputStream = Resources.getResourceAsStream("chapter01/mybatis-config.xml")) {
            assertNotNull("找不到 chapter01/mybatis-config.xml", inputStream);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        }
    }

    @Test
    public void testAnnotationMethod() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            User user = sqlSession.getMapper(UserMapperMixed.class).findById(1);
            assertNotNull(user);
            assertEquals(Integer.valueOf(1), user.getId());
        }
    }

    @Test
    public void testXmlDynamicCondition() {
        User condition = new User();
        condition.setEmail("testuser1@example.com");

        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            List<User> users = sqlSession.getMapper(UserMapperMixed.class)
                    .findByCondition(condition);
            assertEquals(1, users.size());
            assertEquals("testuser1", users.get(0).getUsername());
        }
    }

    @Test
    public void testXmlDynamicConditionWithTwoFields() {
        User condition = new User();
        condition.setUsername("testuser");
        condition.setEmail("testuser2@example.com");

        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            List<User> users = sqlSession.getMapper(UserMapperMixed.class)
                    .findByCondition(condition);
            assertTrue(users.size() > 0);
            assertEquals("testuser2", users.get(0).getUsername());
        }
    }
}
