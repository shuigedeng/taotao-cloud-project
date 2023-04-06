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

package com.taotao.cloud.message.api.enums;

/** 消息编码枚举 */
public enum NoticeMessageParameterEnum {

    /** 商品名称 */
    GOODS("goods", "商品名称"),
    /** 消费积分 */
    EXPENDITURE_POINTS("expenditure_points", "消费积分"),
    /** 获得积分 */
    INCOME_POINTS("income_points", "获得积分"),
    /** 支出金额 */
    EXPENDITURE("expenditure", "支出金额"),
    /** 收入金额 */
    INCOME("income", "收入金额"),
    /** 拒绝原因 */
    REFUSE("refuse", "拒绝原因"),
    /** 取消原因 */
    CANCEL_REASON("cancel_reason", "取消原因"),
    /** 取消原因 */
    PRICE("price", "金额");

    private final String type;
    private final String description;

    NoticeMessageParameterEnum(String type, String description) {
        this.type = type;
        this.description = description;
    }

    /**
     * 根据type获取去value
     *
     * @param type
     * @return
     */
    public static String getValueByType(String type) {
        for (NoticeMessageParameterEnum noticeMessageParameterEnum : NoticeMessageParameterEnum.values()) {
            if (type.toLowerCase().equals(noticeMessageParameterEnum.getType().toLowerCase())) {
                return noticeMessageParameterEnum.getDescription();
            }
        }
        return null;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }
}
