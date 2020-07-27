package com.kelegele.ypaSpace.service.Impl;

import com.kelegele.ypaSpace.mapper.UserMapper;
import com.kelegele.ypaSpace.model.JsonResult;
import com.kelegele.ypaSpace.model.User;
import com.kelegele.ypaSpace.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired(required=false)
    private UserMapper userMapper;

    @Override
    public List<User> getUser() {
        List<User> userList = userMapper.getUser();
        Collections.sort(userList,(u1, u2)->u1.getCreatetime() - u2.getCreatetime());
        return userList;
    }

    @Override
    public JsonResult userLogin(User user) {
        if (user.getUsername().equals("admin") && user.getPassword().equals("201014"))
        {
            HashMap<String,String> token = new HashMap<String, String>();
            token.put("token","admin-token");
            return JsonResult.success(token);
        }
        else
        {
            return JsonResult.validateFailed();
        }

    }

    @Override
    public JsonResult<HashMap> userInfo(String token) {
        return null;
    }
}
