package com.example.chapter02.mapper;

import com.example.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/** chapter02 的注解 Mapper 示例。 */
public interface UserMapperAnnotation {

    @Select("SELECT id, username, password, email, created_at, updated_at "
            + "FROM `user` ORDER BY id")
    List<User> findAll();

    @Insert("INSERT INTO `user` (username, password, email) "
            + "VALUES (#{username}, #{password}, #{email})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int addUser(User user);
}
