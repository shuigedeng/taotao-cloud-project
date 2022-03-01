package com.taotao.cloud.sys.biz.elasticsearch.pagemodel;

import java.util.HashMap;
import java.util.List;

import org.springframework.http.HttpStatus;

public class AggsResultResponse {
	private HttpStatus status; //应用程序状态码

    private Integer code; //响应码

    private String message;//错误消息

    private String developerMessage;//错误堆栈消息
    
    HashMap<String, Object> rs;


	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
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

	public String getDeveloperMessage() {
		return developerMessage;
	}

	public void setDeveloperMessage(String developerMessage) {
		this.developerMessage = developerMessage;
	}
	
	public HashMap<String, Object> getRs() {
		return rs;
	}

	public void setRs(HashMap<String, Object> rs) {
		this.rs = rs;
	}

	public AggsResultResponse(HttpStatus status, Integer code, String message, String developerMessage,
			 HashMap<String, Object> rs) {
		super();
		this.status = status;
		this.code = code;
		this.message = message;
		this.developerMessage = developerMessage;
		this.rs = rs;
	}

    
}
