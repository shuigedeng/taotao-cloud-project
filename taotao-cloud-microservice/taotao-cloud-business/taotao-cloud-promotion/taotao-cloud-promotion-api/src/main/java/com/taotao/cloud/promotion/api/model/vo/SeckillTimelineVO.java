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

package com.taotao.cloud.promotion.api.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 秒杀活动时刻视图对象
 *
 * @author shuigedeng
 * @version 2023.04
 * @since 2023-05-10 14:04:24
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SeckillTimelineVO implements Serializable {

    @Serial
    private static final long serialVersionUID = -8171512491016990179L;

    @Schema(description = "时刻")
    private Integer timeLine;

    @Schema(description = "秒杀开始时间，这个是时间戳")
    private Long startTime;

    @Schema(description = "距离本组活动开始的时间，秒为单位。如果活动的开始时间是10点，服务器时间为8点，距离开始还有多少时间")
    private Long distanceStartTime;

    @Schema(description = "本组活动内的秒杀活动商品列表")
    private List<SeckillGoodsVO> seckillGoodsList;
}
