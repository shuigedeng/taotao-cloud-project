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

package com.taotao.cloud.store.api.enums;

/** 店铺状态枚举 */
public enum StoreStatusEnum {
    /** 开启中 */
    OPEN("开启中"),
    /** 店铺关闭 */
    CLOSED("店铺关闭"),
    /** 申请开店 */
    APPLY("申请开店,只要完成第一步骤就是申请"),
    /** 审核拒绝 */
    REFUSED("审核拒绝"),
    /** 申请中 */
    APPLYING("申请中，提交审核");

    private final String description;

    StoreStatusEnum(String des) {
        this.description = des;
    }

    public String description() {
        return this.description;
    }

    public String value() {
        return this.name();
    }
}
