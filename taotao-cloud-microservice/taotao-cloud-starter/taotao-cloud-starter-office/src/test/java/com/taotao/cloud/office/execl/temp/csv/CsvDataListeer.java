package com.taotao.cloud.office.execl.temp.csv;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson2.JSON;
import com.taotao.cloud.common.utils.log.LogUtils;


public class CsvDataListeer extends AnalysisEventListener<CsvData> {
    @Override
    public void invoke(CsvData data, AnalysisContext context) {
        LogUtils.info("data:{}", JSON.toJSONString(data));
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }
}
