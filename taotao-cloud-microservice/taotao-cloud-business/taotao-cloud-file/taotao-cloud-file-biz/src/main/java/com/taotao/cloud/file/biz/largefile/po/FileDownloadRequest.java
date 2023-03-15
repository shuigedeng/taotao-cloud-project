package com.taotao.cloud.file.biz.largefile.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileDownloadRequest {

	private String path;

	private String name;
}
