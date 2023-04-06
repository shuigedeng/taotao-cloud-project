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

package com.taotao.cloud.im.biz.platform.modules.chat.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/** 消息状态 */
@Getter
public enum MsgStatusEnum {

    /** 正常 */
    NORMAL("0", "正常"),
    /** 对方不是自己朋友 */
    FRIEND_TO("1", "对方不是你的好友，消息发送失败"),
    /** 自己不是对方朋友 */
    FRIEND_FROM("2", "你不是对方的好友，消息发送失败"),
    /** 黑名单 */
    FRIEND_BLACK("3", "消息已发出，但被对方拒收了"),
    /** 注销 */
    FRIEND_DELETED("4", "对方已注销，消息发送失败"),
    /** 群不存在 */
    GROUP_NOT_EXIST("5", "当前群不存在，消息发送失败"),
    /** 群明细不存在 */
    GROUP_INFO_NOT_EXIST("6", "你不在当前群中，消息发送失败"),
    ;

    @EnumValue
    @JsonValue
    private String code;

    private String info;

    MsgStatusEnum(String code, String info) {
        this.code = code;
        this.info = info;
    }
}
