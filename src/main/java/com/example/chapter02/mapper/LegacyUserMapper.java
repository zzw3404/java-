package com.example.chapter02.mapper;

import com.example.entity.User;

import java.util.List;
import java.util.Map;

/** 对应下载文件 UserMapper (1).xml 的 XML 映射器示例。 */
public interface LegacyUserMapper {

    List<User> findAllByxml();
    User findByIdXML(Integer id);
    List<User> findByNameAndPass(User user);
    List<User> findByUsernameXML(String username);
    List<User> findByMap(Map<String, Object> params);
    Integer findCount();
    int addUserxml(User user);
    int addUser(User user);
    int updateUser(User user);
    int deleteUser(Integer id);
}
