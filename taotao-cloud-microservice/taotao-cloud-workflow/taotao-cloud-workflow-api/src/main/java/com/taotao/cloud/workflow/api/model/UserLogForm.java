package com.taotao.cloud.workflow.api.model;

import lombok.Data;

@Data
public class UserLogForm extends Pagination {

	private String startTime;
	private String endTime;
	private int category;
}
