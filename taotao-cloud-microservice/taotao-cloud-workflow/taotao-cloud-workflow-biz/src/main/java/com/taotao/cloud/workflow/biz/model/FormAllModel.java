package com.taotao.cloud.workflow.biz.model;

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
public class FormAllModel {
    /**所有模板的标签 row(栅格)、card(卡片)、table(子表)、mast(主表)、mastTable(主表)、groupTitle(分组标题)**/
    private String jnpfKey;
    /**是否是结束标签 0.不是 1.是**/
    private String isEnd = "0";
    /**主表数据**/
    private FormColumnModel formColumnModel;
    /**子表的数据**/
    private FormColumnTableModel childList;
    /**栅格和卡片等数据**/
    private FormModel formModel;
    /**主表中有子表数据**/
    private FormMastTableModel formMastTableModel;
}
