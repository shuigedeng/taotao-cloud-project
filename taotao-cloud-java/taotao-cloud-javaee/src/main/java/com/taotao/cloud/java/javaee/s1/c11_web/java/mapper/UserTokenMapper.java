package com.taotao.cloud.java.javaee.s1.c11_web.java.mapper;


import com.taotao.cloud.java.javaee.s1.c11_web.java.pojo.UserToken;
import java.util.List;

public interface UserTokenMapper {
    List<UserToken> getTokenList(UserToken criteria);

    UserToken getTokenById(int id);

    void updateToken(UserToken token);

    void addToken(UserToken token);

    void deleteTokens(int[] ids);
}
