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

package com.taotao.cloud.payment.biz.jeepay.pay.rqrs;

import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import lombok.Data;

/*
 * 基础请求参数
 *
 * @author terrfly
 * @site https://www.jeequan.com
 * @date 2021/6/8 17:39
 */
@Data
public abstract class AbstractRQ implements Serializable {

    /** 版本号 * */
    @NotBlank(message = "版本号不能为空")
    protected String version;

    /** 签名类型 * */
    @NotBlank(message = "签名类型不能为空")
    protected String signType;

    /** 签名值 * */
    @NotBlank(message = "签名值不能为空")
    protected String sign;

    /** 接口请求时间 * */
    @NotBlank(message = "时间戳不能为空")
    protected String reqTime;
}
