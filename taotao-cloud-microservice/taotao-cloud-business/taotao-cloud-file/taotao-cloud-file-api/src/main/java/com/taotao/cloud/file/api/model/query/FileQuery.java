package com.taotao.cloud.file.api.model.query;

import lombok.Data;

@Data
public class FileQuery {

	private Long dictId;
	private String itemText;
	private String itemValue;
	private String description;
	private Integer status;
}
