package com.taotao.cloud.common.execl.demo.write;

import com.alibaba.excel.metadata.data.WriteCellData;



/**
 * 根据WriteCellData写
 *

 */
com.taotao.cloud.common.execl
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
}
