package com.taotao.cloud.sys.api.model.query;

import lombok.Data;

@Data
public class DictQuery {

	private Long dictId;
	private String itemText;
	private String itemValue;
	private String description;
	private Integer status;
}
