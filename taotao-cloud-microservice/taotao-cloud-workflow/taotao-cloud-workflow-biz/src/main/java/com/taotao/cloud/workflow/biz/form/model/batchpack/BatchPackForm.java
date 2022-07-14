package com.taotao.cloud.workflow.biz.form.model.batchpack;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 批包装指令
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2021/3/15 8:46
 */
@Data
public class BatchPackForm {
    @ApiModelProperty(value = "产品规格")
    private String standard;
    @ApiModelProperty(value = "操作日期")
    private Long operationDate;
    @NotNull(message = "必填")
    @ApiModelProperty(value = "紧急程度")
    private Integer flowUrgent;
    @ApiModelProperty(value = "编制人员")
    private String compactor;
    @ApiModelProperty(value = "生产车间")
    private String production;
    @ApiModelProperty(value = "备注")
    private String description;
    @ApiModelProperty(value = "包装规格")
    private String packing;
    @NotNull(message = "必填")
    @ApiModelProperty(value = "编制日期")
    private Long compactorDate;
    @ApiModelProperty(value = "产品名称")
    private String productName;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "流程标题")
    private String flowTitle;
    @ApiModelProperty(value = "工艺规程")
    private String regulations;
    @ApiModelProperty(value = "批产数量")
    private String productionQuty;
    @ApiModelProperty(value = "入库序号")
    private String warehousNo;
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
