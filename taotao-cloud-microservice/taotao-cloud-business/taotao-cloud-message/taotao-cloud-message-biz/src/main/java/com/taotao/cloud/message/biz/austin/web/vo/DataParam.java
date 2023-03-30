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

package com.taotao.cloud.message.biz.austin.web.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 全链路 请求参数
 *
 * @author 3y
 * @date 2022/2/22
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataParam {

    /** 查看用户的链路信息 */
    private String receiver;

    /** 业务Id(数据追踪使用) 生成逻辑参考 TaskInfoUtils 如果传入的是模板ID，则生成当天的业务ID */
    private String businessId;

    /** 日期时间(检索短信的条件使用) */
    private Long dateTime;
}
