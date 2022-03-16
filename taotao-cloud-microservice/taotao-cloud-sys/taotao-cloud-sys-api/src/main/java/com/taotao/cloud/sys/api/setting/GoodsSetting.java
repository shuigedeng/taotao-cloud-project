package com.taotao.cloud.sys.api.setting;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 商品设置
 *
 */
@Data
public class GoodsSetting implements Serializable {

    private static final long serialVersionUID = -4132785717179910025L;
    @Schema(description =  "是否开启商品审核")
    private Boolean goodsCheck;

    @Schema(description =  "小图宽")
    private Integer smallPictureWidth;

    @Schema(description =  "小图高")
    private Integer smallPictureHeight;

    @Schema(description =  "缩略图宽")
    private Integer abbreviationPictureWidth;

    @Schema(description =  "缩略图高")
    private Integer abbreviationPictureHeight;

    @Schema(description =  "原图宽")
    private Integer originalPictureWidth;

    @Schema(description =  "原图高")
    private Integer originalPictureHeight;

}
