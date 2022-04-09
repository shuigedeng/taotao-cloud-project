package com.taotao.cloud.member.biz.connect.token.base;


import com.taotao.cloud.common.enums.UserEnum;
import com.taotao.cloud.member.biz.connect.token.Token;

/**
 * AbstractToken
 * 抽象token，定义生成token类
 */
public abstract class AbstractTokenGenerate<T> {

    /**
     * 生成token
     *
     * @param user 用户名
     * @param longTerm 是否长时间有效
     * @return TOKEN对象
     */
    public abstract Token createToken(T user, Boolean longTerm);

    /**
     * 刷新token
     *
     * @param refreshToken 刷新token
     * @return token
     */
    public abstract Token refreshToken(String refreshToken);

    /**
     * 默认role
     */
    public UserEnum role = UserEnum.MANAGER;

}
