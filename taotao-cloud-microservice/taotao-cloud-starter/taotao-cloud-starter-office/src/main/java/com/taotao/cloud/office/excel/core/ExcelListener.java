package com.taotao.cloud.office.excel.core;

import com.alibaba.excel.read.listener.ReadListener;

/**
 * Excel 导入监听
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-07-31 20:57:55
 */
public interface ExcelListener<T> extends ReadListener<T> {

    ExcelResult<T> getExcelResult();

}
