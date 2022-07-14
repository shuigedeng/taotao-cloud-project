package com.taotao.cloud.workflow.biz.form.model.articleswarehous;

import io.swagger.annotations.ApiModelProperty;
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
public class ArticlesWarehousInfoVO {
    @ApiModelProperty(value = "主键id")
    private String id;
    @ApiModelProperty(value = "紧急程度")
    private Integer flowUrgent;
    @ApiModelProperty(value = "用品分类")
    private String classification;
    @ApiModelProperty(value = "用品编码")
    private String articlesId;
    @ApiModelProperty(value = "申请原因")
    private String applyReasons;
    @ApiModelProperty(value = "申请人员")
    private String applyUser;
    @ApiModelProperty(value = "流程标题")
    private String flowTitle;
    @ApiModelProperty(value = "数量")
    private String estimatePeople;
    @ApiModelProperty(value = "单位")
    private String company;
    @ApiModelProperty(value = "申请时间")
    private Long applyDate;
    @ApiModelProperty(value = "所属部门")
    private String department;
    @ApiModelProperty(value = "流程主键")
    private String flowId;
    @ApiModelProperty(value = "流程单据")
    private String billNo;
    @ApiModelProperty(value = "用品库存")
    private String articles;
}
