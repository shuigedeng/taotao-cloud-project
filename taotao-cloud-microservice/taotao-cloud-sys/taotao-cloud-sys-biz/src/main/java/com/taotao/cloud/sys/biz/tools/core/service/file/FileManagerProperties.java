package com.taotao.cloud.sys.biz.tools.core.service.file;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@ConfigurationProperties(prefix = "data.path")
public class FileManagerProperties {
    /**
     * 基础路径配置, 如果后面的路径没配置,则会默认以这个为基础路径 , 即
     * configs: $base/configs
     * tmp: $base/tmp
     * data: $base/data
     */
    private File base;
    /**
     * 配置文件路径
     */
    private File configs;
    /**
     * 临时文件路径
     */
    private File tmp;
    /**
     * 数据目录
     */
    private File data;

	public File getBase() {
		return base;
	}

	public void setBase(File base) {
		this.base = base;
	}

	public File getConfigs() {
		return configs;
	}

	public void setConfigs(File configs) {
		this.configs = configs;
	}

	public File getTmp() {
		return tmp;
	}

	public void setTmp(File tmp) {
		this.tmp = tmp;
	}

	public File getData() {
		return data;
	}

	public void setData(File data) {
		this.data = data;
	}
}
