package com.taotao.cloud.excel.execl.demo.write;

import com.alibaba.excel.metadata.data.WriteCellData;



/**
 * 根据WriteCellData写
 *

 */
public class WriteCellDemoData {
    /**
     * 超链接
     *
     */
    private WriteCellData<String> hyperlink;

    /**
     * 备注
     *
     */
    private WriteCellData<String> commentData;

    /**
     * 公式
     *
     */
    private WriteCellData<String> formulaData;

    /**
     * 指定单元格的样式。当然样式 也可以用注解等方式。
     *
     */
    private WriteCellData<String> writeCellStyle;

    /**
     * 指定一个单元格有多个样式
     *
     */
    private WriteCellData<String> richText;

	public WriteCellData<String> getHyperlink() {
		return hyperlink;
	}

	public void setHyperlink(WriteCellData<String> hyperlink) {
		this.hyperlink = hyperlink;
	}

	public WriteCellData<String> getCommentData() {
		return commentData;
	}

	public void setCommentData(WriteCellData<String> commentData) {
		this.commentData = commentData;
	}

	public WriteCellData<String> getFormulaData() {
		return formulaData;
	}

	public void setFormulaData(WriteCellData<String> formulaData) {
		this.formulaData = formulaData;
	}

	public WriteCellData<String> getWriteCellStyle() {
		return writeCellStyle;
	}

	public void setWriteCellStyle(WriteCellData<String> writeCellStyle) {
		this.writeCellStyle = writeCellStyle;
	}

	public WriteCellData<String> getRichText() {
		return richText;
	}

	public void setRichText(WriteCellData<String> richText) {
		this.richText = richText;
	}
}
