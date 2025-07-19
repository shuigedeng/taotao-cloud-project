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

package com.taotao.cloud.realtime.datalake.mall.bean;

import com.taotao.cloud.realtime.mall.bean.OrderWide;
import java.math.BigDecimal;

/**
 *
 * Date: 2021/2/24
 * Desc:地区统计宽表实体类
 */
public class ProvinceStats {

    private String stt;
    private String edt;
    private Long province_id;
    private String province_name;
    private String area_code;
    private String iso_code;
    private String iso_3166_2;
    private BigDecimal order_amount;
    private Long order_count;
    private Long ts;

    public ProvinceStats(OrderWide orderWide) {
        province_id = orderWide.getProvince_id();
        order_amount = orderWide.getSplit_total_amount();
        province_name = orderWide.getProvince_name();
        area_code = orderWide.getProvince_area_code();
        iso_3166_2 = orderWide.getProvince_iso_code();
        iso_code = orderWide.getProvince_iso_code();

        order_count = 1L;
        ts = System.currentTimeMillis();
    }
}
