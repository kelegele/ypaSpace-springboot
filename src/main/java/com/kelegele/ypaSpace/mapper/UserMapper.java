package com.kelegele.ypaSpace.mapper;

import com.kelegele.ypaSpace.entity.User;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserMapper {

    @Select("select * from ypaspace_og.public.\"user\";")
    List<User> getUser();
}
