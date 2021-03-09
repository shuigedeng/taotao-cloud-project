package com.taotao.cloud.java.javaee.s2.c5_redis.web.java.service;

import com.github.pagehelper.PageInfo;
import com.qianfeng.openapi.web.master.pojo.UserToken;

public interface UserTokenService {
    PageInfo<UserToken> getTokenList(UserToken criteria, int page, int pageSize);

    UserToken getTokenById(int id);

    void updateToken(UserToken token);

    void addToken(UserToken token);

    void deleteUserToken(int[] ids);
}
