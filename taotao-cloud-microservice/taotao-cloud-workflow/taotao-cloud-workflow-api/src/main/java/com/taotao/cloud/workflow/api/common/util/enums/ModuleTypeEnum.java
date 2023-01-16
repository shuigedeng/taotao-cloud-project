package com.taotao.cloud.workflow.api.common.util.enums;

/**
 * 功能分类枚举
 */
public enum ModuleTypeEnum {
    /**
     * 数据接口类型
     */
    SYSTEM_DATAINTEFASE("bd"),
    /**
     * 单据规则
     */
    SYSTEM_BILLRULE("bb"),
    /**
     * 菜单
     */
    SYSTEM_MODULE("bm"),
    /**
     * 数据建模
     */
    SYSTEM_DBTABLE("bdb"),
    /**
     * 数据字典
     */
    SYSTEM_DICTIONARYDATA("bdd"),
    /**
     * 打印模板
     */
    SYSTEM_PRINT("bp"),
    /**
     * 大屏导出
     */
    VISUAL_DATA("vd"),
    /**
     * 在线开发
     */
    VISUAL_DEV("vdd"),
    /**
     * APP导出
     */
    VISUAL_APP("va"),
    /**
     * 门户导出
     */
    VISUAL_PORTAL("vp"),
    /**
     * 流程设计
     */
    FLOW_FLOWENGINE("ffe"),
    ;

    ModuleTypeEnum(String moduleName) {
        this.tableName = moduleName;
    }

    private String tableName;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
