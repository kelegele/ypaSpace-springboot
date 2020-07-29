package com.kelegele.ypaSpace.controller;

import com.kelegele.ypaSpace.entity.JsonResult;
import com.kelegele.ypaSpace.entity.User;
import com.kelegele.ypaSpace.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public JsonResult UserLogin(@RequestBody User user) {
        return userService.userLogin(user);
    }

    @RequestMapping(value ="/logout", method = RequestMethod.GET)
    public JsonResult UserLogout(@RequestParam("token") String token) {
        return userService.userLogout(token);
    }

    @RequestMapping(value ="/register", method = RequestMethod.POST)
    public JsonResult UserRegister(@RequestBody User user) {
        return userService.userRegister(user);
    }

    @RequestMapping(value ="/info", method = RequestMethod.GET)
    public JsonResult UserInfo(@RequestParam("token") String token) {
        return userService.userInfo(token);
    }
}
