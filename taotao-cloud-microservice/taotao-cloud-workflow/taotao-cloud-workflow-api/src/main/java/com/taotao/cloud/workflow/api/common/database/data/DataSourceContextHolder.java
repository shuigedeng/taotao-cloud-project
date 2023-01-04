package com.taotao.cloud.workflow.api.common.database.data;

/**
 * 数据库上下文切换
 */
public class DataSourceContextHolder {

    private static final ThreadLocal<String> CONTEXT_DB_NAME_HOLDER = new ThreadLocal<>();

    private static final ThreadLocal<String> CONTEXT_DB_ID_HOLDER = new ThreadLocal<>();

    /**
     * 设置当前数据库
     */
    public static void setDatasource(String dbId,String dbName) {
        CONTEXT_DB_NAME_HOLDER.set(dbName);
        CONTEXT_DB_ID_HOLDER.set(dbId);
    }

    /**
     * 取得当前数据源Id
     */
    public static String getDatasourceId() {
        String str = CONTEXT_DB_ID_HOLDER.get();
        return str;
    }
    /**
     * 取得当前数据源名称
     */
    public static String getDatasourceName() {
        String str = CONTEXT_DB_NAME_HOLDER.get();
        return str;
    }

    /**
     * 清除上下文数据
     */
    public static void clearDatasourceType() {
        CONTEXT_DB_NAME_HOLDER.remove();
        CONTEXT_DB_ID_HOLDER.remove();
    }
}
