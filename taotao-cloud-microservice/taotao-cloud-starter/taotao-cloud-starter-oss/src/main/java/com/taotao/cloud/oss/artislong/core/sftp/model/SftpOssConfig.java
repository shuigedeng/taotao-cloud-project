package com.taotao.cloud.oss.artislong.core.sftp.model;

import cn.hutool.extra.ftp.FtpConfig;
import com.taotao.cloud.oss.artislong.utils.OssPathUtil;

/**
 * @author 陈敏
 * @version SftpOssConfig.java, v 1.1 2022/2/20 9:06 chenmin Exp $
 * Created on 2022/2/20
 */
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
