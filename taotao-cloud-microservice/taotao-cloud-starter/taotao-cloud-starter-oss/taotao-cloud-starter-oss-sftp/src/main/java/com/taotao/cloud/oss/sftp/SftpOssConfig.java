package com.taotao.cloud.oss.sftp;

import cn.hutool.extra.ftp.FtpConfig;
import com.taotao.cloud.oss.common.util.OssPathUtil;

/**
 * sftp oss配置
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:42:52
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
