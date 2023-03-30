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

/** 经办对象 */
public enum FlowTaskOperatorEnum {

    // 发起者主管
    LaunchCharge("1", "发起者主管"),
    // 部门经理
    DepartmentCharge("2", "部门经理"),
    // 发起者本人
    InitiatorMe("3", "发起者本人"),
    // 变量
    Variate("4", "变量"),
    // 环节
    Tache("5", "环节"),
    // 指定人
    Nominator("6", "指定人"),
    // 候选人
    FreeApprover("7", "候选人"),
    // 服务
    Serve("9", "服务");

    private String code;
    private String message;

    FlowTaskOperatorEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
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
    public static String getMessageByCode(String code) {
        for (FlowTaskOperatorEnum status : FlowTaskOperatorEnum.values()) {
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
    public static FlowTaskOperatorEnum getByCode(String code) {
        for (FlowTaskOperatorEnum status : FlowTaskOperatorEnum.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}
