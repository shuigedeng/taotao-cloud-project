package com.taotao.cloud.excel.execl.core.encrypt;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson2.JSON;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**

 */
public class EncryptDataListener extends AnalysisEventListener<EncryptData> {
    private static final Logger LOGGER = LoggerFactory.getLogger(EncryptDataListener.class);
    List<EncryptData> list = new ArrayList<EncryptData>();

    @Override
    public void invoke(EncryptData data, AnalysisContext context) {
        list.add(data);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        Assert.assertEquals(list.size(), 10);
        Assert.assertEquals(list.get(0).getName(), "姓名0");
        Assert.assertEquals((int)(context.readSheetHolder().getSheetNo()), 0);
        Assert.assertEquals(
            context.readSheetHolder().getExcelReadHeadProperty().getHeadMap().get(0).getHeadNameList().get(0), "姓名");
        LOGGER.debug("First row:{}", JSON.toJSONString(list.get(0)));
    }
}
