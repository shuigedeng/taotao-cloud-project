package com.taotao.cloud.workflow.biz.common.base;

/**
 * 操作类型枚举类
 *
 */
public enum HandleMethodEnum {
    /**
     * 登录
     */
    GET("GET", "查询"),
    /**
     * 访问
     */
    POST("POST", "新增"),
    /**
     * 操作
     */
    PUT("PUT", "修改"),
    /**
     * 异常
     */
    DELETE("DELETE", "删除"),
    /**
     * 请求
     */
    IMPORT("IMPORT", "导入"),
    /**
     * 请求
     */
    EXPORT("EXPORT", "导出");

    /**
     * 请求方式
     */
    private String requestType;

    /**
     * 操作类型
     */
    private String requestMethod;

    HandleMethodEnum(String requestType, String requestMethod) {
        this.requestType = requestType;
        this.requestMethod = requestMethod;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    /**
     * 根据请求方式获取操作类型
     *
     * @return
     */
    public static String getMethodByType(String requestType) {
        for (HandleMethodEnum status : HandleMethodEnum.values()) {
            if (status.getRequestType().equals(requestType)) {
                return status.requestMethod;
            }
        }
        return null;
    }

}
