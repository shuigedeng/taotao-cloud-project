package com.taotao.cloud.common.execl.temp.csv;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.taotao.cloud.common.utils.log.LogUtil;


public class CsvDataListeer extends AnalysisEventListener<CsvData> {
    @Override
    public void invoke(CsvData data, AnalysisContext context) {
        LogUtil.info("data:{}", JSON.toJSONString(data));
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }
}
