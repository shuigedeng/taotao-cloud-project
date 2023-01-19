package com.taotao.cloud.workflow.biz.common.database.model;

import lombok.Data;

/**
 * 表数据页面对象
 *
 */
@Data
public class DbTableDataForm extends Pagination {
     private String field;
}
