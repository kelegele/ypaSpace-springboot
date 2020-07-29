package com.kelegele.ypaSpace.mapper;

import com.kelegele.ypaSpace.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserMapper {

    @Select("select * from ypaspace_og.public.user;")
    List<User> getUserList();

    @Select("select * from ypaspace_og.public.user where username=#{username};")
    User findByUsername(@Param("username") String username);

    @Select("select * from ypaspace_og.public.user where userid=#{userid};")
    User findByUserId(@Param("userid") String userId);

    @Insert("insert into ypaspace_og.public.user (userid,username,password) " +
            "values(#{user.userId},#{user.username},#{user.password});")
    void addUser(@Param("user") User user);


}
