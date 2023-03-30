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

package com.taotao.cloud.stock.api.common.util;

/** 公共常量 */
public interface CommonConstant {

    /** 多租户 请求头 */
    String TENANT_ID = "tenant_id";

    /** 当前页码 */
    String PAGE = "page";

    /** 每页显示记录数 */
    String LIMIT = "limit";

    /** 排序字段 */
    String ORDER_FIELD = "sidx";

    /** 排序方式 */
    String ORDER = "order";

    /** 升序 */
    String ASC = "asc";

    /** 手机验证码 */
    String REDIS_PHONE_CODE = "PC";
}
