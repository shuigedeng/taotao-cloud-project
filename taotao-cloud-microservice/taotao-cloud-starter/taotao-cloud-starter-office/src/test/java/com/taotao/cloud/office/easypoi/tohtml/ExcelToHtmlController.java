package com.taotao.cloud.office.easypoi.tohtml;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.WorkbookUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import cn.afterturn.easypoi.cache.manager.POICacheManager;
import cn.afterturn.easypoi.excel.ExcelXorHtmlUtil;
import cn.afterturn.easypoi.excel.entity.ExcelToHtmlParams;

/**
 * Created by JueYue on 2017/10/9.
 */
@Controller
@RequestMapping("tohtml")
public class ExcelToHtmlController {

    /**
     * 03 版本EXCEL预览
     */
    @RequestMapping("03")
    public void toHtmlOf03Base(HttpServletResponse response) throws IOException, InvalidFormatException {
        ExcelToHtmlParams params = new ExcelToHtmlParams(WorkbookFactory.create(POICacheManager.getFile("exceltohtml/testExportTitleExcel.xls")));
        response.getOutputStream().write(ExcelXorHtmlUtil.excelToHtml(params).getBytes());
    }
    /**
     * 07 版本EXCEL预览
     */
    @RequestMapping("07")
    public void toHtmlOf07Base(HttpServletResponse response) throws IOException, InvalidFormatException {
        ExcelToHtmlParams params = new ExcelToHtmlParams(WorkbookFactory.create(POICacheManager.getFile("exceltohtml/testExportTitleExcel.xlsx")));
        response.getOutputStream().write(ExcelXorHtmlUtil.excelToHtml(params).getBytes());
    }
    /**
     * 03 版本EXCEL预览
     */
    @RequestMapping("03img")
    public void toHtmlOf03Img(HttpServletResponse response) throws IOException, InvalidFormatException {
        ExcelToHtmlParams params = new ExcelToHtmlParams(WorkbookFactory.create(POICacheManager.getFile("exceltohtml/exporttemp_img.xls")),true,"yes");
        response.getOutputStream().write(ExcelXorHtmlUtil.excelToHtml(params).getBytes());
    }
    /**
     * 07 版本EXCEL预览
     */
    @RequestMapping("07img")
    public void toHtmlOf07Img(HttpServletResponse response) throws IOException, InvalidFormatException {
        ExcelToHtmlParams params = new ExcelToHtmlParams(WorkbookFactory.create(POICacheManager.getFile("exceltohtml/exportTemp_image.xlsx")),true,"yes");
        response.getOutputStream().write(ExcelXorHtmlUtil.excelToHtml(params).getBytes());
    }


}
