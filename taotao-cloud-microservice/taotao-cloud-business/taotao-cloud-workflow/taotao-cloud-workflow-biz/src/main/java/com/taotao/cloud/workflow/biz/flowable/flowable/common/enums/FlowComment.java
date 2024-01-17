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

package com.taotao.cloud.workflow.biz.flowable.flowable.common.enums;

/**
 * 流程意见类型
 *
 * @author Tony
 * @since 2021/4/19
 */
public enum FlowComment {

    /** 说明 */
    NORMAL("1", "正常意见"),
    REBACK("2", "退回意见"),
    REJECT("3", "驳回意见"),
    DELEGATE("4", "委派意见"),
    ASSIGN("5", "转办意见"),
    STOP("6", "终止流程");

    /** 类型 */
    private final String type;

    /** 说明 */
    private final String remark;

    FlowComment(String type, String remark) {
        this.type = type;
        this.remark = remark;
    }

    public String getType() {
        return type;
    }

    public String getRemark() {
        return remark;
    }
}
