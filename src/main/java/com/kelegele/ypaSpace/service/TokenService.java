package com.kelegele.ypaSpace.service;

import com.kelegele.ypaSpace.entity.Token;

public interface TokenService {

    /* 添加token */
    void addToken(Token token);

    /* 修改token */
    void updataToken(Token token);

    /* 查询token */
    Token findByUserId(Long userId);
}
