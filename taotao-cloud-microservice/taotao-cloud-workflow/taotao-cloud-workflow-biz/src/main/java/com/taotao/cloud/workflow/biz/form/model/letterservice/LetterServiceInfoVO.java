package com.taotao.cloud.workflow.biz.form.model.letterservice;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 发文单
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2021/3/15 8:46
 */
@Data
public class LetterServiceInfoVO {
    @ApiModelProperty(value = "主键id")
    private String id;
    @ApiModelProperty(value = "相关附件")
    private String fileJson;
    @ApiModelProperty(value = "发文字号")
    private String issuedNum;
    @ApiModelProperty(value = "流程标题")
    private String flowTitle;
    @ApiModelProperty(value = "紧急程度")
    private Integer flowUrgent;
    @ApiModelProperty(value = "发文日期")
    private Long  writingDate;
    @ApiModelProperty(value = "主办单位")
    private String hostUnit;
    @ApiModelProperty(value = "抄送")
    private String copy;
    @ApiModelProperty(value = "发文标题")
    private String title;
    @ApiModelProperty(value = "主送")
    private String mainDelivery;
    @ApiModelProperty(value = "流程主键")
    private String flowId;
    @ApiModelProperty(value = "流程单据")
    private String billNo;
    @ApiModelProperty(value = "份数")
    private String shareNum;

}
