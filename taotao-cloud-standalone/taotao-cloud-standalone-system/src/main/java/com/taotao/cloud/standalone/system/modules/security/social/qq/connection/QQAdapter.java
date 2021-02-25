package com.taotao.cloud.standalone.system.modules.security.social.qq.connection;

import com.taotao.cloud.standalone.system.modules.security.social.qq.api.QQ;
import com.taotao.cloud.standalone.system.modules.security.social.qq.api.QQUserInfo;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;

/**
 * 个性化服务提供商的数据（此处是从QQ获取的用户信息） 与 OAuth2协议的标准数据 之间的适配器
 * 所要适配的个性化api接口
 */
public class QQAdapter implements ApiAdapter<QQ> {
    /**
     * 用来测试当前的API是否可用
     * @param qq
     * @return
     */
    @Override
    public boolean test(QQ qq) {
        return true;
    }

    /**
     * 将服务提供商个性化的用户信息映射到ConnectionValues标准的数据化结构上
     * @param qq
     * @param connectionValues
     */
    @Override
    public void setConnectionValues(QQ qq, ConnectionValues connectionValues) {
        QQUserInfo userInfo = qq.getUserInfo();
        //显示的用户名称
        connectionValues.setDisplayName(userInfo.getNickname());
        //用户的头像
        connectionValues.setImageUrl(userInfo.getFigureurl_qq_1());
        //个人主页
        connectionValues.setProfileUrl(null);
        //QQ的唯一标识
        connectionValues.setProviderUserId(userInfo.getOpenId());
    }

    /**
     * 和上面的方法类似
     * @param qq
     * @return
     */
    @Override
    public UserProfile fetchUserProfile(QQ qq) {
        return null;
    }

    /**
     *
     * @param qq
     * @param s
     */
    @Override
    public void updateStatus(QQ qq, String s) {

    }
}
