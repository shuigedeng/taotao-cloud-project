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

package com.taotao.cloud.order.biz.model.page.order;

import com.taotao.boot.common.enums.UserEnum;
import com.taotao.boot.common.model.request.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import lombok.*;
import lombok.Data;
import lombok.experimental.*;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 订单查询参数
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-21 16:59:38
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain=true)
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "订单查询参数")
public class OrderLogPageQuery extends PageQuery {

    @Serial
    private static final long serialVersionUID = -6380573339089959194L;

    /** 订单编号 */
    private String orderSn;

    /** 操作者id(可以是卖家) */
    private Long operatorId;

    /**
     * 操作者类型
     *
     * @see UserEnum
     */
    private String operatorType;

    /** 操作者名称 */
    private String operatorName;
}
