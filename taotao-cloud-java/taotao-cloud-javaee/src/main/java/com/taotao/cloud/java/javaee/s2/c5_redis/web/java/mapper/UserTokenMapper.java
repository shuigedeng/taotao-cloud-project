package com.taotao.cloud.java.javaee.s2.c5_redis.web.java.mapper;

import com.qianfeng.openapi.web.master.pojo.UserToken;

import java.util.List;

public interface UserTokenMapper {
    List<UserToken> getTokenList(UserToken criteria);

    UserToken getTokenById(int id);

    void updateToken(UserToken token);

    void addToken(UserToken token);

    void deleteTokens(int[] ids);
}
