package com.taotao.cloud.office.excel.core;

import java.util.List;

/**
 * excel返回对象
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-07-31 20:57:59
 */
public interface ExcelResult<T> {

    /**
     * 对象列表
     */
    List<T> getList();

    /**
     * 错误列表
     */
    List<String> getErrorList();

    /**
     * 导入回执
     */
    String getAnalysis();
}
