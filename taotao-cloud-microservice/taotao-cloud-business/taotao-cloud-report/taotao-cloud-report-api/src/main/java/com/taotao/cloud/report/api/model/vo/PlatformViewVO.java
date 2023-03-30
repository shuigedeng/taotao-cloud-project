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

package com.taotao.cloud.report.api.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 流量数据展示VO */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlatformViewVO {

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @Schema(description = "展示时间")
    private LocalDateTime date;

    @Schema(description = "pv数量")
    private Long pvNum;

    @Schema(description = "uv数量")
    private Long uvNum;

    @Builder.Default
    @Schema(description = "店铺id")
    private Long storeId = 1L;

    public Long getPvNum() {
        if (pvNum == null) {
            return 0L;
        }
        return pvNum;
    }

    public Long getUvNum() {
        if (uvNum == null) {
            return 0L;
        }
        return uvNum;
    }

    public PlatformViewVO(LocalDateTime date) {
        // 初始化参数
        pvNum = 0L;
        uvNum = 0L;
        this.date = date;
    }
}
