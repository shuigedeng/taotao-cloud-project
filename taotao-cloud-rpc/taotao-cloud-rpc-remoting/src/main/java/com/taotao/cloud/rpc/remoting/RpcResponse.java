package com.taotao.cloud.rpc.remoting;

import java.io.Serializable;

/**
 * RPC 响应
 */

public class RpcResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 请求 ID
     */
    private String requestId;

    /**
     * 返回结果
     */
    private Object result;

    /**
     * 异常信息
     */
    private Throwable exception;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 创建成功响应
     */
    public static RpcResponse success(String requestId, Object result) {
        RpcResponse response = new RpcResponse();
        response.setRequestId(requestId);
        response.setResult(result);
        return response;
    }

    /**
     * 创建失败响应
     */
    public static RpcResponse error(String requestId, Throwable exception) {
        RpcResponse response = new RpcResponse();
        response.setRequestId(requestId);
        response.setException(exception);
        response.setErrorMessage(exception.getMessage());
        return response;
    }

    /**
     * 是否有异常
     */
    public boolean hasException() {
        return exception != null;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Throwable getException() {
        return exception;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
