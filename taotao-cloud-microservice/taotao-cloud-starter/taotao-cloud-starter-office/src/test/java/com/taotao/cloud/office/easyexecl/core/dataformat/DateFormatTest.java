package com.taotao.cloud.office.easyexecl.core.dataformat;

import com.alibaba.excel.EasyExcel;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.office.easyexecl.util.TestFileUtil;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.File;
import java.util.List;
import java.util.Locale;

/**

 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class DateFormatTest {

    private static File file07;
    private static File file03;

    @BeforeClass
    public static void init() {
        file07 = TestFileUtil.readFile("dataformat" + File.separator + "dataformat.xlsx");
        file03 = TestFileUtil.readFile("dataformat" + File.separator + "dataformat.xls");
    }

    @Test
    public void t01Read07() {
        readCn(file07);
        readUs(file07);
    }

    @Test
    public void t02Read03() {
        readCn(file03);
        readUs(file03);
    }

    private void readCn(File file) {
        List<DateFormatData> list =
            EasyExcel.read(file, DateFormatData.class, null).locale(Locale.CHINA).sheet().doReadSync();
        for (DateFormatData data : list) {
            if (data.getDateStringCn() != null && !data.getDateStringCn().equals(data.getDate())) {
                LogUtils.info("date:cn:{},{}", data.getDateStringCn(), data.getDate());
            }
            if (data.getNumberStringCn() != null && !data.getNumberStringCn().equals(data.getNumber())) {
                LogUtils.info("number:cn{},{}", data.getNumberStringCn(), data.getNumber());
            }
        }
        for (DateFormatData data : list) {
            Assert.assertEquals(data.getDateStringCn(), data.getDate());
            Assert.assertEquals(data.getNumberStringCn(), data.getNumber());
        }
    }

    private void readUs(File file) {
        List<DateFormatData> list =
            EasyExcel.read(file, DateFormatData.class, null).locale(Locale.US).sheet().doReadSync();
        for (DateFormatData data : list) {
            if (data.getDateStringUs() != null && !data.getDateStringUs().equals(data.getDate())) {
                LogUtils.info("date:us:{},{}", data.getDateStringUs(), data.getDate());
            }
            if (data.getNumberStringUs() != null && !data.getNumberStringUs().equals(data.getNumber())) {
                LogUtils.info("number:us{},{}", data.getNumberStringUs(), data.getNumber());
            }
        }
        for (DateFormatData data : list) {
            Assert.assertEquals(data.getDateStringUs(), data.getDate());
            Assert.assertEquals(data.getNumberStringUs(), data.getNumber());
        }
    }
}
