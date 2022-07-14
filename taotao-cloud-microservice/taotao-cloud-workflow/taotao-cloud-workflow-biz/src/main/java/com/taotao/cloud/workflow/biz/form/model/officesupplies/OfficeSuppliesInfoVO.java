package com.taotao.cloud.workflow.biz.form.model.officesupplies;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 领用办公用品申请表
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2021/3/15 8:46
 */
@Data
public class OfficeSuppliesInfoVO {
    @ApiModelProperty(value = "主键id")
    private String id;
    @ApiModelProperty(value = "用品名称")
    private String articlesName;
    @ApiModelProperty(value = "紧急程度")
    private Integer flowUrgent;
    @ApiModelProperty(value = "用品分类")
    private String classification;
    @ApiModelProperty(value = "用品编码")
    private String articlesId;
    @ApiModelProperty(value = "申请原因")
    private String applyReasons;
    @ApiModelProperty(value = "申请部门")
    private String applyUser;
    @ApiModelProperty(value = "流程标题")
    private String flowTitle;
    @ApiModelProperty(value = "领用仓库")
    private String useStock;
    @ApiModelProperty(value = "用品数量")
    private String articlesNum;
    @ApiModelProperty(value = "申请时间")
    private Long  applyDate;
    @ApiModelProperty(value = "所属部门")
    private String department;
    @ApiModelProperty(value = "流程主键")
    private String flowId;
    @ApiModelProperty(value = "流程单据")
    private String billNo;

}
