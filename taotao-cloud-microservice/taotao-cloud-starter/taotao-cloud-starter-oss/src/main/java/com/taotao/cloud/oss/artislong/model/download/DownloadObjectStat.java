package com.taotao.cloud.oss.artislong.model.download;


import java.io.Serializable;
import java.util.Date;

/**
 * @author 陈敏
 * @version ObjectStat.java, v 1.1 2022/2/21 15:15 chenmin Exp $ Created on 2022/2/21
 */
public class DownloadObjectStat implements Serializable {

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
