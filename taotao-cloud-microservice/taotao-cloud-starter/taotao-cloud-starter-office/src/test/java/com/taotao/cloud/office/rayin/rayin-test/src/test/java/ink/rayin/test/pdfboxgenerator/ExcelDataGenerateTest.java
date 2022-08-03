package com.taotao.cloud.office.rayin.rayin;

import ink.rayin.htmladapter.base.PdfGenerator;
import ink.rayin.htmladapter.openhtmltopdf.service.PdfBoxGenerator;
import ink.rayin.tools.utils.EasyExcelUtils;
import ink.rayin.tools.utils.ResourceUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;

/**
 * 该类用于测试通过excel文件作为数据源生成pdf，即代替json数据
 * This class is used to test the generation of PDF through Excel files as data sources, that is, to replace JSON data
 *
 * @author Jonah Wang
 * @date 2022-07-24
 */
@Slf4j
public class ExcelDataGenerateTest {
    static PdfGenerator pdfGenerator;
    static  {
        try {
            pdfGenerator = new PdfBoxGenerator();
            pdfGenerator.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void readExcelData() throws IOException {
        EasyExcelUtils.readWithoutHead(ResourceUtil.getResourceAsStream("examples/example10/data.xlsx"));
    }

    @Test
    public void generateFilesByExcel() throws Exception {
        String outputFileClass = ResourceUtil.getResourceAbsolutePathByClassPath("");
        // 生成pdf路径
        // generate pdf path
        String outputDir = new File(outputFileClass).getParentFile().getParent() + "/tmp/";
        StopWatch watch = StopWatch.createStarted();
        pdfGenerator.generatePdfFilesByTplAndExcel(ResourceUtil.getResourceAsString("examples/example10/tpl.json", StandardCharsets.UTF_8),
                ResourceUtil.getResourceAsStream("examples/example10/data.xlsx"), outputDir,
                "example10_openhtmltopdf_" + System.currentTimeMillis());
        watch.stop();
        log.info("exp10generateFilesByExcelTest duration：" +  watch.getTime() + "ms");
    }

    @Test
    public void generateFileByExcel() throws Exception {
        log.info("exp10generateFileByExcelTest start time：" + new Timestamp(System.currentTimeMillis()));
        String outputFileClass = ResourceUtil.getResourceAbsolutePathByClassPath("");
        // 生成pdf路径
        // generate pdf path
        String outputFile = new File(outputFileClass)
                .getParentFile().getParent()
                + "/tmp/"
                + "example10_openhtmltopdf_" + System.currentTimeMillis() + ".pdf";

        StopWatch watch = StopWatch.createStarted();
        pdfGenerator.generatePdfFileByTplAndExcel(ResourceUtil.getResourceAsString("examples/example10/tpl.json", StandardCharsets.UTF_8),
                ResourceUtil.getResourceAsStream("examples/example10/data.xlsx"),outputFile);
        watch.stop();
        log.info("exp10generateFileByExcelTest duration：" +  watch.getTime() + "ms");
    }

}
