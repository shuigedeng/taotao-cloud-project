package com.taotao.cloud.sys.biz.tools.jvm.service.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class CommandResultContext {
    /**
     * 命令的原始返回结果
     */
    @JsonIgnore
    private String origin;

    /**
     * 命令处理中间结果
     */
    private Object result;

    public CommandResultContext() {
    }

    public CommandResultContext(String origin) {
        this.origin = origin;
    }

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}
}
