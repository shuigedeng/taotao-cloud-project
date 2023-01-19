package com.taotao.cloud.workflow.biz.common.database.enums;

/**
 * 数据库 结构、参数 替换枚举 structure
 *
 */
public enum ParamEnum {

    /**
     * 数据库
     */
    DB_URL("{dbUrl}"),
    /**
     * 数据库名
     */
    DB_NAME("{dbName}"),
    /**
     * 用户名
     */
    USER_NAME("{userName}"),
    /**
     * 模式
     * schema关键字,加前缀
     */
    DB_SCHEMA("{dbSchema}"),
    /**
     * 表空间
     */
    TABLE_SPACE("{tableSpace}"),
    /**
     * 表
     */
    TABLE("{table}"),
    /**
     * 替换符
     */
    SPLIT("split"),
    /**
     * 字段名
     */
    FILED("{filed}"),
    /**
     * 字段长度
     */
    DATA_LENGTH("{dataLength}"),
    /**
     * 字段注释
     */
    COMMENT("{comment}")
    ;

    /**
     * 替换目标
     */
    private final String target;

    public String getTarget(){
        return this.target;
    }

    public String getParamSign(){
        return "?";
    }

    ParamEnum(String target){
        this.target = target;
    }


}
