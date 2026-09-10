package com.example.mapper;

import com.example.entity.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

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

    /** 使用 @Param 明确指定多个参数的名字。 */
    @Select("SELECT id, username, password, email, created_at, updated_at "
            + "FROM `user` WHERE username = #{username} AND email = #{email}")
    List<User> findByNameAndEmail(@Param("username") String username,
                                  @Param("email") String email);

    /** Map 中的 key 会对应 SQL 中的参数名。 */
    @Select("SELECT id, username, password, email, created_at, updated_at "
            + "FROM `user` WHERE username = #{username} AND email = #{email}")
    List<User> findByMap(Map<String, Object> params);

    @Select("SELECT id, username, password, email, created_at, updated_at "
            + "FROM `user` WHERE username LIKE CONCAT('%', #{username}, '%') "
            + "ORDER BY id")
    List<User> findByUsernameLike(@Param("username") String username);

    /** #{username} 使用预编译参数，是正常业务中的安全写法。 */
    @Select("SELECT id, username, password, email, created_at, updated_at "
            + "FROM `user` WHERE username = #{username} AND password = #{password}")
    User loginSafe(@Param("username") String username,
                   @Param("password") String password);

    /**
     * 仅用于课堂演示 ${} 的 SQL 注入风险，禁止在真实业务中使用。
     */
    @Deprecated
    @Select("SELECT id, username, password, email, created_at, updated_at "
            + "FROM `user` WHERE username = '${username}' AND password = '${password}'")
    User loginUnsafe(@Param("username") String username,
                     @Param("password") String password);
}
