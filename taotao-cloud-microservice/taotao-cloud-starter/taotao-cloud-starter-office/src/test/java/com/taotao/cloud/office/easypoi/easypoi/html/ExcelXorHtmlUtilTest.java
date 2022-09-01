package com.taotao.cloud.office.easypoi.easypoi.html;

import cn.afterturn.easypoi.excel.ExcelXorHtmlUtil;
import cn.afterturn.easypoi.excel.entity.ExcelToHtmlParams;
import com.taotao.cloud.office.easypoi.easypoi.test.excel.read.FileUtilTest;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;

public class ExcelXorHtmlUtilTest {

    @Test
    public void testToTableHtmlWorkbook() throws Exception {
        Workbook wb = new HSSFWorkbook(new FileInputStream(new File(FileUtilTest
                .getWebRootPath("WEB-INF/doc/专项支出用款申请书.xls"))));
        String     html = ExcelXorHtmlUtil.toTableHtml(wb);
        FileWriter fw   = new FileWriter("D:/home/excel/专项支出用款申请书_table.html");
        fw.write(html);
        fw.close();
    }

    @Test
    public void testToTableHtmlWorkbookRowNum() throws Exception {
        Workbook wb = new HSSFWorkbook(new FileInputStream(new File(FileUtilTest
                .getWebRootPath("WEB-INF/doc/专项支出用款申请书.xls"))));
        ExcelToHtmlParams params = new ExcelToHtmlParams(wb, false, 0, null);
        String            html   = ExcelXorHtmlUtil.excelToHtml(params);
        FileWriter        fw     = new FileWriter("D:/home/excel/专项支出用款申请书_table_RowNum.html");
        fw.write(html);
        fw.close();
    }

    @Test
    public void testToTableHtmlWorkbookInt() throws Exception {
        Workbook wb = new HSSFWorkbook(new FileInputStream(new File(FileUtilTest
                .getWebRootPath("doc/exportTemp.xls"))));
        String     html = ExcelXorHtmlUtil.excelToHtml(new ExcelToHtmlParams(wb, 1));
        FileWriter fw   = new FileWriter("D:/home/excel/exportTemp_table.html");
        fw.write(html);
        fw.close();
    }

    @Test
    public void testToAllHtmlWorkbookAndImage() throws Exception {

        //Workbook wb = new HSSFWorkbook(new FileInputStream(new File("html/exportTemp_image.xls")));
        Workbook wb = new HSSFWorkbook(new FileInputStream(new File(
                FileUtilTest.getWebRootPath("html/exportTemp_image.xls"))));
        long       d    = System.nanoTime();
        String     html = ExcelXorHtmlUtil.excelToHtml(new ExcelToHtmlParams(wb, true, "yes"));
        FileWriter fw   = new FileWriter("D:/home/excel/exportTemp_image_all.html");
        fw.write(html);
        fw.close();

        System.err.println(System.nanoTime() - d);
        d = System.nanoTime();
        html = ExcelXorHtmlUtil.excelToHtml(new ExcelToHtmlParams(wb, true, "D:/home/excel/"));
        fw = new FileWriter("D:/home/excel/exportTemp_image_all_cache.html");
        fw.write(html);
        fw.close();
        System.err.println(System.nanoTime() - d);
    }

    @Test
    public void testToAllHtmlWorkbook() throws Exception {
        Workbook wb = new HSSFWorkbook(new FileInputStream(new File(FileUtilTest
                .getWebRootPath("WEB-INF/doc/专项支出用款申请书.xls"))));
        //            Workbook wb = new HSSFWorkbook(new FileInputStream(
        //                new File(
        //                    PoiPublicUtil
        //                    .getWebRootPath("doc/专项支出用款申请书.xls"))));
        String     html = ExcelXorHtmlUtil.toAllHtml(wb);
        FileWriter fw   = new FileWriter("D:/home/excel/专项支出用款申请书_all.html");
        fw.write(html);
        fw.close();
    }

    @Test
    public void testToAllHtmlWorkbookInt() throws Exception {
        Workbook wb = new HSSFWorkbook(new FileInputStream(new File(FileUtilTest
                .getWebRootPath("doc/exportTemp.xls"))));
        String     html = ExcelXorHtmlUtil.excelToHtml(new ExcelToHtmlParams(wb, true, 1, null));
        FileWriter fw   = new FileWriter("D:/home/excel/exportTemp_all.html");
        fw.write(html);
        fw.close();
    }

}
