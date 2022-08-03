package com.taotao.cloud.office.rayin.rayin;

import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import ink.rayin.htmladapter.base.PdfGenerator;
import ink.rayin.htmladapter.base.utils.JsonSchemaValidator;
import ink.rayin.htmladapter.openhtmltopdf.service.PdfBoxGenerator;
import ink.rayin.tools.utils.FileUtil;
import ink.rayin.tools.utils.ResourceUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

/**
 * Openhtmltopdf适配生成器行业模板样例
 * Openhtmltopdf adapter generator samples
 */
@Slf4j
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PdfBoxGeneratorOpenhtmltopdfSamples {
    static String DATA_FILE_NAME = "data.json";
    static String TPL_FILE_NAME = "tpl.json";
    static PdfGenerator pdfGenerator;
    static  {
        try {
            pdfGenerator = new PdfBoxGenerator();
            pdfGenerator.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * samples generate
     * 样例生成
     */
    @Test
    public void samplesGenerate() throws Exception {
        List<File> fileList = FileUtil.list(ResourceUtil.getResourceAbsolutePathByClassPath("samples"));
        fileList.forEach(f->{
            if(f.getName().equals(TPL_FILE_NAME)) {
                String jsonDataFilePath = null;
                try {
                    jsonDataFilePath = ResourceUtil.getResourceAbsolutePathByClassPath(f.getParent() + "/" + DATA_FILE_NAME);
                    JsonNode jsonDataNode = JsonSchemaValidator.getJsonNodeFromFile(jsonDataFilePath);
                    //依据单个构建配置生成PDF
                    //generate pdf by element
                    JSONObject jsonData = JSONObject.parseObject(jsonDataNode.toString());

                    String outputFile = "";
                    String outputFileClass = ResourceUtil.getResourceAbsolutePathByClassPath("");

                    // 生成pdf路径
                    // generate pdf path
                    outputFile = (outputFile == null || outputFile.equals("")) ? new File(outputFileClass)
                            .getParentFile().getParent()
                            + "/tmp/"
                            + f.getParentFile().getName() + "_sample_" + System.currentTimeMillis() + ".pdf" : outputFile;

                    //数据参数可以为空
                    pdfGenerator.generatePdfFileByTplConfigFile(f.getAbsolutePath(), jsonData, outputFile);

                    log.info("samplesGenerate ["+ f.getParentFile().getName() +  "] end time：" + new Timestamp(System.currentTimeMillis()));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });






    }

    
}
