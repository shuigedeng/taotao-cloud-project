package com.taotao.cloud.workflow.biz.form.model.violationhandling;

import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 违章处理申请表
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2021/3/15 8:46
 */
@Data
public class ViolationHandlingForm {
    @NotNull(message = "紧急程度不能为空")
    @ApiModelProperty(value = "紧急程度")
    private Integer flowUrgent;
    @ApiModelProperty(value = "违章地点")
    private String violationSite;
    @ApiModelProperty(value = "备注")
    private String description;
    @ApiModelProperty(value = "车牌号")
    private String plateNum;
    @ApiModelProperty(value = "负责人")
    private String leadingOfficial;
    @NotNull(message = "违章日期不能为空")
    @ApiModelProperty(value = "违章日期")
    private Long  peccancyDate;
    @NotBlank(message = "流程标题不能为空")
    @ApiModelProperty(value = "流程标题")
    private String flowTitle;
    @ApiModelProperty(value = "驾驶人")
    private String driver;
    @ApiModelProperty(value = "违章扣分")
    private String deduction;
    @NotNull(message = "通知日期不能为空")
    @ApiModelProperty(value = "通知日期")
    private Long  noticeDate;
    @NotNull(message = "限处理日期不能为空")
    @ApiModelProperty(value = "限处理日期")
    private Long  limitDate;
    @ApiModelProperty(value = "违章行为")
    private String violationBehavior;
    @ApiModelProperty(value = "违章罚款")
    private BigDecimal amountMoney;
    @NotBlank(message = "流程主键不能为空")
    @ApiModelProperty(value = "流程主键")
    private String flowId;
    @NotBlank(message = "流程单据不能为空")
    @ApiModelProperty(value = "流程单据")
    private String billNo;
    @ApiModelProperty(value = "提交/保存 0-1")
    private String status;
    @ApiModelProperty(value = "候选人")
    private Map<String, List<String>> candidateList;

}
