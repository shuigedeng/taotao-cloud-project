package com.taotao.cloud.workflow.biz.form.model.articleswarehous;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 用品入库申请表
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2021/3/15 8:46
 */
@Data
public class ArticlesWarehousForm {
    @NotNull(message = "必填")
    @ApiModelProperty(value = "紧急程度")
    private Integer flowUrgent;
    @ApiModelProperty(value = "用品分类")
    private String classification;
    @ApiModelProperty(value = "用品编码")
    private String articlesId;
    @ApiModelProperty(value = "申请原因")
    private String applyReasons;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "申请人员")
    private String applyUser;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "流程标题")
    private String flowTitle;
    @ApiModelProperty(value = "数量")
    private String estimatePeople;
    @ApiModelProperty(value = "单位")
    private String company;
    @NotNull(message = "必填")
    @ApiModelProperty(value = "申请时间")
    private Long applyDate;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "所属部门")
    private String department;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "流程主键")
    private String flowId;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "流程单据")
    private String billNo;
    @ApiModelProperty(value = "用品库存")
    private String articles;
    @ApiModelProperty(value = "提交/保存 0-1")
    private String status;
    @ApiModelProperty(value = "候选人")
    private Map<String, List<String>> candidateList;
}
