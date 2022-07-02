package com.taotao.cloud.sys.biz.support.docx4j.output.builder;

/**
 * 支持导出的文件类型
 */
public interface OutputFileType {
    /**
     * 枚举名称 用于定义文件后缀名
     * @return 枚举名称
     */
    String name();

    /**
     * 获得文件全名
     * @param name 文件名称
     * @return 文件名称.后缀名
     */
    default String fullName(String name) {
        return String.format("%s.%s", name, this.name().toLowerCase());
    }
}
