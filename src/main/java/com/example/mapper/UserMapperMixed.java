package com.example.mapper;

import com.example.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * XML 与注解混合开发示例：简单查询使用注解，动态条件查询放在 XML 中。
 */
public interface UserMapperMixed {

    @Select("SELECT id, username, password, email, created_at, updated_at "
            + "FROM `user` WHERE id = #{id}")
    User findById(@Param("id") Integer id);

    List<User> findByCondition(User condition);
}
