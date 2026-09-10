package com.example.mapper;

import com.example.entity.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 用户 Mapper 注解版 CRUD 示例。
 *
 * <p>简单 SQL 可以直接写在接口注解中，不需要单独的 XML Mapper 文件。</p>
 */
public interface UserMapperAnnotation {

    @Select("SELECT id, username, password, email, created_at, updated_at "
            + "FROM `user` ORDER BY id")
    List<User> findAll();

    @Select("SELECT id, username, password, email, created_at, updated_at "
            + "FROM `user` WHERE id = #{id}")
    User findById(@Param("id") Integer id);

    @Insert("INSERT INTO `user` (username, password, email) "
            + "VALUES (#{username}, #{password}, #{email})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int addUser(User user);

    @Update("UPDATE `user` SET username = #{username}, password = #{password}, "
            + "email = #{email} WHERE id = #{id}")
    int updateUser(User user);

    @Delete("DELETE FROM `user` WHERE id = #{id}")
    int deleteUser(@Param("id") Integer id);
}
