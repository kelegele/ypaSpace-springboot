package com.kelegele.ypaSpace.entity;

import lombok.Data;

@Data
public class Token {

    /* tokenId */
    private Long id;

    /* 用户ID */
    private Long userId;

    /* 刷新时间 */
    private int buildTime;

    /* token */
    private String token;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getBuildTime() {
        return buildTime;
    }

    public void setBuildTime(int buildTime) {
        this.buildTime = buildTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
