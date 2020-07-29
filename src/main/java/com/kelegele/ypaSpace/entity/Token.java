package com.kelegele.ypaSpace.entity;

import lombok.Data;

@Data
public class Token {

    /* tokenId */
    private String id;

    /* 用户ID */
    private String userId;

    /* 刷新时间 */
    private int buildTime;

    /* token */
    private String token;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
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
