package com.taotao.cloud.office.easypoi.tohtml;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Created by JueYue on 2017/10/9.
 */
@Controller
@RequestMapping("wps")
public class WpsToHtmlController {

    /**
     * 03 版本EXCEL预览
     */
    @RequestMapping("03")
    public String toHtmlOf03Base(HttpServletResponse response) throws IOException, InvalidFormatException {
        return "redirect:/easypoi-preview.html?filePath=" + "testExportTitleExcel.xls";
    }

    /**
     * 07 版本EXCEL预览
     */
    @RequestMapping("07")
    public String toHtmlOf07Base(HttpServletResponse response) throws IOException, InvalidFormatException {
        return "redirect:/easypoi-preview.html?filePath=" + "testExportTitleExcel.xlsx";
    }

    /**
     * 03 版本EXCEL预览
     */
    @RequestMapping("03img")
    public String toHtmlOf03Img(HttpServletResponse response) throws IOException, InvalidFormatException {
        return "redirect:/easypoi-preview.html?filePath=" + "exporttemp_img.xls";
    }

    /**
     * 07 版本EXCEL预览
     */
    @RequestMapping("07img")
    public String toHtmlOf07Img(HttpServletResponse response) throws IOException, InvalidFormatException {
        return "redirect:/easypoi-preview.html?filePath=" + "exportTemp_image.xlsx";
    }

    @RequestMapping("doc")
    public String doc(HttpServletResponse response) throws IOException, InvalidFormatException {
        return "redirect:/easypoi-preview.html?filePath=" + URLEncoder.encode("纳税信息.docx","UTF-8");
    }

    @RequestMapping("pdf")
    public String pdf(HttpServletResponse response) throws IOException, InvalidFormatException {
        return "redirect:/easypoi-preview.html?filePath=" + "loan.pdf";
    }


}
