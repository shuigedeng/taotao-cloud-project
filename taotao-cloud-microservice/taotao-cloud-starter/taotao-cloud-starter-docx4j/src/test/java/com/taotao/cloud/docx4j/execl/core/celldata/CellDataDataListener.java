package com.taotao.cloud.docx4j.execl.core.celldata;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.fastjson2.JSON;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**

 */
public class CellDataDataListener extends AnalysisEventListener<CellDataReadData> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CellDataDataListener.class);
    List<CellDataReadData> list = new ArrayList<>();

    @Override
    public void invoke(CellDataReadData data, AnalysisContext context) {
        list.add(data);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        Assert.assertEquals(list.size(), 1);
        CellDataReadData cellDataData = list.get(0);

        Assert.assertEquals("2020年01月01日", cellDataData.getDate().getData());
        Assert.assertEquals((long)cellDataData.getInteger1().getData(), 2L);
        Assert.assertEquals((long)cellDataData.getInteger2(), 2L);
        if (context.readWorkbookHolder().getExcelType() != ExcelTypeEnum.CSV) {
            Assert.assertEquals(cellDataData.getFormulaValue().getFormulaData().getFormulaValue(), "B2+C2");
        } else {
            Assert.assertNull(cellDataData.getFormulaValue().getData());
        }
        LOGGER.debug("First row:{}", JSON.toJSONString(list.get(0)));
    }
}
