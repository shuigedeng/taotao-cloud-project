package com.taotao.cloud.oss.common.model.download;


import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 下载对象属性
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:34:49
 */
public class DownloadObjectStat implements Serializable {

	@Serial
	private static final long serialVersionUID = -2883494783412999919L;

	private long size;
	private Date lastModified;
	private String digest;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((digest == null) ? 0 : digest.hashCode());
		result = prime * result + ((lastModified == null) ? 0 : lastModified.hashCode());
		result = prime * result + (int) (size ^ (size >>> 32));
		return result;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public String getDigest() {
		return digest;
	}

	public void setDigest(String digest) {
		this.digest = digest;
	}
}
