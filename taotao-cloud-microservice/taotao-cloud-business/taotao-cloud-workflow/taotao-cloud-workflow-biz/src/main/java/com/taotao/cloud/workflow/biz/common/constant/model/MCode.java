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

package com.taotao.cloud.workflow.biz.common.constant.model;

/** 类功能 */
public class MCode {

    /** 提示信息类型 */
    private final String type;

    /** 错误编码 */
    private final String code;

    /** description 描述 */
    private final String desc;

    public MCode(String type, String desc) {
        this.type = type;
        this.code = this.getClass().getName();
        this.desc = desc;
    }

    public String get() {
        return desc;
    }

    public String getMsg() {
        return type + ":" + code + " " + desc;
    }
}
