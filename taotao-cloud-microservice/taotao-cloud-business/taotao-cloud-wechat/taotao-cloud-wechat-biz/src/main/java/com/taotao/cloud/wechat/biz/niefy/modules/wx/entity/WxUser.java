/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.wechat.biz.niefy.modules.wx.entity;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.niefy.common.utils.Json;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.experimental.*;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.util.StringUtils;

/**
 * 微信粉丝
 *
 * @author Nifury
 * @since 2017-9-27
 */
@Data
@TableName("wx_user")
public class WxUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.INPUT)
    private String openid;

    private String appid;
    private String phone;
    private String nickname;
    private int sex;
    private String city;
    private String province;
    private String headimgurl;

    @JSONField(name = "subscribe_time")
    private Date subscribeTime;

    private boolean subscribe;
    private String unionid;
    private String remark;
    private JSONArray tagidList;
    private String subscribeScene;
    private String qrSceneStr;

    public WxUser() {}

    public WxUser(String openid) {
        this.openid = openid;
    }

    public WxUser(WxMpUser wxMpUser, String appid) {
        this.openid = wxMpUser.getOpenId();
        this.appid = appid;
        this.subscribe = wxMpUser.getSubscribe();
        if (wxMpUser.getSubscribe()) {
            this.nickname = wxMpUser.getNickname();
            this.headimgurl = wxMpUser.getHeadImgUrl();
            this.subscribeTime = new Date(wxMpUser.getSubscribeTime() * 1000);
            this.unionid = wxMpUser.getUnionId();
            this.remark = wxMpUser.getRemark();
            this.tagidList = JSONArray.parseArray(JSONObject.toJSONString(wxMpUser.getTagIds()));
            this.subscribeScene = wxMpUser.getSubscribeScene();
            String qrScene = wxMpUser.getQrScene();
            this.qrSceneStr = !StringUtils.hasText(qrScene) ? wxMpUser.getQrSceneStr() : qrScene;
        }
    }

    public WxUser(WxOAuth2UserInfo wxMpUser, String appid) {
        this.openid = wxMpUser.getOpenid();
        this.appid = appid;
        this.subscribe = wxMpUser.getNickname() != null;
        if (this.subscribe) {
            this.nickname = wxMpUser.getNickname();
            this.headimgurl = wxMpUser.getHeadImgUrl();
            this.unionid = wxMpUser.getUnionId();
        }
    }

    @Override
    public String toString() {
        return Json.toJsonString(this);
    }
}
