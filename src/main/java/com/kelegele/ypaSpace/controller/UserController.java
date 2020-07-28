package com.kelegele.ypaSpace.controller;

import com.kelegele.ypaSpace.entity.JsonResult;
import com.kelegele.ypaSpace.entity.User;
import com.kelegele.ypaSpace.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value ="/list", method = RequestMethod.GET)
    public List<User> UserQry() {
        return userService.getUser();
    }

    @RequestMapping(value ="/login", method = RequestMethod.POST)
    public JsonResult<HashMap> UserLogin(@RequestBody User user) {
        return userService.userLogin(user);
    }

    @RequestMapping(value ="/info", method = RequestMethod.GET)
    public JsonResult<HashMap> UserInfo(@RequestHeader("X_Token") String token, @RequestParam String name) {

        return userService.userInfo(token);
    }
}
