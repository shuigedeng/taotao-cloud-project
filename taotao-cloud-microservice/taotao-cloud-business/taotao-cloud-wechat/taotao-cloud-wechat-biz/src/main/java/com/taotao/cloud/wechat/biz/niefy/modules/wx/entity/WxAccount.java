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

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotEmpty;
import java.io.Serializable;
import lombok.Data;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;

/**
 * 公众号账号
 *
 * @author niefy
 * @since 2020-06-17 13:56:51
 */
@Data
@TableName("wx_account")
public class WxAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    /** id */
    @TableId(type = IdType.INPUT)
    @NotEmpty(message = "appid不得为空")
    private String appid;
    /** 公众号名称 */
    @NotEmpty(message = "名称不得为空")
    private String name;
    /** 账号类型 */
    private int type;
    /** 认证状态 */
    private boolean verified;
    /** appsecret */
    @NotEmpty(message = "appSecret不得为空")
    private String secret;
    /** token */
    private String token;
    /** aesKey */
    private String aesKey;

    public WxMpDefaultConfigImpl toWxMpConfigStorage() {
        WxMpDefaultConfigImpl configStorage = new WxMpDefaultConfigImpl();
        configStorage.setAppId(appid);
        configStorage.setSecret(secret);
        configStorage.setToken(token);
        configStorage.setAesKey(aesKey);
        return configStorage;
    }
}
