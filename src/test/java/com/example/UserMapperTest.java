package com.example;

import com.example.entity.User;
import com.example.mapper.UserMapper;
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

/**
 * chapter01 的 UserMapper 集成测试。
 *
 * <p>运行前请确认 common/db.properties 中的数据库连接信息正确，
 * 并且已经执行 sql/User_db.sql。</p>
 */
public class UserMapperTest {

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
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            List<User> users = userMapper.findAll();

            assertNotNull(users);
            assertTrue("user 表中应该至少有一条测试数据", users.size() > 0);
            users.forEach(System.out::println);
        }
    }

    @Test
    public void testFindById() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            User user = userMapper.findById(1);

            assertNotNull("id=1 的用户不存在，请先执行 sql/User_db.sql", user);
            System.out.println(user);
        }
    }

    @Test
    public void testAddUser() {
        User user = new User();
        String suffix = UUID.randomUUID().toString().replace("-", "");
        user.setUsername("xml-test-" + suffix);
        user.setPassword("123456");
        user.setEmail("xml-test-" + suffix + "@example.com");

        try (SqlSession sqlSession = sqlSessionFactory.openSession(true)) {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            int rows = userMapper.addUser(user);

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
            original = sqlSession.getMapper(UserMapper.class).findById(1);
        }
        assertNotNull("id=1 的用户不存在，无法测试更新", original);

        User changed = new User();
        changed.setId(original.getId());
        changed.setUsername(original.getUsername() + "-updated");
        changed.setPassword(original.getPassword());
        changed.setEmail(original.getEmail());

        try (SqlSession sqlSession = sqlSessionFactory.openSession(true)) {
            int rows = sqlSession.getMapper(UserMapper.class).updateUser(changed);
            assertEquals(1, rows);
        } finally {
            try (SqlSession sqlSession = sqlSessionFactory.openSession(true)) {
                sqlSession.getMapper(UserMapper.class).updateUser(original);
            }
        }
    }

    @Test
    public void testDeleteUser() {
        User user = new User();
        String suffix = UUID.randomUUID().toString().replace("-", "");
        user.setUsername("xml-delete-" + suffix);
        user.setPassword("123456");
        user.setEmail("xml-delete-" + suffix + "@example.com");

        try (SqlSession sqlSession = sqlSessionFactory.openSession(true)) {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            userMapper.addUser(user);
            assertNotNull(user.getId());

            int rows = userMapper.deleteUser(user.getId());
            assertEquals(1, rows);
            assertTrue("删除后不应该再查到该用户",
                    userMapper.findById(user.getId()) == null);
        } finally {
            deleteIfCreated(user.getId());
        }
    }

    private void deleteIfCreated(Integer id) {
        if (id == null) {
            return;
        }
        try (SqlSession sqlSession = sqlSessionFactory.openSession(true)) {
            sqlSession.getMapper(UserMapper.class).deleteUser(id);
        }
    }
}
