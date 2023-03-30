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

package com.taotao.cloud.goods.api.enums;

/**
 * 直播间状态
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:32:28
 */
public enum StudioStatusEnum {

    /** 新建 */
    NEW("新建"),
    /** 开始 */
    START("开始"),
    /** 结束 */
    END("结束");

    private final String clientName;

    StudioStatusEnum(String des) {
        this.clientName = des;
    }

    public String clientName() {
        return this.clientName;
    }
}
