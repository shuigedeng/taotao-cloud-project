package com.taotao.cloud.docx4j.execl.core.annotation;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson2.JSON;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class AnnotationIndexAndNameDataListener extends AnalysisEventListener<AnnotationIndexAndNameData> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AnnotationIndexAndNameDataListener.class);
    List<AnnotationIndexAndNameData> list = new ArrayList<AnnotationIndexAndNameData>();

    @Override
    public void invoke(AnnotationIndexAndNameData data, AnalysisContext context) {
        list.add(data);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        Assert.assertEquals(list.size(), 1);
        AnnotationIndexAndNameData data = list.get(0);
        Assert.assertEquals(data.getIndex0(), "第0个");
        Assert.assertEquals(data.getIndex1(), "第1个");
        Assert.assertEquals(data.getIndex2(), "第2个");
        Assert.assertEquals(data.getIndex4(), "第4个");
        LOGGER.debug("First row:{}", JSON.toJSONString(list.get(0)));
    }
}
