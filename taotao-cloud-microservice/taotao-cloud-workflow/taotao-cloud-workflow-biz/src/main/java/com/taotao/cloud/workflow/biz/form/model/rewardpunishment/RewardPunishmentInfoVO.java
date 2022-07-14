package com.taotao.cloud.workflow.biz.form.model.rewardpunishment;

import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import lombok.Data;

/**
 * 行政赏罚单
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2021/3/15 8:46
 */
@Data
public class RewardPunishmentInfoVO {
    @ApiModelProperty(value = "主键id")
    private String id;
    @ApiModelProperty(value = "赏罚原因")
    private String reason;
    @ApiModelProperty(value = "流程标题")
    private String flowTitle;
    @ApiModelProperty(value = "紧急程度")
    private Integer flowUrgent;
    @ApiModelProperty(value = "员工姓名")
    private String fullName;
    @ApiModelProperty(value = "填表日期")
    private Long fillFromDate;
    @ApiModelProperty(value = "赏罚金额")
    private BigDecimal rewardPun;
    @ApiModelProperty(value = "员工职位")
    private String position;
    @ApiModelProperty(value = "员工部门")
    private String department;
    @ApiModelProperty(value = "流程主键")
    private String flowId;
    @ApiModelProperty(value = "流程单据")
    private String billNo;
}
