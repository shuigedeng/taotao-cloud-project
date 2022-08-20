package com.taotao.cloud.office.util.excel;

import com.alibaba.excel.EasyExcel;

import com.taotao.cloud.office.util.refactor.ThrowingConsumer;
import java.io.InputStream;
import java.util.List;

/**
 * @Description 导入excel模板
 */
public class ExcelImportUtil<T> {

    /**
     * 通用导入excel文件方法
     *
     * @param fileStream 导入的文件流
     * @param rowDto 接收excel每行数据的实体
     * @param rowAction 将接收到的实体进行自定义的业务处理逻辑方法
     * @param <T> 实体类型
     */
    public static <T> void importFile(InputStream fileStream, T rowDto, ThrowingConsumer<List<T>> rowAction) {
        // 获取excel通用监听器
        ExcelImportCommonListener<T> commonListener = new ExcelImportCommonListener<>(rowAction);
        // 读取excel文件并导入
        EasyExcel.read(fileStream, rowDto.getClass(), commonListener).sheet().doRead();
    }

}
