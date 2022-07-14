package com.taotao.cloud.workflow.biz.form.model.documentsigning;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 文件签阅表
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2021/3/15 8:46
 */
@Data
public class DocumentSigningInfoVO {
    @ApiModelProperty(value = "主键id")
    private String id;
    @ApiModelProperty(value = "相关附件")
    private String fileJson;
    @ApiModelProperty(value = "文件名称")
    private String fileName;
    @ApiModelProperty(value = "紧急程度")
    private Integer flowUrgent;
    @ApiModelProperty(value = "签阅人")
    private String reader;
    @ApiModelProperty(value = "文件拟办")
    private String fillPreparation;
    @ApiModelProperty(value = "文件内容")
    private String documentContent;
    @ApiModelProperty(value = "签阅时间")
    private Long  checkDate;
    @ApiModelProperty(value = "文件编码")
    private String fillNum;
    @ApiModelProperty(value = "拟稿人")
    private String draftedPerson;
    @ApiModelProperty(value = "流程标题")
    private String flowTitle;
    @ApiModelProperty(value = "流程主键")
    private String flowId;
    @ApiModelProperty(value = "流程单据")
    private String billNo;
    @ApiModelProperty(value = "发稿日期")
    private Long  publicationDate;
    @ApiModelProperty(value = "建议栏")
    private String adviceColumn;
}
