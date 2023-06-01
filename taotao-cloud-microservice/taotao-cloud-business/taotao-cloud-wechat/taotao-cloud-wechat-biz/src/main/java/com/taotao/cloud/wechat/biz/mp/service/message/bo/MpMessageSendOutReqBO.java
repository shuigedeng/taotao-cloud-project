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

package com.taotao.cloud.wechat.biz.mp.service.message.bo;

import cn.iocoder.yudao.module.mp.dal.dataobject.message.MpMessageDO;
import cn.iocoder.yudao.module.mp.framework.mp.core.util.MpUtils.*;
import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import me.chanjar.weixin.common.api.WxConsts;
import org.hibernate.validator.constraints.URL;

/**
 * 公众号消息发送 Request BO
 *
 * <p>为什么要有该 BO 呢？在自动回复、客服消息、菜单回复消息等场景，都涉及到 MP 给粉丝发送消息，所以使用该 BO 统一承接
 *
 * @author 芋道源码
 */
@Data
public class MpMessageSendOutReqBO {

    /** 公众号 appId */
    @NotEmpty(message = "公众号 appId 不能为空")
    private String appId;
    /** 公众号粉丝 openid */
    @NotEmpty(message = "公众号粉丝 openid 不能为空")
    private String openid;

    // ========== 消息内容 ==========
    /**
     * 消息类型
     *
     * <p>枚举 {@link WxConsts.XmlMsgType} 中的 TEXT、IMAGE、VOICE、VIDEO、NEWS、MUSIC
     */
    @NotEmpty(message = "消息类型不能为空")
    public String type;

    /**
     * 消息内容
     *
     * <p>消息类型为 {@link WxConsts.XmlMsgType} 的 TEXT
     */
    @NotEmpty(message = "消息内容不能为空", groups = TextMessageGroup.class)
    private String content;

    /**
     * 媒体 id
     *
     * <p>消息类型为 {@link WxConsts.XmlMsgType} 的 IMAGE、VOICE、VIDEO
     */
    @NotEmpty(
            message = "消息 mediaId 不能为空",
            groups = {ImageMessageGroup.class, VoiceMessageGroup.class, VideoMessageGroup.class})
    private String mediaId;

    /**
     * 缩略图的媒体 id
     *
     * <p>消息类型为 {@link WxConsts.XmlMsgType} 的 VIDEO、MUSIC
     */
    @NotEmpty(
            message = "消息 thumbMediaId 不能为空",
            groups = {MusicMessageGroup.class})
    private String thumbMediaId;

    /**
     * 标题
     *
     * <p>消息类型为 {@link WxConsts.XmlMsgType} 的 VIDEO
     */
    @NotEmpty(message = "消息标题不能为空", groups = VideoMessageGroup.class)
    private String title;
    /**
     * 描述
     *
     * <p>消息类型为 {@link WxConsts.XmlMsgType} 的 VIDEO
     */
    @NotEmpty(message = "消息描述不能为空", groups = VideoMessageGroup.class)
    private String description;

    /**
     * 图文消息
     *
     * <p>消息类型为 {@link WxConsts.XmlMsgType} 的 NEWS
     */
    @Valid
    @NotNull(message = "图文消息不能为空", groups = NewsMessageGroup.class)
    private List<MpMessageDO.Article> articles;

    /**
     * 音乐链接
     *
     * <p>消息类型为 {@link WxConsts.XmlMsgType} 的 MUSIC
     */
    @NotEmpty(message = "音乐链接不能为空", groups = MusicMessageGroup.class)
    @URL(message = "高质量音乐链接格式不正确", groups = MusicMessageGroup.class)
    private String musicUrl;

    /**
     * 高质量音乐链接
     *
     * <p>消息类型为 {@link WxConsts.XmlMsgType} 的 MUSIC
     */
    @NotEmpty(message = "高质量音乐链接不能为空", groups = MusicMessageGroup.class)
    @URL(message = "高质量音乐链接格式不正确", groups = MusicMessageGroup.class)
    private String hqMusicUrl;
}
