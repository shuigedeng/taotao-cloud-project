package com.taotao.cloud.promotion.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import lombok.NoArgsConstructor;

/**
 * 秒杀活动时刻视图对象
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SeckillTimelineVO implements Serializable {

    private static final long serialVersionUID = -8171512491016990179L;

    @Schema(description =  "时刻")
    private Integer timeLine;

    @Schema(description =  "秒杀开始时间，这个是时间戳")
    private Long startTime;

    @Schema(description =  "距离本组活动开始的时间，秒为单位。如果活动的开始时间是10点，服务器时间为8点，距离开始还有多少时间")
    private Long distanceStartTime;

    @Schema(description =  "本组活动内的秒杀活动商品列表")
    private List<SeckillGoodsVO> seckillGoodsList;

}
