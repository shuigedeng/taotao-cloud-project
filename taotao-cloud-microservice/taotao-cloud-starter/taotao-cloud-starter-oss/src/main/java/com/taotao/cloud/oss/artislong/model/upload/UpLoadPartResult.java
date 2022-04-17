package com.taotao.cloud.oss.artislong.model.upload;


/**
 * 分块结果集
 */
public class UpLoadPartResult {

	/**
	 * 分块号
	 */
	private int number;
	/**
	 * 分块在文件中的偏移量
	 */
	private long offset;
	/**
	 * 分块大小
	 */
	private long length;
	/**
	 * 分块失败标识
	 */
	private boolean failed = false;
	/**
	 * 分块上传失败异常
	 */
	private Exception exception;
	/**
	 * 分块crc
	 */
	private Long partCrc;

	private UpLoadPartEntityTag entityTag;

	public UpLoadPartResult(int number, long offset, long length) {
		this.number = number;
		this.offset = offset;
		this.length = length;
	}

	public UpLoadPartResult(int number, long offset, long length, long partCrc) {
		this.number = number;
		this.offset = offset;
		this.length = length;
		this.partCrc = partCrc;
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

	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
	}

	public boolean isFailed() {
		return failed;
	}

	public void setFailed(boolean failed) {
		this.failed = failed;
	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

	public Long getPartCrc() {
		return partCrc;
	}

	public void setPartCrc(Long partCrc) {
		this.partCrc = partCrc;
	}

	public UpLoadPartEntityTag getEntityTag() {
		return entityTag;
	}

	public void setEntityTag(UpLoadPartEntityTag entityTag) {
		this.entityTag = entityTag;
	}
}
