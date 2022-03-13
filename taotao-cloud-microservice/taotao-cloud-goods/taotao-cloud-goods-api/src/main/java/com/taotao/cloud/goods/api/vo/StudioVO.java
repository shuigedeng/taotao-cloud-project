package com.taotao.cloud.goods.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Data;

/**
 * 直播间VO
 */
@Data
public class StudioVO extends Studio {

    @Schema(description = "直播间商品列表")
    private List<Commodity> commodityList;

}
