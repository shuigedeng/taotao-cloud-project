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

package com.taotao.cloud.workflow.biz.common.flowable.model.query;

import com.taotao.boot.common.model.request.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import lombok.*;
import lombok.Data;
import lombok.experimental.*;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 店铺搜索参数VO
 *
 * @since 2020-03-07 17:02:05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain=true)
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "店铺搜索参数VO")
public class StorePageQuery extends PageQuery {

    @Serial
    private static final long serialVersionUID = 6916054310764833369L;

    @Schema(description = "会员名称")
    private String memberName;

    @Schema(description = "店铺名称")
    private String storeName;
    /**
     * @see StoreStatusEnum
     */
    @Schema(description = "店铺状态")
    private String storeDisable;

    @Schema(description = "开始时间")
    private String startDate;

    @Schema(description = "结束时间")
    private String endDate;
}
