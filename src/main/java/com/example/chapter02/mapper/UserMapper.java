package com.example.chapter02.mapper;

import com.example.chapter02.entity.Vo;
import com.example.entity.User;

import java.util.List;

/** 对应 chapter02 UserMapper.xml，演示 resultType/resultMap 和 XML CRUD。 */
public interface UserMapper {

    User findById(Integer id);
    int addUser(User user);
    int updateUser(User user);
    int deleteUser(Integer id);

    User selectById(Integer id);
    int insert(User user);
    int update(User user);
    int deleteById(Integer id);

    List<Vo> selectAllByVo();
    List<User> selectAll();
    List<Vo> selectAllVO();
}
