package com.taotao.cloud.workflow.biz.engine.model.flowengine;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import jnpf.base.Pagination;
import lombok.Data;

/**
 *
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2021/3/15 9:17
 */
@Data
public class PaginationFlowEngine extends Pagination {
    private Integer formType;
    private Integer enabledMark;
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private Integer type;
}
