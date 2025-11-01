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

package com.taotao.cloud.wechat.biz.mp.controller.admin.message.vo.autoreply;

import cn.iocoder.yudao.module.mp.dal.dataobject.message.MpMessageDO;
import cn.iocoder.yudao.module.mp.enums.message.MpAutoReplyTypeEnum;
import cn.iocoder.yudao.module.mp.framework.mp.core.util.MpUtils.*;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.*;
import me.chanjar.weixin.common.api.WxConsts;
import org.hibernate.validator.constraints.URL;

/** 公众号自动回复 Base VO，提供给添加、修改、详细的子 VO 使用 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成 */
@Data
public class MpAutoReplyBaseVO {

    @ApiModelProperty(value = "回复类型", example = "1", notes = "参见 MpAutoReplyTypeEnum 枚举")
    @NotNull(message = "回复类型不能为空")
    private Integer type;

    // ==================== 请求消息 ====================

    @ApiModelProperty(value = "请求的关键字", example = "关键字", notes = "当 type 为 MpAutoReplyTypeEnum#KEYWORD 时，必填")
    private String requestKeyword;

    @ApiModelProperty(value = "请求的匹配方式", example = "1", notes = "当 type 为 MpAutoReplyTypeEnum#KEYWORD 时，必填")
    private Integer requestMatch;

    @ApiModelProperty(value = "请求的消息类型", example = "text", notes = "当 type 为 MpAutoReplyTypeEnum#MESSAGE 时，必填")
    private String requestMessageType;

    // ==================== 响应消息 ====================

    @ApiModelProperty(value = "回复的消息类型", example = "text", notes = "枚举 TEXT、IMAGE、VOICE、VIDEO、NEWS、MUSIC")
    @NotEmpty(message = "回复的消息类型不能为空")
    private String responseMessageType;

    @ApiModelProperty(value = "回复的消息内容", example = "欢迎关注")
    @NotEmpty(message = "回复的消息内容不能为空", groups = TextMessageGroup.class)
    private String responseContent;

    @ApiModelProperty(value = "回复的媒体 id", example = "123456")
    @NotEmpty(
            message = "回复的消息 mediaId 不能为空",
            groups = {ImageMessageGroup.class, VoiceMessageGroup.class, VideoMessageGroup.class})
    private String responseMediaId;

    @ApiModelProperty(value = "回复的媒体 URL", example = "https://www.iocoder.cn/xxx.jpg")
    @NotEmpty(
            message = "回复的消息 mediaId 不能为空",
            groups = {ImageMessageGroup.class, VoiceMessageGroup.class, VideoMessageGroup.class})
    private String responseMediaUrl;

    @ApiModelProperty(value = "缩略图的媒体 id", example = "123456")
    @NotEmpty(
            message = "回复的消息 thumbMediaId 不能为空",
            groups = {MusicMessageGroup.class})
    private String responseThumbMediaId;

    @ApiModelProperty(value = "缩略图的媒体 URL", example = "https://www.iocoder.cn/xxx.jpg")
    @NotEmpty(
            message = "回复的消息 thumbMedia 地址不能为空",
            groups = {MusicMessageGroup.class})
    private String responseThumbMediaUrl;

    @ApiModelProperty(value = "回复的标题", example = "视频标题")
    @NotEmpty(message = "回复的消息标题不能为空", groups = VideoMessageGroup.class)
    private String responseTitle;

    @ApiModelProperty(value = "回复的描述", example = "视频描述")
    @NotEmpty(message = "消息描述不能为空", groups = VideoMessageGroup.class)
    private String responseDescription;

    /**
     * 回复的图文消息
     *
     * <p>消息类型为 {@link WxConsts.XmlMsgType} 的 NEWS
     */
    @NotNull(
            message = "回复的图文消息不能为空",
            groups = {NewsMessageGroup.class, ViewLimitedButtonGroup.class})
    @Valid
    private List<MpMessageDO.Article> responseArticles;

    @ApiModelProperty(value = "回复的音乐链接", example = "https://www.iocoder.cn/xxx.mp3")
    @NotEmpty(message = "回复的音乐链接不能为空", groups = MusicMessageGroup.class)
    @URL(message = "回复的高质量音乐链接格式不正确", groups = MusicMessageGroup.class)
    private String responseMusicUrl;

    @ApiModelProperty(value = "高质量音乐链接", example = "https://www.iocoder.cn/xxx.mp3")
    @NotEmpty(message = "回复的高质量音乐链接不能为空", groups = MusicMessageGroup.class)
    @URL(message = "回复的高质量音乐链接格式不正确", groups = MusicMessageGroup.class)
    private String responseHqMusicUrl;

    @AssertTrue(message = "请求的关键字不能为空")
    public boolean isRequestKeywordValid() {
        return ObjectUtil.notEqual(type, MpAutoReplyTypeEnum.KEYWORD) || requestKeyword != null;
    }

    @AssertTrue(message = "请求的关键字的匹配不能为空")
    public boolean isRequestMatchValid() {
        return ObjectUtil.notEqual(type, MpAutoReplyTypeEnum.KEYWORD) || requestMatch != null;
    }

    @AssertTrue(message = "请求的消息类型不能为空")
    public boolean isRequestMessageTypeValid() {
        return ObjectUtil.notEqual(type, MpAutoReplyTypeEnum.MESSAGE) || requestMessageType != null;
    }
}
