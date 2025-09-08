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

package com.taotao.cloud.report.biz.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.Data;
import lombok.experimental.*;

/** 平台pv统计 */
@Data
@TableName("tt_s_platform_view_data")
public class PlatformViewData {

    @Schema(description = "pv数量")
    private Long pvNum;

    @Schema(description = "uv数量")
    private Long uvNum;

    @Schema(description = "统计日")
    private Date date;

    // 默认是平台流量统计//

    @Schema(description = "店铺id")
    private String storeId = "-1";
}
