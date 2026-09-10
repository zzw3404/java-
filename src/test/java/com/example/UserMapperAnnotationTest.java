package com.example;

import com.example.entity.User;
import com.example.mapper.UserMapperAnnotation;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/** 注解版 UserMapper 集成测试。 */
public class UserMapperAnnotationTest {

    private SqlSessionFactory sqlSessionFactory;

    @Before
    public void init() throws Exception {
        try (InputStream inputStream = Resources.getResourceAsStream("chapter01/mybatis-config.xml")) {
            assertNotNull("找不到 chapter01/mybatis-config.xml", inputStream);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        }
    }

    @Test
    public void testFindAll() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            List<User> users = sqlSession.getMapper(UserMapperAnnotation.class).findAll();
            assertNotNull(users);
            assertTrue("user 表中应该至少有一条测试数据", users.size() > 0);
        }
    }

    @Test
    public void testFindById() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            User user = sqlSession.getMapper(UserMapperAnnotation.class).findById(1);
            assertNotNull("id=1 的用户不存在，请先执行 sql/User_db.sql", user);
            assertEquals(Integer.valueOf(1), user.getId());
        }
    }

    @Test
    public void testAddUser() {
        User user = newTemporaryUser("annotation-add");
        try (SqlSession sqlSession = sqlSessionFactory.openSession(true)) {
            int rows = sqlSession.getMapper(UserMapperAnnotation.class).addUser(user);
            assertEquals(1, rows);
            assertNotNull("插入成功后应该回填自增 id", user.getId());
        } finally {
            deleteIfCreated(user.getId());
        }
    }

    @Test
    public void testUpdateUser() {
        User original;
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            original = sqlSession.getMapper(UserMapperAnnotation.class).findById(1);
        }
        assertNotNull("id=1 的用户不存在，无法测试更新", original);

        User changed = new User();
        changed.setId(original.getId());
        changed.setUsername(original.getUsername() + "-annotation-updated");
        changed.setPassword(original.getPassword());
        changed.setEmail(original.getEmail());

        try (SqlSession sqlSession = sqlSessionFactory.openSession(true)) {
            int rows = sqlSession.getMapper(UserMapperAnnotation.class).updateUser(changed);
            assertEquals(1, rows);
        } finally {
            try (SqlSession sqlSession = sqlSessionFactory.openSession(true)) {
                sqlSession.getMapper(UserMapperAnnotation.class).updateUser(original);
            }
        }
    }

    @Test
    public void testDeleteUser() {
        User user = newTemporaryUser("annotation-delete");
        try (SqlSession sqlSession = sqlSessionFactory.openSession(true)) {
            UserMapperAnnotation mapper = sqlSession.getMapper(UserMapperAnnotation.class);
            mapper.addUser(user);
            assertNotNull(user.getId());

            int rows = mapper.deleteUser(user.getId());
            assertEquals(1, rows);
            assertTrue("删除后不应该再查到该用户", mapper.findById(user.getId()) == null);
        } finally {
            deleteIfCreated(user.getId());
        }
    }

    @Test
    public void testParamAndMapArguments() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            UserMapperAnnotation mapper = sqlSession.getMapper(UserMapperAnnotation.class);

            List<User> byParam = mapper.findByNameAndEmail(
                    "testuser1", "testuser1@example.com");
            assertEquals(1, byParam.size());

            Map<String, Object> params = new HashMap<>();
            params.put("username", "testuser1");
            params.put("email", "testuser1@example.com");
            List<User> byMap = mapper.findByMap(params);
            assertEquals(1, byMap.size());

            assertTrue(mapper.findByUsernameLike("testuser").size() >= 10);
        }
    }

    @Test
    @SuppressWarnings("deprecation")
    public void testHashPlaceholderAndDollarPlaceholder() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            UserMapperAnnotation mapper = sqlSession.getMapper(UserMapperAnnotation.class);

            User safeLogin = mapper.loginSafe("testuser1", "password123");
            assertNotNull("正确账号密码应该能够登录", safeLogin);

            User injectionAgainstSafeSql = mapper.loginSafe("testuser1' -- ", "wrong");
            assertNull("#{ } 预编译参数应该阻止注入", injectionAgainstSafeSql);

            User injectionAgainstUnsafeSql = mapper.loginUnsafe("testuser1' -- ", "wrong");
            assertNotNull("${ } 仅用于演示：该输入会暴露 SQL 注入风险", injectionAgainstUnsafeSql);
        }
    }

    private User newTemporaryUser(String prefix) {
        String suffix = UUID.randomUUID().toString().replace("-", "");
        User user = new User();
        user.setUsername(prefix + "-" + suffix);
        user.setPassword("123456");
        user.setEmail(prefix + "-" + suffix + "@example.com");
        return user;
    }

    private void deleteIfCreated(Integer id) {
        if (id == null) {
            return;
        }
        try (SqlSession sqlSession = sqlSessionFactory.openSession(true)) {
            sqlSession.getMapper(UserMapperAnnotation.class).deleteUser(id);
        }
    }
}
