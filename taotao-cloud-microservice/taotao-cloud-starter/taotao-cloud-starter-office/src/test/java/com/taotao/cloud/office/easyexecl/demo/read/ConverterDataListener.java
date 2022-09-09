package com.taotao.cloud.office.easyexecl.demo.read;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.fastjson2.JSON;
import com.taotao.cloud.common.utils.log.LogUtils;

import java.util.List;

/**
 * 模板的读取类
 *

 */

public class ConverterDataListener implements ReadListener<ConverterData> {

    /**
     * 每隔5条存储数据库，实际使用中可以100条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 5;
    private List<ConverterData> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    @Override
    public void invoke(ConverterData data, AnalysisContext context) {
        LogUtils.info("解析到一条数据:{}", JSON.toJSONString(data));
        cachedDataList.add(data);
        if (cachedDataList.size() >= BATCH_COUNT) {
            saveData();
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        saveData();
        LogUtils.info("所有数据解析完成！");
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        LogUtils.info("{}条数据，开始存储数据库！", cachedDataList.size());
        LogUtils.info("存储数据库成功！");
    }
}