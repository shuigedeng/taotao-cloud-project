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

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.niefy.modules.wx.form.WxQrCodeForm;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 公众号带参二维码
 *
 * @author niefy
 * @email niefy@qq.com
 * @since 2020-01-02 11:11:55
 */
@Data
@TableName("wx_qr_code")
public class WxQrCode implements Serializable {
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId
    private Long id;

    private String appid;
    /** 二维码类型 */
    private Boolean isTemp;
    /** 场景值ID */
    private String sceneStr;
    /** 二维码ticket */
    private String ticket;
    /** 二维码图片解析后的地址 */
    private String url;
    /** 该二维码失效时间 */
    private Date expireTime;
    /** 该二维码创建时间 */
    private Date createTime;

    public WxQrCode() {}

    public WxQrCode(WxQrCodeForm form, String appid) {
        this.appid = appid;
        this.isTemp = form.getIsTemp();
        this.sceneStr = form.getSceneStr();
        this.createTime = new Date();
    }
}
