package com.taotao.cloud.office.easypoi.easypoi.test.excel.read;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.test.entity.MsgClient;
import cn.afterturn.easypoi.test.entity.onettomany.hasname.DemandEntity;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ExcelImportUtilOneToManyHasNameTest {

    ///ExcelExportMsgClient 测试是这个到处的数据下个版本吧,现在还不支持
    //

    @Test
    public void test() throws Exception {
        ImportParams params = new ImportParams();
        params.setTitleRows(1);
        params.setHeadRows(3);
        long start = new Date().getTime();
        List<DemandEntity> list = ExcelImportUtil.importExcel(
                new FileInputStream(
                        new File(FileUtilTest.getWebRootPath("import/OneToManyHaseNameTest.demandEntityTest.xlsx"))),
                DemandEntity.class, params);
        for (int i = 0; i < list.size(); i++) {
            System.out.println(ReflectionToStringBuilder.toString(list.get(i)));
        }
        Assert.assertEquals(100,list.size());
    }


}
