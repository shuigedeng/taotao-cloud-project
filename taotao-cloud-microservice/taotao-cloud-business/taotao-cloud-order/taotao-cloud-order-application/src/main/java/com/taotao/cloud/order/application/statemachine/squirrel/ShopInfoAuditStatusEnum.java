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

package com.taotao.cloud.order.application.statemachine.squirrel;
// 店铺审核状态
public enum ShopInfoAuditStatusEnum {
    audit(0, "待审核"),
    agree(1, "审核通过"),
    reject(2, "审核驳回");

    private Integer code;
    private String desc;

    ShopInfoAuditStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
