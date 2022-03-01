package com.taotao.cloud.sys.biz.tools.database.dtos;


import javax.validation.constraints.NotNull;

public class TemplateContent {
    @NotNull
    private String name;
    @NotNull
    private String content;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
