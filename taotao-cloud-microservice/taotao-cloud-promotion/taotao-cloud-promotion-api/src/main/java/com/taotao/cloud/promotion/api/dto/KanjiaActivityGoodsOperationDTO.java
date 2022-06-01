package com.taotao.cloud.promotion.api.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import lombok.NoArgsConstructor;

/**
 * 砍价活动商品操作DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KanjiaActivityGoodsOperationDTO implements Serializable {

    @Serial
	private static final long serialVersionUID = -1378599087650538592L;

    @Min(message = "活动开始时间不能为空", value = 0)
    @Schema(description =  "活动开始时间", required = true)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @Min(message = "活动结束时间不能为空", value = 0)
    @Schema(description =  "活动结束时间", required = true)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    @Schema(description =  "砍价活动商品列表")
    List<KanjiaActivityGoodsDTO> promotionGoodsList;

}
