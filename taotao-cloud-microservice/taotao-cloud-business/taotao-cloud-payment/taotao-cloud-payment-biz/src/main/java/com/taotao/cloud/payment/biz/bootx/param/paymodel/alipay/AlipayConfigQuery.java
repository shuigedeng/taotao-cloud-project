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

package com.taotao.cloud.payment.biz.bootx.param.paymodel.alipay;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author xxm
 * @date 2021/7/22
 */
@Data
@Accessors(chain = true)
@Schema(title = "支付宝配置搜索参数")
public class AlipayConfigQuery implements Serializable {
    private static final long serialVersionUID = -173325268481050362L;

    /** 名称 */
    private String name;

    /** 状态 */
    private Integer state;

    /** 支付宝商户appId */
    private String appId;
}
