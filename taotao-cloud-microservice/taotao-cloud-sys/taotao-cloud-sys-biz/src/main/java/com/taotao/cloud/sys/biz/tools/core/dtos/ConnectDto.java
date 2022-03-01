package com.taotao.cloud.sys.biz.tools.core.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 *  {module:[{},{}],module2:[{},{}]}
 */
public class ConnectDto {
    /**
     * 模块名称
     */
    private String module;
    /**
     * 连接名称
     */
    private String name;
    /**
     * 上次修改时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastModified;

    public ConnectDto() {
    }

    public ConnectDto(String module, String name, Date lastModified) {
        this.module = module;
        this.name = name;
        this.lastModified = lastModified;
    }

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}
}
