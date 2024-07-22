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

package com.taotao.cloud.sys.api.feign.response.setting;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

/** 经验值设置 */
@Data
public class ExperienceSettingApiResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = -4261856614779031745L;

    /** 注册 */
    private Integer register;

    /** 每日签到经验值 */
    private Integer signIn;

    /** 订单评价赠送经验值 */
    private Integer comment;

    /** 分享获取经验值 */
    private Integer share;

    /** 购物获取经验值,一元*经验值 */
    private BigDecimal money;
}
