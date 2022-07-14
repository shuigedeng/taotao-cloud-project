package com.taotao.cloud.workflow.biz.form.model.postbatchtab;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 发文呈批表
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2021/3/15 8:46
 */
@Data
public class PostBatchTabInfoVO {
    @ApiModelProperty(value = "主键id")
    private String id;
    private String draftedPerson;
    @ApiModelProperty(value = "相关附件")
    private String fileJson;
    @ApiModelProperty(value = "流程标题")
    private String flowTitle;
    @ApiModelProperty(value = "紧急程度")
    private Integer flowUrgent;
    @ApiModelProperty(value = "发文日期")
    private Long writingDate;
    @ApiModelProperty(value = "文件标题")
    private String fileTitle;
    @ApiModelProperty(value = "发往单位")
    private String sendUnit;
    @ApiModelProperty(value = "备注")
    private String description;
    @ApiModelProperty(value = "发文编码")
    private String writingNum;
    @ApiModelProperty(value = "流程主键")
    private String flowId;
    @ApiModelProperty(value = "流程单据")
    private String billNo;
    @ApiModelProperty(value = "份数")
    private String shareNum;
}
