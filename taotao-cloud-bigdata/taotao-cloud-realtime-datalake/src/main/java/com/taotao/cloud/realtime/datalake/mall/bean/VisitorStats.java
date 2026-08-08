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

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * Date: 2021/2/22
 * Desc: 访客统计实体类
 *  在当前类中定义类访客统计相关的维度和度量
 */
@Data
@AllArgsConstructor
public class VisitorStats {
    // 统计开始时间
    private String stt;
    // 统计结束时间
    private String edt;
    // 维度：版本
    private String vc;
    // 维度：渠道
    private String ch;
    // 维度：地区
    private String ar;
    // 维度：新老用户标识
    private String is_new;
    // 度量：独立访客数
    private Long uv_ct = 0L;
    // 度量：页面访问数
    private Long pv_ct = 0L;
    // 度量： 进入次数 (session_count)
    private Long sv_ct = 0L;
    // 度量： 跳出次数
    private Long uj_ct = 0L;
    // 度量： 持续访问时间
    private Long dur_sum = 0L;
    // 统计时间
    private Long ts;
}
