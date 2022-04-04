package com.taotao.cloud.report.biz.entity.statistics.entity.dos;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 会员统计
 */
@Data
@TableName("li_member_statistics_data")
@ApiModel(value = "会员统计")
public class MemberStatisticsData extends BaseIdEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "统计日")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date createDate;

    @ApiModelProperty(value = "当前会员数量")
    private Long memberCount;

    @ApiModelProperty(value = "新增会员数量")
    private Long newlyAdded;

    @ApiModelProperty(value = "当日活跃数量")
    private Long activeQuantity;


}
