package com.kelegele.ypaSpace.mapper;

import com.kelegele.ypaSpace.entity.Token;
import org.apache.ibatis.annotations.*;

public interface TokenMapper {

    /* 添加token */
    @Insert("insert into ypaspace_og.public.token (id,userid,buildtime,token) " +
            "values(#{token.id},#{token.userId},#{token.buildTime},#{token.token});")
    @Options(keyProperty="token.userId",useGeneratedKeys=true)
    void addToken(@Param("token") Token token);

    /* 修改token */
    @Update("update ypaspace_og.public.token set token=#{token.token} where userid=#{token.userId}")
    void updataToken(@Param("token") Token token);

    /* 查询token */
    @Select("select * from ypaspace_og.public.token where userid=#{userid};")
    Token findByUserId(@Param("userid") String userId);

    @Delete("delete from ypaspace_og.public.token where userid=#{userid};")
    void delByUserId(@Param("userid") String userId);
}
