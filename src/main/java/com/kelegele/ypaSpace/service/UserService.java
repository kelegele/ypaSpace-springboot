package com.kelegele.ypaSpace.service;

import com.kelegele.ypaSpace.entity.JsonResult;
import com.kelegele.ypaSpace.entity.User;

import java.util.HashMap;
import java.util.List;

public interface UserService {

    List<User> getUser();

    JsonResult<HashMap> userLogin(User user);

    JsonResult<HashMap> userInfo(String token);
}
