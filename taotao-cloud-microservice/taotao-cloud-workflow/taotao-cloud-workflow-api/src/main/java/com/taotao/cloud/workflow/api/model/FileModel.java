package com.taotao.cloud.workflow.api.model;

import lombok.Data;

@Data
public class FileModel {

	private String fileId;
	private String fileName;
	private String fileSize;
	private String fileTime;
	private String fileState;
	private String fileType;
}
