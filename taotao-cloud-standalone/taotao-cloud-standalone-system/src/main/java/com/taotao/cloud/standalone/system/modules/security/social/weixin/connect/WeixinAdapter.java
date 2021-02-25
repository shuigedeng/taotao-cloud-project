package com.taotao.cloud.standalone.system.modules.security.social.weixin.connect;

import com.taotao.cloud.standalone.system.modules.security.social.weixin.api.Weixin;
import com.taotao.cloud.standalone.system.modules.security.social.weixin.api.WeixinUserInfo;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;


/**
 * @Classname WeixinAdapter
 * @Description 微信 api适配器，将微信 api的数据模型转为spring social的标准模型
 * @Author Created by Lihaodong (alias:小东啊) lihaodongmail@163.com
 * @since 2019-08-23 16:50
 * @Version 1.0
 */
public class WeixinAdapter implements ApiAdapter<Weixin> {

    private String openId;

    public WeixinAdapter() {
    }

    public WeixinAdapter(String openId) {
        this.openId = openId;
    }

    @Override
    public boolean test(Weixin api) {
        return true;
    }

    @Override
    public void setConnectionValues(Weixin api, ConnectionValues values) {
        WeixinUserInfo userInfo = api.getUserInfo(openId);
        values.setProviderUserId(userInfo.getOpenid());
        values.setDisplayName(userInfo.getNickname());
        values.setImageUrl(userInfo.getHeadimgurl());
    }

    @Override
    public UserProfile fetchUserProfile(Weixin api) {
        return null;
    }

    @Override
    public void updateStatus(Weixin api, String message) {

    }
}
