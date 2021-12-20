package com.taotao.cloud.portal.util;


/**
 * @author xiaoming
 * @since 2020/3/9
 * @blame xiaoming
 */
public class ResponseBase {

    /**
     * 响应码
     */
    private Integer code;

    /**
     * 消息内容
     */
    private String message;

    /**
     * 返回数据
     */
    private Object data;

    public ResponseBase(){}

    public ResponseBase(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
