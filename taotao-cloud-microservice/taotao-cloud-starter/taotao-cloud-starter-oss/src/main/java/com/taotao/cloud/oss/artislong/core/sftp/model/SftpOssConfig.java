package com.taotao.cloud.oss.artislong.core.sftp.model;

import cn.hutool.extra.ftp.FtpConfig;
import com.taotao.cloud.oss.artislong.utils.OssPathUtil;

public class SftpOssConfig extends FtpConfig {

    private String basePath;

    public void init() {
        basePath = OssPathUtil.valid(basePath);
    }

	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}
}
