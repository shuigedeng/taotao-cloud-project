package com.taotao.cloud.oss.artislong.model.upload;


import java.io.File;
import java.io.Serial;
import java.io.Serializable;

/**
 * 文件状态
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:34:27
 */
public class UpLoadFileStat implements Serializable {

	@Serial
	private static final long serialVersionUID = -1223810339796425415L;

	/**
	 * 文件大小
	 */
	private long size;
	/**
	 * 文件最新修改时间
	 */
	private long lastModified;
	/**
	 * 文件内容签名
	 */
	private String digest;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((digest == null) ? 0 : digest.hashCode());
		result = prime * result + (int) (lastModified ^ (lastModified >>> 32));
		result = prime * result + (int) (size ^ (size >>> 32));
		return result;
	}

	public static UpLoadFileStat getFileStat(String uploadFile) {
		UpLoadFileStat fileStat = new UpLoadFileStat();
		File file = new File(uploadFile);
		fileStat.setSize(file.length());
		fileStat.setLastModified(file.lastModified());
		return fileStat;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public long getLastModified() {
		return lastModified;
	}

	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}

	public String getDigest() {
		return digest;
	}

	public void setDigest(String digest) {
		this.digest = digest;
	}
}
