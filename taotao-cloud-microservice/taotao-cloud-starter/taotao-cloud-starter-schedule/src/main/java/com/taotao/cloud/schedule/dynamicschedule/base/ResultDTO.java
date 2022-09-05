package com.taotao.cloud.schedule.dynamicschedule.base;


import lombok.Data;

import java.io.Serializable;


/**
 * 返回数据格式
 *
 */
public class ResultDTO implements Serializable {

	private int code;

	private String msg;

	private Object data;

	private int count;
	public ResultDTO(ResultCode code, Object result) {
		this.code = code.getCode();
		this.msg = code.getErrorDesc();
		this.data = result;
	}

	public ResultDTO(ResultCode code, String errorDesc, Object result) {
		this.code = code.getCode();
		this.msg = errorDesc;
		this.data = result;
	}

	public ResultDTO(int code, String errorDesc, Object result) {
		this.code = code;
		this.msg = errorDesc;
		this.data = result;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
