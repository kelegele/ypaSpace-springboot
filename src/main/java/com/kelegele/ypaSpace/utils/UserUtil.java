package com.kelegele.ypaSpace.utils;

import io.jsonwebtoken.Claims;

public class UserUtil {

    public static String getUserIdByToken(String token){

        Claims claims = JwtUtil.parseJWT(token);
        return (String) claims.get("userId");

    }
}
