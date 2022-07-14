package com.taotao.cloud.workflow.biz.form.model.officesupplies;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
public class OfficeSuppliesForm {
    @ApiModelProperty(value = "用品名称")
    private String articlesName;
    @NotNull(message = "紧急程度不能为空")
    @ApiModelProperty(value = "紧急程度")
    private Integer flowUrgent;
    @ApiModelProperty(value = "用品分类")
    private String classification;
    @ApiModelProperty(value = "用品编码")
    private String articlesId;
    @ApiModelProperty(value = "申请原因")
    private String applyReasons;
    @NotBlank(message = "申请人员不能为空")
    @ApiModelProperty(value = "申请人员")
    private String applyUser;
    @NotBlank(message = "流程标题不能为空")
    @ApiModelProperty(value = "流程标题")
    private String flowTitle;
    @ApiModelProperty(value = "领用仓库")
    private String useStock;
    @ApiModelProperty(value = "用品数量")
    private String articlesNum;
    @NotNull(message = "申请时间不能为空")
    @ApiModelProperty(value = "申请时间")
    private Long  applyDate;
    @NotBlank(message = "所属部门不能为空")
    @ApiModelProperty(value = "所属部门")
    private String department;
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
