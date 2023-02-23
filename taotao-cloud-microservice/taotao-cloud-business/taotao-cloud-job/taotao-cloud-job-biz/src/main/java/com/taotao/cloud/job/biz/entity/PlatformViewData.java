package com.taotao.cloud.job.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.Data;

/**
 * 平台pv统计
 *
 */
@Data
@TableName("tt_s_platform_view_data")
@ApiModel(value = "平台pv统计")
public class PlatformViewData extends BaseIdEntity {


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
