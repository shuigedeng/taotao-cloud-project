package com.taotao.cloud.workflow.biz.common.base.vo;

import lombok.Data;

/**
 * 数据接口弹窗选择
 *
 */
@Data
public class DataInterfacePageListVO<T> extends PageListVO {
    private String dataProcessing;
}
