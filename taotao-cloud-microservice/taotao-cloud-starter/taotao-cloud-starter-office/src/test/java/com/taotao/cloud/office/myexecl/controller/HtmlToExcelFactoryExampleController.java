package com.taotao.cloud.office.myexecl.controller;

import com.github.liaochong.myexcel.core.HtmlToExcelFactory;
import com.github.liaochong.myexcel.utils.AttachmentExportUtil;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HtmlToExcelFactoryExampleController {

    @GetMapping("/htmlToExcel/example")
    public void htmlToExcel(HttpServletResponse response) throws Exception {
        // get html file
        URL htmlToExcelEampleURL = this.getClass().getResource("/templates/htmlToExcelExample.html");
        Path path = Paths.get(htmlToExcelEampleURL.toURI());

        // read the html file and use default excel style to create excel
        Workbook workbook = HtmlToExcelFactory.readHtml(path.toFile()).build();

        // this is a example,you can write the workbook to any valid outputstream
        AttachmentExportUtil.export(workbook, "转换示例", response);
    }

    @GetMapping("/htmlToExcel/encrypt/example")
    public void htmlToExcelOfEncrypt(HttpServletResponse response) throws Exception {
        // get html file
        URL htmlToExcelEampleURL = this.getClass().getResource("/templates/htmlToExcelExample.html");
        Path path = Paths.get(htmlToExcelEampleURL.toURI());

        // read the html file and use default excel style to create excel
        Workbook workbook = HtmlToExcelFactory.readHtml(path.toFile()).build();

        // this is a example,you can write the workbook to any valid outputstream
        AttachmentExportUtil.encryptExport(workbook, "转换示例", response, "123456");
    }

}
