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

package com.taotao.cloud.promotion.api.model.page;

import com.taotao.boot.common.model.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;
import java.io.Serializable;

import lombok.*;
import lombok.experimental.*;

/**
 * 砍价活动参与实体类
 *
 * @author shuigedeng
 * @version 2023.07
 * @since 2023-07-04 09:43:47
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain=true)
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "砍价活动参与记录查询对象")
public class KanJiaActivityLogPageQuery extends PageQuery implements Serializable {

    @Serial
	private static final long serialVersionUID = -1583030890805926292L;

    @Schema(description = "砍价发起活动id")
    private String kanJiaActivityId;

    // public <T> QueryWrapper<T> wrapper() {
    //     QueryWrapper<T> queryWrapper = new QueryWrapper<>();
    //
    //     if (CharSequenceUtil.isNotEmpty(kanJiaActivityId)) {
    //         queryWrapper.like("kanjia_activity_id", kanJiaActivityId);
    //     }
    //     queryWrapper.eq("delete_flag", false);
    //     queryWrapper.orderByDesc("create_time");
    //     return queryWrapper;
    // }
}
