package com.taotao.cloud.workflow.biz.form.model.outboundorder;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 出库单
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2021/3/15 8:46
 */
@Data
public class OutboundOrderForm {
    @NotBlank(message = "流程主键不能为空")
    @ApiModelProperty(value = "流程主键")
    private String flowId;
    @NotBlank(message = "流程标题不能为空")
    @ApiModelProperty(value = "流程标题")
    private String flowTitle;
    @NotNull(message = "紧急程度不能为空")
    @ApiModelProperty(value = "紧急程度")
    private Integer flowUrgent;
    @NotBlank(message = "流程单据不能为空")
    @ApiModelProperty(value = "流程单据")
    private String billNo;
    @ApiModelProperty(value = "客户名称")
    private String customerName;
    @ApiModelProperty(value = "仓库")
    private String warehouse;
    @ApiModelProperty(value = "仓库人")
    private String outStorage;
    @ApiModelProperty(value = "业务人员")
    private String businessPeople;
    @ApiModelProperty(value = "业务类型")
    private String businessType;
    @NotNull(message = "出库日期不能为空")
    @ApiModelProperty(value = "出库日期")
    private Long outboundDate;
    @ApiModelProperty(value = "备注")
    private String description;
    @ApiModelProperty(value = "提交/保存 0-1")
    private String status;
    @ApiModelProperty(value = "明细")
    List<OutboundEntryEntityInfoModel> entryList;
    @ApiModelProperty(value = "候选人")
    private Map<String, List<String>> candidateList;
}
