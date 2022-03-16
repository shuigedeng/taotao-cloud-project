package com.taotao.cloud.promotion.api.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


/**
 * 砍价活动参与实体类
 */
@Data
@Schema(description = "砍价活动参与记录对象")
//public class KanjiaActivityDTO extends KanjiaActivityLog {
public class KanjiaActivityDTO  {

    @Schema(description =  "砍价商品Id")
    private String kanjiaActivityGoodsId;

}
