package com.taotao.cloud.rpc.common.common;

/**
 * rpc返回数据实体<br>
 *
 * @author shuigedeng
 * @version v1.0.0
 */
public class RpcReponse {
    private String requestId;
    private Throwable errorMsg;
    private Object result;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Throwable getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(Throwable errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public boolean isError() {
        return errorMsg != null;
    }
}
