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

package com.taotao.cloud.workflow.biz.engine.enums;

/** 流程状态 */
public enum FlowTaskStatusEnum {
    // 等待提交
    Draft(0, "等待提交"),
    // 等待审核
    Handle(1, "等待审核"),
    // 审核通过
    Adopt(2, "审核通过"),
    // 审核驳回
    Reject(3, "审核驳回"),
    // 审核撤销
    Revoke(4, "审核撤销"),
    // 审核作废
    Cancel(5, "审核作废");

    private int code;
    private String message;

    FlowTaskStatusEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 根据状态code获取枚举名称
     *
     * @return
     */
    public static String getMessageByCode(Integer code) {
        for (FlowTaskStatusEnum status : FlowTaskStatusEnum.values()) {
            if (status.getCode().equals(code)) {
                return status.message;
            }
        }
        return null;
    }

    /**
     * 根据状态code获取枚举值
     *
     * @return
     */
    public static FlowTaskStatusEnum getByCode(Integer code) {
        for (FlowTaskStatusEnum status : FlowTaskStatusEnum.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}
