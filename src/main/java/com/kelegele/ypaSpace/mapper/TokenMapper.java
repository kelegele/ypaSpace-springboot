package com.kelegele.ypaSpace.mapper;

import com.kelegele.ypaSpace.entity.Token;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface TokenMapper {

    /* 添加token */
    @Insert("")
    void addToken(Token token);

    /* 修改token */
    @Update("")
    void updataToken(Token token);

    /* 查询token */
    @Select("")
    Token findByUserId(Long userId);
}
