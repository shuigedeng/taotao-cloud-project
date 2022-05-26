package com.taotao.cloud.common.execl.demo.read;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.fastjson2.JSON;
import com.taotao.cloud.common.utils.log.LogUtil;

import java.util.List;

/**
 * 模板的读取类
 *

 */

public class IndexOrNameDataListener extends AnalysisEventListener<IndexOrNameData> {
    /**
     * 每隔5条存储数据库，实际使用中可以100条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 5;
    private List<IndexOrNameData> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    @Override
    public void invoke(IndexOrNameData data, AnalysisContext context) {
        LogUtil.info("解析到一条数据:{}", JSON.toJSONString(data));
        cachedDataList.add(data);
        if (cachedDataList.size() >= BATCH_COUNT) {
            saveData();
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        saveData();
        LogUtil.info("所有数据解析完成！");
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        LogUtil.info("{}条数据，开始存储数据库！", cachedDataList.size());
        LogUtil.info("存储数据库成功！");
    }
}
