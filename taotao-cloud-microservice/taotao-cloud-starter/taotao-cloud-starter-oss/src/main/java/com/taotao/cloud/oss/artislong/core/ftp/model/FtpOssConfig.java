package com.taotao.cloud.oss.artislong.core.ftp.model;

import cn.hutool.extra.ftp.FtpConfig;
import cn.hutool.extra.ftp.FtpMode;
import com.taotao.cloud.oss.artislong.utils.OssPathUtil;

public class FtpOssConfig extends FtpConfig {

    private String basePath;
    /**
     * FTP连接模式,默认被动
     */
    private FtpMode mode = FtpMode.Passive;
    /**
     * 设置执行完操作是否返回当前目录,默认false
     */
    private boolean backToPwd = false;

    public void init() {
        basePath = OssPathUtil.valid(basePath);
    }

	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	public FtpMode getMode() {
		return mode;
	}

	public void setMode(FtpMode mode) {
		this.mode = mode;
	}

	public boolean isBackToPwd() {
		return backToPwd;
	}

	public void setBackToPwd(boolean backToPwd) {
		this.backToPwd = backToPwd;
	}
}
