package com.taotao.cloud.sys.biz.support.docx4j.input.builder.sheet;


import java.io.Serializable;

/**
 * 行记录解析结果
 *
 */
public class RowMessage implements Serializable {

	/**
	 * 行号
	 */
	private int row;
	/**
	 * 错误消息
	 */
	private String message;

	public RowMessage() {
	}

	public RowMessage(int row, String message) {
		this.row = row;
		this.message = message;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
