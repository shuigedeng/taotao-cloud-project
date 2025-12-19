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

package com.taotao.cloud.realtime.warehouse.datageneration.userlog_code.model;

import lombok.Data;

/**
 * Common
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
@Data
public class Common {

    private String ar; // 地区
    private String ba; // 品牌
    private String ch; // 渠道
    private String is_new; // 是否新用户
    private String md; // 机型
    private String mid; // 设备id
    private String os; // 操作系统
    private String uid; // 用户id
    private String vc; // 版本号
}
