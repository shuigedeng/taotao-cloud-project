package com.taotao.cloud.office.util.excel;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ConverterUtils;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.office.util.constant.ImportConstant;
import com.taotao.cloud.office.util.refactor.ThrowingConsumer;
import com.taotao.cloud.office.util.valid.ImportValid;
import org.apache.commons.compress.utils.Lists;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class ExcelImportCommonListener<T> implements ReadListener<T> {

    /**
     * 转换后插入数据表的实体
     */
    private List<T> persistentDataList = Lists.newArrayList();

    /**
     * 具体数据落库的业务逻辑方法：此处的逻辑是将数据从excel中读取出来后，然后进行自己的业务处理，最后进行落库操作
     * 不懂的可以参考：UserServiceImpl下的saveUserList方法案例
     */
    private final ThrowingConsumer<List<T>> persistentActionMethod;

    /**
     * 异常日志记录(可用于记录解析excel数据时存储异常信息，用于业务的事务的回滚)
     */
    private List<String> errorLogList = new ArrayList<>();

    /**
     * 用于测试异常变量
     */
    private Long count = 1000L;

    /**
     * 构造函数(不包含异常信息)
     *
     * @param persistentActionMethod 从excel读取到的数据到落库的业务逻辑
     */
    public ExcelImportCommonListener(ThrowingConsumer<List<T>> persistentActionMethod) {
        this.persistentActionMethod = persistentActionMethod;
    }

    /**
     * 构造函数(不包含异常信息)
     *
     * @param persistentActionMethod 从excel读取到的数据到落库的业务逻辑
     */
    public ExcelImportCommonListener(ThrowingConsumer<List<T>> persistentActionMethod, List<String> errorLogLIst) {
        this.persistentActionMethod = persistentActionMethod;
        this.errorLogList = errorLogLIst;
    }

    /**
     * 在转换异常 获取其他异常情况下会调用本接口。抛出异常则停止读取。如果这里不抛出异常则 继续读取下一行。
     * 根据自己需要看是否抛出异常，如果没有特殊要求，则可以不修改。
     *
     * @param exception
     * @param context
     * @throws Exception
     */
    @Override
    public void onException(Exception exception, AnalysisContext context) throws Exception {
        if (exception instanceof ExcelDataConvertException) {
            ExcelDataConvertException excelDataConvertException = (ExcelDataConvertException) exception;
	        LogUtil.error("第{}行，第{}列解析异常，数据为:{}", excelDataConvertException.getRowIndex(),
                    excelDataConvertException.getColumnIndex(), excelDataConvertException.getCellData().getStringValue());
            if (Objects.nonNull(errorLogList)) {
                // 记录异常日志
                String errorLog = "第" + excelDataConvertException.getRowIndex() + "行，第" + excelDataConvertException.getColumnIndex()
                        + "列解析异常，数据为:" + excelDataConvertException.getCellData().getStringValue() + "";
                errorLogList.add(errorLog);
            }
        }
    }

    /**
     * 返回每个sheet页的表头，根据自己实际业务进行表头字段等校验逻辑，如果没有则保持不动
     *
     * @param headMap
     * @param context
     */
    @Override
    public void invokeHead(Map<Integer, ReadCellData<?>> headMap, AnalysisContext context) {
        Map<Integer, String> headMapping = ConverterUtils.convertToStringMap(headMap, context);
	    LogUtil.info("表头数据: " + StrUtil.toString(headMapping));
        if (CollUtil.isEmpty(headMapping)) {
            errorLogList.add("The header of file can't be empty!");
        }
    }

    /**
     * 解析excel表中每行数据
     *
     * @param t
     * @param analysisContext
     */
    @Override
    public void invoke(T t, AnalysisContext analysisContext) {
        try {
            // 如果对实体需要设置其他额外属性，可以通过反射方式，如下面的relationId属性
            Class<?> aClass = t.getClass();
            Field relationIdField = aClass.getDeclaredField("relationId");
            relationIdField.setAccessible(Boolean.TRUE);
            relationIdField.set(t, count++);
        } catch (Exception e) {
	        LogUtil.error("in error{}", e);
            errorLogList.add("The Row Data inject relationId field in error");
        }
        // 校验导入字段
        ImportValid.validRequireField(t,errorLogList);
        if (Objects.isNull(errorLogList) || CollUtil.isEmpty(errorLogList)) {
            persistentDataList.add(t);
            // 当数据达到最大插入数量后则进行落库操作，防止大数量情况下OOM
            if (persistentDataList.size() >= ImportConstant.MAX_INSERT_COUNT) {
                // 进行业务数据插入
                this.persistentDataToDb(persistentDataList);
                // 清空集合
                persistentDataList.clear();
            }
        }
    }


    /**
     * 所有数据解析完后，回调
     *
     * @param analysisContext
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        // 在此处调用落库操作，防止有剩余数据未落库
        if (CollUtil.isNotEmpty(persistentDataList)) {
            persistentDataToDb(persistentDataList);
        }
    }

    /**
     * 将数据持久化到数据库中
     *
     * @param data
     */
    private void persistentDataToDb(List<T> data) {
        // 对数据分组，批量插入
        List<List<T>> dataList = ListUtil.split(data, ImportConstant.MAX_INSERT_COUNT);
        dataList.stream().forEach(persistentActionMethod);
    }
}
