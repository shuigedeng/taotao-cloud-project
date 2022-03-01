package com.taotao.cloud.sys.biz.tools.core.controller.dtos;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * 上传多文件到类加载器, 并附带一个默认的 pom
 */
public class UploadClassInfo {
    private String classloaderName;
    @JSONField(serialize = false)
    private MultipartFile [] files;
    private String pomContent;

	public String getClassloaderName() {
		return classloaderName;
	}

	public void setClassloaderName(String classloaderName) {
		this.classloaderName = classloaderName;
	}

	public MultipartFile[] getFiles() {
		return files;
	}

	public void setFiles(MultipartFile[] files) {
		this.files = files;
	}

	public String getPomContent() {
		return pomContent;
	}

	public void setPomContent(String pomContent) {
		this.pomContent = pomContent;
	}
}
