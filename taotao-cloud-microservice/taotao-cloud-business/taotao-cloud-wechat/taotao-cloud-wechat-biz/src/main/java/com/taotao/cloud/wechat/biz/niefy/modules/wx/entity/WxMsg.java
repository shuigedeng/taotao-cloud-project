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

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.experimental.*;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.util.WxMpConfigStorageHolder;

/**
 * 微信消息
 *
 * @author niefy
 * @since 2020-05-14 17:28:34
 */
@Data
@TableName("wx_msg")
public class WxMsg implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId
    private Long id;

    private String appid;
    /** 微信用户ID */
    private String openid;
    /** 消息方向 */
    private byte inOut;
    /** 消息类型 */
    private String msgType;
    /** 消息详情 */
    private JSONObject detail;
    /** 创建时间 */
    private Date createTime;

    public static class WxMsgInOut {
        static final byte IN = 0;
        static final byte OUT = 1;
    }

    public WxMsg() {}

    public WxMsg(WxMpXmlMessage wxMessage) {
        this.openid = wxMessage.getFromUser();
        this.appid = WxMpConfigStorageHolder.get();
        this.inOut = WxMsgInOut.IN;
        this.msgType = wxMessage.getMsgType();
        this.detail = new JSONObject();
        Long createTime = wxMessage.getCreateTime();
        this.createTime = createTime == null ? new Date() : new Date(createTime * 1000);
        if (WxConsts.XmlMsgType.TEXT.equals(this.msgType)) {
            this.detail.put("content", wxMessage.getContent());
        } else if (WxConsts.XmlMsgType.IMAGE.equals(this.msgType)) {
            this.detail.put("picUrl", wxMessage.getPicUrl());
            this.detail.put("mediaId", wxMessage.getMediaId());
        } else if (WxConsts.XmlMsgType.VOICE.equals(this.msgType)) {
            this.detail.put("format", wxMessage.getFormat());
            this.detail.put("mediaId", wxMessage.getMediaId());
        } else if (WxConsts.XmlMsgType.VIDEO.equals(this.msgType)
                || WxConsts.XmlMsgType.SHORTVIDEO.equals(this.msgType)) {
            this.detail.put("thumbMediaId", wxMessage.getThumbMediaId());
            this.detail.put("mediaId", wxMessage.getMediaId());
        } else if (WxConsts.XmlMsgType.LOCATION.equals(this.msgType)) {
            this.detail.put("locationX", wxMessage.getLocationX());
            this.detail.put("locationY", wxMessage.getLocationY());
            this.detail.put("scale", wxMessage.getScale());
            this.detail.put("label", wxMessage.getLabel());
        } else if (WxConsts.XmlMsgType.LINK.equals(this.msgType)) {
            this.detail.put("title", wxMessage.getTitle());
            this.detail.put("description", wxMessage.getDescription());
            this.detail.put("url", wxMessage.getUrl());
        } else if (WxConsts.XmlMsgType.EVENT.equals(this.msgType)) {
            this.detail.put("event", wxMessage.getEvent());
            this.detail.put("eventKey", wxMessage.getEventKey());
        }
    }

    public static WxMsg buildOutMsg(String msgType, String openid, JSONObject detail) {
        WxMsg wxMsg = new WxMsg();
        wxMsg.appid = WxMpConfigStorageHolder.get();
        wxMsg.msgType = msgType;
        wxMsg.openid = openid;
        wxMsg.detail = detail;
        wxMsg.createTime = new Date();
        wxMsg.inOut = WxMsgInOut.OUT;
        return wxMsg;
    }
}
