package com.kelegele.ypaSpace.utils;

public class TokenUtil {

    public boolean checkToken(String token){
        boolean b = "admin-token".equals(token) ? true : false;
        return b;
    }

}
