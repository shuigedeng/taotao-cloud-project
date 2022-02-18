package com.taotao.cloud.web.docx4j.output.builder.document;


/**
 * 段落样式用于word文档构建
 * 所有样式来源于<code>styles.xml</code>
 */
public enum ParagraphStyle {
    /**
     * 标题一样式
     */
    HEADING_1("1", "标题一"),
    /**
     * 标题二样式
     */
    HEADING_2("2", "标题二"),
    /**
     * 标题三样式
     */
    HEADING_3("3", "标题三"),
    /**
     * 标题五样式
     */
    HEADING_5("5", "标题五"),
    /**
     * 标题七样式
     */
    HEADING_7("7", "标题七"),
    /**
     * 标题九样式
     */
    HEADING_9("9", "标题九"),
    /**
     * 居中副标题
     */
    SUB_HEADING("a4", "副标题");

    /**
     * 样式id
     */
    public final String id;
    /**
     * 样式名称
     */
    public final String name;

	ParagraphStyle(String id, String name) {
		this.id = id;
		this.name = name;
	}
}
