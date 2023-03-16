/**
 * Project Name: my-projects Package Name: com.taotao.rpc.common Date: 2020/2/27 11:08 Author:
 * shuigedeng
 */
package com.taotao.cloud.demo.rpc.taotao;

/**
 * rpc返回数据实体<br>
 *
 * @author shuigedeng
 * @version v1.0.0
 * @create 2020/2/27 11:08
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
