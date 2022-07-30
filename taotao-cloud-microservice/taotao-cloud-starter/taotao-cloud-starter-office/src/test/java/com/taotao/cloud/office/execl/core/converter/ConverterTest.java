package com.taotao.cloud.office.execl.core.converter;

import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.converters.floatconverter.FloatNumberConverter;
import com.alibaba.excel.metadata.data.WriteCellData;
import java.math.BigDecimal;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**

 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ConverterTest {

    @Test
    public void t01FloatNumberConverter() {
        FloatNumberConverter floatNumberConverter = new FloatNumberConverter();
        WriteConverterContext<Float> context = new WriteConverterContext<>();
        context.setValue(95.62F);
        WriteCellData<?> writeCellData = floatNumberConverter.convertToExcelData(context);
        Assert.assertEquals(0, writeCellData.getNumberValue().compareTo(new BigDecimal("95.62")));
    }

}
