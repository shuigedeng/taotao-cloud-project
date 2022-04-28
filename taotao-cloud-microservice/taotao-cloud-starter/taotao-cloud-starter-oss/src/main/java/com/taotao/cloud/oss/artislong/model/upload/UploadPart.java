package com.taotao.cloud.oss.artislong.model.upload;


import java.io.Serial;
import java.io.Serializable;

/**
 * 分块
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:34:32
 */
public class UploadPart implements Serializable {

	@Serial
	private static final long serialVersionUID = 6692863980224332199L;

	/**
	 * 分块号(顺序)
	 */
	private int number;
	/**
	 * 分块在文件中的偏移量
	 */
	private long offset;
	/**
	 * 分块大小
	 */
	private long size;
	/**
	 * 分块成功标识
	 */
	private boolean isCompleted = false;
	/**
	 * 分块crc
	 */
	private long crc;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (isCompleted ? 1231 : 1237);
		result = prime * result + number;
		result = prime * result + (int) (offset ^ (offset >>> 32));
		result = prime * result + (int) (size ^ (size >>> 32));
		result = prime * result + (int) (crc ^ (crc >>> 32));
		return result;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public long getOffset() {
		return offset;
	}

	public void setOffset(long offset) {
		this.offset = offset;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public boolean isCompleted() {
		return isCompleted;
	}

	public void setCompleted(boolean completed) {
		isCompleted = completed;
	}

	public long getCrc() {
		return crc;
	}

	public void setCrc(long crc) {
		this.crc = crc;
	}
}
