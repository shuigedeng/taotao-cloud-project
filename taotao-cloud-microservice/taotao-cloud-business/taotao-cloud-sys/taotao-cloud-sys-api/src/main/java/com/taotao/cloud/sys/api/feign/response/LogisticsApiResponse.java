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

package com.taotao.cloud.sys.api.feign.response;

import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * LogisticsVO
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2021/12/20 14:06
 */
@Data
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class LogisticsApiResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = -4132785717179910025L;

    private Long id;

    /** 物流公司名称 */
    private String name;

    /** 物流公司code */
    private String code;

    /** 物流公司联系人 */
    private String contactName;

    /** 物流公司联系电话 */
    private String contactMobile;

    /** 支持电子面单 */
    private String standBy;

    /** 物流公司电子面单表单 */
    private String formItems;

    /** 禁用状态 OPEN：开启，CLOSE：禁用 */
    private String disabled;
}
