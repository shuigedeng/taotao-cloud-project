package com.taotao.cloud.workflow.biz.form.model.batchtable;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 行文呈批表
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2021/3/15 8:46
 */
@Data
public class BatchTableForm {
    @ApiModelProperty(value = "相关附件")
    private String fileJson;
    @NotNull(message = "紧急程度不能为空")
    @ApiModelProperty(value = "紧急程度")
    private Integer flowUrgent;
    @NotNull(message = "发文日期不能为空")
    @ApiModelProperty(value = "发文日期")
    private Long writingDate;
    @ApiModelProperty(value = "备注")
    private String description;
    @ApiModelProperty(value = "份数")
    private String shareNum;
    @ApiModelProperty(value = "文件编码")
    private String fillNum;
    @ApiModelProperty(value = "主办单位")
    private String draftedPerson;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "流程标题")
    private String flowTitle;
    @ApiModelProperty(value = "文件标题")
    private String fileTitle;
    @ApiModelProperty(value = "发往单位")
    private String sendUnit;
    @ApiModelProperty(value = "打字")
    private String typing;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "流程主键")
    private String flowId;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "流程单据")
    private String billNo;
    @ApiModelProperty(value = "提交/保存 0-1")
    private String status;
    @ApiModelProperty(value = "候选人")
    private Map<String, List<String>> candidateList;
}
