package com.taotao.cloud.im.biz.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 平台pv统计
 *
 */
@Data
@TableName("tt_s_platform_view_data")
public class PlatformViewData {


    @Schema(description =  "pv数量")
    private Long pvNum;

    @Schema(description =  "uv数量")
    private Long uvNum;


    @Schema(description =  "统计日")
    private Date date;

    //默认是平台流量统计//

    @Schema(description =  "店铺id")
    private String storeId = "-1";
}
