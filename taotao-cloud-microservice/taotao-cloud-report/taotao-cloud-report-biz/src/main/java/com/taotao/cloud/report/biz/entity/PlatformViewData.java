package com.taotao.cloud.search.biz.entity;

import cn.lili.mybatis.BaseIdEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 平台pv统计
 *
 */
@Data
@TableName("li_s_platform_view_data")
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
