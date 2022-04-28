package com.taotao.cloud.common.execl.temp.csv;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;


public class CsvDataListeer extends AnalysisEventListener<CsvData> {
    @Override
    public void invoke(CsvData data, AnalysisContext context) {
        log.info("data:{}", JSON.toJSONString(data));
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }
}
