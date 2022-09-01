package com.taotao.cloud.office.easypoi.easypoi.test.excel.read;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.test.entity.NewDateEntity;
import junit.framework.Assert;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * @author by jueyue on 19-6-23.
 */
public class ExcelImportNewDateTest {

    @Test
    public void importTest() {
        ImportParams params = new ImportParams();
        params.setTitleRows(1);
        params.setHeadRows(1);
        long start = new Date().getTime();
        List<NewDateEntity> list = ExcelImportUtil.importExcel(
                new File(FileUtilTest.getWebRootPath("import/ExcelNewDateTest.xlsx")), NewDateEntity.class, params);
        System.out.println(new Date().getTime() - start);
        Assert.assertEquals(list.size(), 100);
        System.out.println(list.size());
        System.out.println(ReflectionToStringBuilder.toString(list.get(1)));

    }
}
