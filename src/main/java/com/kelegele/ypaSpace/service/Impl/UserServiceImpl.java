package com.kelegele.ypaSpace.service.Impl;

import cn.hutool.core.util.IdUtil;
import com.alibaba.druid.util.StringUtils;
import com.google.gson.Gson;
import com.kelegele.ypaSpace.config.Constant;
import com.kelegele.ypaSpace.entity.JsonResult;
import com.kelegele.ypaSpace.entity.Token;
import com.kelegele.ypaSpace.entity.User;
import com.kelegele.ypaSpace.mapper.TokenMapper;
import com.kelegele.ypaSpace.mapper.UserMapper;
import com.kelegele.ypaSpace.service.HdfsService;
import com.kelegele.ypaSpace.service.UserService;
import com.kelegele.ypaSpace.utils.JwtUtil;
import com.kelegele.ypaSpace.utils.MD5Util;
import com.kelegele.ypaSpace.utils.UserUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired(required = false)
    private UserMapper userMapper;

    @Autowired(required = false)
    private TokenMapper tokenMapper;


    @Value("${hdfs.userpath}")
    private String userpath;

    private static String hdfsPath;

    @PostConstruct
    public void getHdfsPath() {
        hdfsPath = this.userpath;
    }

    private String getUserHdfsPath(String userid){
        return hdfsPath + userid ;
    }

    @Override
    public List<User> getUser() {
        List<User> userList = userMapper.getUserList();
        return userList;
    }

    @Override
    public JsonResult userLogin(User user) {

        //判断用户信息为空
        if (StringUtils.isEmpty(user.getUsername()) || StringUtils.isEmpty(user.getPassword())) {
            return JsonResult.failed("用户名，密码不为空");
        }

        //根据用户名查询user对象
        User u = userMapper.findByUsername(user.getUsername());
        if (u == null) {
            return JsonResult.failed("用户不存在");
        }

        if (!MD5Util.md5Encrypt32Upper(user.getPassword()).equals(u.getPassword())) {
            return JsonResult.failed("密码错误");
        }

        //根据数据库的用户信息查询Token
        return operateToKen(u.getUserId());

    }

    private JsonResult operateToKen(String userId) {
        //根据数据库的用户信息查询Token
        Token token = tokenMapper.findByUserId(userId);

        //为生成Token准备
        String TokenStr = "";
        Date date = new Date();
        int nowTime = (int) (date.getTime() / 1000);
        Gson ug = new Gson();

        try {
            //生成Token
            TokenStr = JwtUtil.createJWT(userId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (null == token) {
            //第一次登录
            token = new Token();
            token.setToken(TokenStr);
            token.setBuildTime(nowTime);
            token.setUserId(userId);
            token.setId(String.valueOf(IdUtil.fastSimpleUUID()));
            tokenMapper.addToken(token);
        } else {
            //登录就更新Token信息
            token.setToken(TokenStr);
            token.setBuildTime(nowTime);
            tokenMapper.updataToken(token);
        }


        //返回Token信息给客户端
        HashMap<String, String> map = new HashMap<>();
        map.put("token", TokenStr);
        return JsonResult.success(map, "登录成功！");
    }

    @Override
    public JsonResult userRegister(User user) {
        //判断用户信息为空
        if (StringUtils.isEmpty(user.getUsername()) || StringUtils.isEmpty(user.getPassword())) {
            return JsonResult.failed("用户名，密码不为空");
        }

        //根据用户名查询user对象
        User u = userMapper.findByUsername(user.getUsername());
        if (u != null) {
            return JsonResult.failed("用户名已存在！");
        }

        user.setUserid(IdUtil.randomUUID());
        user.setPassword(MD5Util.md5Encrypt32Upper(user.getPassword()));

        //在hadoop里创建对应用户文件夹
        try {
            HdfsService.mkdir(getUserHdfsPath(user.getUserId()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        userMapper.addUser(user);

        return JsonResult.success("注册成功！");
    }

    @Override
    public JsonResult userInfo(String token) {
        String tokenUserId = null;

        //解析Token信息
        try {
            Claims claims = JwtUtil.parseJWT(token);
            tokenUserId = (String) claims.get("userId");
            //根据客户Token查找数据库Token
            Token myToken = tokenMapper.findByUserId(tokenUserId);

            //数据库没有Token记录
            if (null == myToken) {
                return JsonResult.failed("不存在token！");
            }
            //数据库Token与客户Token比较
            if (!token.equals(myToken.getToken())) {
                return JsonResult.failed("token错误！");
            }
            //判断Token过期
            Date tokenDate = claims.getExpiration();
            int overTime = (int) (new Date().getTime() - tokenDate.getTime()) / 1000;
            if (overTime > Constant.JWT_TTL) {
                return JsonResult.failed("token过期！");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        User user = userMapper.findByUserId(tokenUserId);
        HashMap<String, String> map = new HashMap<>();
        map.put("name", user.getUsername());
        map.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");

        return JsonResult.success(map, "用户信息");
    }

    @Override
    public JsonResult userLogout(String token) {
        try {
            String tokenUserId = UserUtil.getUserIdByToken(token);
            tokenMapper.delByUserId(tokenUserId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return JsonResult.success("退出成功");
    }
}
