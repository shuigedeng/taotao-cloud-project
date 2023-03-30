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

package com.taotao.cloud.distribution.api.enums;

/** 分销员状态 */
public enum DistributionStatusEnum {
    /** 申请中 */
    APPLY("申请中"),
    /** 已清退 */
    RETREAT("已清退"),
    /** 审核拒绝 */
    REFUSE("审核拒绝"),
    /** 审核通过 */
    PASS("审核通过");

    private final String description;

    DistributionStatusEnum(String description) {
        this.description = description;
    }
}
