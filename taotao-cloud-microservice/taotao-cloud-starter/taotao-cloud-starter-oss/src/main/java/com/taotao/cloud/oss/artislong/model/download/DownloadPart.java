package com.taotao.cloud.oss.artislong.model.download;


import java.io.Serializable;

public class DownloadPart implements Serializable {

	private static final long serialVersionUID = -3655925846487976207L;

	private int index;
	private long start;
	private long end;
	private boolean isCompleted;
	private long length;
	private long crc;
	private long fileStart;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + index;
		result = prime * result + (isCompleted ? 1231 : 1237);
		result = prime * result + (int) (end ^ (end >>> 32));
		result = prime * result + (int) (start ^ (start >>> 32));
		result = prime * result + (int) (crc ^ (crc >>> 32));
		result = prime * result + (int) (fileStart ^ (fileStart >>> 32));
		return result;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public long getEnd() {
		return end;
	}

	public void setEnd(long end) {
		this.end = end;
	}

	public boolean isCompleted() {
		return isCompleted;
	}

	public void setCompleted(boolean completed) {
		isCompleted = completed;
	}

	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
	}

	public long getCrc() {
		return crc;
	}

	public void setCrc(long crc) {
		this.crc = crc;
	}

	public long getFileStart() {
		return fileStart;
	}

	public void setFileStart(long fileStart) {
		this.fileStart = fileStart;
	}
}
