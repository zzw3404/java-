package com.example.chapter02;

import com.example.chapter02.mapper.LegacyUserMapper;
import com.example.entity.User;
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
import static org.junit.Assert.assertTrue;

/** 迁移自下载的 UserMapperTest (1).java，已适配当前项目包结构。 */
public class UserMapperTest {

    private SqlSessionFactory sqlSessionFactory;

    @Before
    public void init() throws Exception {
        try (InputStream inputStream = Resources.getResourceAsStream("chapter02/mybatis-config.xml")) {
            assertNotNull("找不到 chapter02/mybatis-config.xml", inputStream);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        }
    }

    @Test
    public void testFindAllByXml() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            List<User> users = sqlSession.getMapper(LegacyUserMapper.class).findAllByxml();
            assertEquals(10, users.size());
        }
    }

    @Test
    public void testFindByIdByXml() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            User user = sqlSession.getMapper(LegacyUserMapper.class).findByIdXML(7);
            assertNotNull(user);
            assertEquals(Integer.valueOf(7), user.getId());
        }
    }

    @Test
    public void testFindByNameAndPass() {
        User condition = new User();
        condition.setUsername("not-exist");
        condition.setPassword("password123");

        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            List<User> users = sqlSession.getMapper(LegacyUserMapper.class)
                    .findByNameAndPass(condition);
            assertTrue(users.size() >= 5);
        }
    }

    @Test
    public void testFindByUsernameByXml() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            List<User> users = sqlSession.getMapper(LegacyUserMapper.class)
                    .findByUsernameXML("testuser1");
            assertEquals(1, users.size());
        }
    }

    @Test
    public void testFindByMap() {
        Map<String, Object> params = new HashMap<>();
        params.put("abc", "not-exist");
        params.put("username", "testuser1");

        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            List<User> users = sqlSession.getMapper(LegacyUserMapper.class).findByMap(params);
            assertEquals(1, users.size());
        }
    }

    @Test
    public void testFindCountByXml() {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            int count = sqlSession.getMapper(LegacyUserMapper.class).findCount();
            assertEquals(10, count);
        }
    }

    @Test
    public void testInsertByXml() {
        User user = temporaryUser("chapter02-xml");
        try (SqlSession sqlSession = sqlSessionFactory.openSession(true)) {
            LegacyUserMapper mapper = sqlSession.getMapper(LegacyUserMapper.class);
            int rows = mapper.addUserxml(user);
            assertEquals(1, rows);
            assertNotNull(user.getId());
        } finally {
            deleteIfCreated(user.getId());
        }
    }

    @Test
    public void testUpdateUserByXml() {
        User original;
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            original = sqlSession.getMapper(LegacyUserMapper.class).findByIdXML(2);
        }
        assertNotNull(original);

        User changed = new User();
        changed.setId(original.getId());
        changed.setUsername(original.getUsername() + "-chapter02");
        changed.setPassword(original.getPassword());
        changed.setEmail(original.getEmail());

        try (SqlSession sqlSession = sqlSessionFactory.openSession(true)) {
            int rows = sqlSession.getMapper(LegacyUserMapper.class).updateUser(changed);
            assertEquals(1, rows);
        } finally {
            try (SqlSession sqlSession = sqlSessionFactory.openSession(true)) {
                sqlSession.getMapper(LegacyUserMapper.class).updateUser(original);
            }
        }
    }

    private User temporaryUser(String prefix) {
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
            sqlSession.getMapper(LegacyUserMapper.class).deleteUser(id);
        }
    }
}
