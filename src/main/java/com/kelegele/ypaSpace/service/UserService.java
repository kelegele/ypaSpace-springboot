package com.kelegele.ypaSpace.service;

import com.kelegele.ypaSpace.entity.JsonResult;
import com.kelegele.ypaSpace.entity.User;

import java.util.List;

public interface UserService {

    List<User> getUser();

    JsonResult userLogin(User user);

    JsonResult userRegister(User user);

    JsonResult userInfo(String token);

    JsonResult userLogout(String token);

}
