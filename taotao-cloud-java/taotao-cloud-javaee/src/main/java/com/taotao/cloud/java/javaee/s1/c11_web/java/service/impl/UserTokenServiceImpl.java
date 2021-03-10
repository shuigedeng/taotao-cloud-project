package com.taotao.cloud.java.javaee.s1.c11_web.java.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.cloud.java.javaee.s1.c11_web.java.mapper.UserTokenMapper;
import com.taotao.cloud.java.javaee.s1.c11_web.java.pojo.UserToken;
import com.taotao.cloud.java.javaee.s1.c11_web.java.service.UserTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author menglili
 */
@Service
@Transactional
public class UserTokenServiceImpl implements UserTokenService {

    @Autowired
    private UserTokenMapper userTokenMapper;

    @Override
    public PageInfo<UserToken> getTokenList(UserToken criteria, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return new PageInfo<>(userTokenMapper.getTokenList(criteria));
    }

    @Override
    public UserToken getTokenById(int id) {
        return userTokenMapper.getTokenById(id);
    }

    @Override
    public void updateToken(UserToken token) {
        userTokenMapper.updateToken(token);
    }

    @Override
    public void addToken(UserToken token) {
        userTokenMapper.addToken(token);
    }

    @Override
    public void deleteUserToken(int[] ids) {
        if (ids == null) {
            return;
        }
        userTokenMapper.deleteTokens(ids);
    }
}
