package com.taotao.cloud.workflow.biz.model;

import java.util.List;
import lombok.Data;

/**
 * 解析引擎
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2021/3/15 9:19
 */
@Data
public class FormColumnTableModel {

    /**json原始名称**/
    private String tableModel;
    /**表名称**/
    private String tableName;
    /**标题**/
    private String label;
    /**宽度**/
    private Integer span;
    /**是否显示标题**/
    private boolean showTitle;
    /**按钮名称**/
    private String actionText;
    /**子表的属性**/
    private List<FormColumnModel> childList;
    /**app子表属性**/
    private String fieLdsModel;

    /**
     * 子表是否合计
     */
    private Boolean showSummary;

    /**
     * 子表合计字段
     */
    private String summaryField;

    /**
     * app子表合计名称
     */
    private String summaryFieldName;
}
