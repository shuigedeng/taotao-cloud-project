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

package com.taotao.cloud.message.biz.austin.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 打点信息枚举
 *
 * @author 3y
 */
@Getter
@ToString
@AllArgsConstructor
public enum AnchorState {

    /** 消息接收成功（获取到请求） */
    RECEIVE(10, "消息接收成功"),
    /** 消息被丢弃（从Kafka消费后，被丢弃） */
    DISCARD(20, "消费被丢弃"),
    /** 消息被夜间屏蔽（模板设置了夜间屏蔽） */
    NIGHT_SHIELD(22, "夜间屏蔽"),
    /** 消息被夜间屏蔽（模板设置了夜间屏蔽，次日9点再发送） */
    NIGHT_SHIELD_NEXT_SEND(24, "夜间屏蔽(次日早上9点发送)"),

    /** 消息被内容去重（重复内容5min内多次发送） */
    CONTENT_DEDUPLICATION(30, "消息被内容去重"),
    /** 消息被频次去重（同一个渠道短时间内发送多次消息给用户） */
    RULE_DEDUPLICATION(40, "消息被频次去重"),
    /** 白名单过滤（非正式环境，不在白名单内） */
    WHITE_LIST(50, "白名单过滤"),
    /** 下发成功（调用渠道接口成功） */
    SEND_SUCCESS(60, "消息下发成功"),
    /** 下发失败（调用渠道接口失败） */
    SEND_FAIL(70, "消息下发失败"),

    /** 点击（下发的消息被点击） */
    CLICK(0100, "消息被点击"),
    ;

    private final Integer code;
    private final String description;

    /**
     * 通过code获取描述
     *
     * @param code
     * @return
     */
    public static String getDescriptionByCode(Integer code) {
        for (AnchorState anchorState : AnchorState.values()) {
            if (anchorState.getCode().equals(code)) {
                return anchorState.getDescription();
            }
        }
        return "未知点位";
    }
}
