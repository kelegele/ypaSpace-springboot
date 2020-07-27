package com.kelegele.ypaSpace.service;

import com.kelegele.ypaSpace.model.JsonResult;
import com.kelegele.ypaSpace.model.User;

import java.util.HashMap;
import java.util.List;

public interface UserService {

    List<User> getUser();

    JsonResult<HashMap> userLogin(User user);

    JsonResult<HashMap> userInfo(String token);
}
