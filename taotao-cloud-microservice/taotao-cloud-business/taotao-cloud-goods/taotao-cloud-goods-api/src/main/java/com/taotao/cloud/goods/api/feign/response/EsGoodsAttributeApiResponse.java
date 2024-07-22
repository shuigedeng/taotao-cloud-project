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

package com.taotao.cloud.goods.api.feign.response;

import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品属性索引
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:18:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EsGoodsAttributeApiResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 4018042777559970062L;

    /** 属性参数：0->规格；1->参数 */
    private Integer type;

    /** 属性名称 */
    private String nameId;

    /** 属性名称 */
    private String name;

    /** 属性值 */
    private String valueId;

    /** 属性值 */
    private String value;

    /** 排序 */
    private Integer sort;
}
