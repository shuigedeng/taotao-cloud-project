package com.taotao.cloud.office.rayin.rayin;

import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import ink.rayin.htmladapter.base.PdfGenerator;
import ink.rayin.htmladapter.base.Signature;
import ink.rayin.htmladapter.base.model.tplconfig.MarkInfo;
import ink.rayin.htmladapter.base.model.tplconfig.RayinMeta;
import ink.rayin.htmladapter.base.model.tplconfig.SignatureProperty;
import ink.rayin.htmladapter.base.utils.JsonSchemaValidator;
import ink.rayin.htmladapter.openhtmltopdf.service.PdfBoxGenerator;
import ink.rayin.htmladapter.openhtmltopdf.service.PdfBoxSignature;
import ink.rayin.tools.utils.ResourceUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Openhtmltopdf适配生成器测试类
 * Openhtmltopdf adapter generator test class
 */

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PdfBoxGeneratorOpenhtmltopdfExample {
    static PdfGenerator pdfGenerator;
    Signature pdfSign = new PdfBoxSignature();
    static  {
        try {
            pdfGenerator = new PdfBoxGenerator();
            pdfGenerator.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * example01
     * 单个构件生成测试
     * single element generate test
     */
    @Test
    @Order(1)
    public void exp01ElementGenerateTest() throws Exception {
        String outputFileClass = ResourceUtil.getResourceAbsolutePathByClassPath("");

        // 生成pdf路径
        // generate pdf path
        String outputFile = new File(outputFileClass)
                .getParentFile().getParent()
                + "/tmp/"
                + "example01_openhtmltopdf_"+System.currentTimeMillis() + ".pdf";

        StopWatch watch = StopWatch.createStarted();
        //数据参数可以为空
        pdfGenerator.generatePdfFileByHtmlAndData(ResourceUtil.getResourceAbsolutePathByClassPath("examples/example1/element1.html"),null,outputFile);
        watch.stop();
        log.info("exp01ElementGenerateTest duration：" +  watch.getTime() + "ms");
    }

    /**
     * example02
     * 简单的模板生成测试
     * simple template generate test
     */
    @Test
    @Order(2)
    public void exp02SimpleTemplateGenerateTest() throws Exception {
       String outputFileClass = ResourceUtil.getResourceAbsolutePathByClassPath("");

        // 生成pdf路径
        // generate pdf path
        String outputFile = new File(outputFileClass)
                .getParentFile().getParent()
                + "/tmp/"
                + "example02_openhtmltopdf_"+System.currentTimeMillis() + ".pdf";

        StopWatch watch = StopWatch.createStarted();
        pdfGenerator.generatePdfFileByTplConfigFile(ResourceUtil.getResourceAbsolutePathByClassPath("examples/example2/tpl.json"),null,outputFile);
        watch.stop();
        log.info("exp02SimpleTemplateGenerateTest duration：" +  watch.getTime() + "ms");
    }

    /**
     * example03
     * 单个构件绑定数据生成测试
     * single element bind data generate test
     */
    @Test
    @Order(3)
    public void exp03ElementBindDataGenerateTest() throws Exception {
        String jsonDataFilePath = ResourceUtil.getResourceAbsolutePathByClassPath("examples/example3/data.json");
        JsonNode jsonDataNode = JsonSchemaValidator.getJsonNodeFromFile(jsonDataFilePath);

        //依据单个构建配置生成PDF
        //generate pdf by element
        JSONObject jsonData = JSONObject.parseObject(jsonDataNode.toString());

        String outputFileClass = ResourceUtil.getResourceAbsolutePathByClassPath("");

        // 生成pdf路径
        // generate pdf path
        String outputFile = new File(outputFileClass)
                .getParentFile().getParent()
                + "/tmp/"
                + "example03_openhtmltopdf_"+System.currentTimeMillis() + ".pdf";

        StopWatch watch = StopWatch.createStarted();
        pdfGenerator.generatePdfFileByHtmlAndData(ResourceUtil.getResourceAbsolutePathByClassPath("examples/example3/element1.html"),jsonData,outputFile);
        watch.stop();
        log.info("exp03ElementBindDataGenerateTest duration：" +  watch.getTime() + "ms");
    }

    /**
     * example04 绑定数据的模板生成测试
     * template bind data generate test
     */
    @Test
    @Order(4)
    public void exp04TemplateBindDataGenerateTest() throws Exception {

        String jsonDataFilePath = ResourceUtil.getResourceAbsolutePathByClassPath("examples/example4/data.json");
        JsonNode jsonDataNode = JsonSchemaValidator.getJsonNodeFromFile(jsonDataFilePath);

        //依据构建配置生成PDF
        JSONObject jsonData = JSONObject.parseObject(jsonDataNode.toString());

        String outputFileClass = ResourceUtil.getResourceAbsolutePathByClassPath("");

        // 生成pdf路径
        String outputFile = new File(outputFileClass)
                .getParentFile().getParent()
                + "/tmp/"
                + "example04_openhtmltopdf_"+System.currentTimeMillis() + ".pdf";

        StopWatch watch = StopWatch.createStarted();
        pdfGenerator.generatePdfFileByTplConfigFile(ResourceUtil.getResourceAbsolutePathByClassPath("examples/example4/tpl.json"),jsonData,outputFile);
        watch.stop();
        log.info("exp04TemplateBindDataGenerateTest duration：" +  watch.getTime() + "ms");
    }

    /**
     * example05
     * 复杂构件绑定数据生成测试
     * single element bind data generate test
     */
    @Test
    @Order(5)
    public void exp05ComplexElementBindDataGenerateTest() throws Exception {
        String jsonDataFilePath = ResourceUtil.getResourceAbsolutePathByClassPath("examples/example5/data.json");
        JsonNode jsonDataNode = JsonSchemaValidator.getJsonNodeFromFile(jsonDataFilePath);

        //依据单个构建配置生成PDF
        //generate pdf by element
        JSONObject jsonData = JSONObject.parseObject(jsonDataNode.toString());

        String outputFileClass = ResourceUtil.getResourceAbsolutePathByClassPath("");

        // 生成pdf路径
        // generate pdf path
        String outputFile = new File(outputFileClass)
                .getParentFile().getParent()
                + "/tmp/"
                + "example05_openhtmltopdf_"+System.currentTimeMillis() + ".pdf";

        StopWatch watch = StopWatch.createStarted();
        pdfGenerator.generatePdfFileByHtmlAndData(ResourceUtil.getResourceAbsolutePathByClassPath("examples/example5/element1.html"),jsonData,outputFile);
        watch.stop();
        log.info("exp05ComplexElementBindDataGenerateTest duration：" +  watch.getTime() + "ms");
    }

    /**
     * example06
     * 复杂模板配置生成测试
     * complex template generate test
     */
    @Test
    @Order(6)
    public void exp06ComplexTemplateGenerateTest() throws Exception {
        String jsonDataFilePath = ResourceUtil.getResourceAbsolutePathByClassPath("examples/example6/data.json");
        JsonNode jsonDataNode = JsonSchemaValidator.getJsonNodeFromFile(jsonDataFilePath);

        //依据构建配置生成PDF
        JSONObject jsonData = JSONObject.parseObject(jsonDataNode.toString());

        String outputFileClass = ResourceUtil.getResourceAbsolutePathByClassPath("");

        // 生成pdf路径
        String outputFile = new File(outputFileClass)
                .getParentFile().getParent()
                + "/tmp/"
                + "example06_openhtmltopdf_"+System.currentTimeMillis() + ".pdf";

        StopWatch watch = StopWatch.createStarted();
        pdfGenerator.generatePdfFileByTplConfigFile(ResourceUtil.getResourceAbsolutePathByClassPath("examples/example6/tpl.json"),jsonData,outputFile);
        watch.stop();
        log.info("exp06ComplexTemplateGenerateTest duration：" +  watch.getTime() + "ms");
    }

    /**
     * example07
     * 特殊标签测试
     * special tag generate test
     */
    @Test
    @Order(7)
    public void exp07SpecialTagGenerateTest() throws Exception {
        log.info("exp07SpecialTagGenerateTest start time：" + new Timestamp(System.currentTimeMillis()));

        String jsonDataFilePath = ResourceUtil.getResourceAbsolutePathByClassPath("examples/example7/data.json");
        JsonNode jsonDataNode = JsonSchemaValidator.getJsonNodeFromFile(jsonDataFilePath);

        //依据单个构建配置生成PDF
        //generate pdf by element
        JSONObject jsonData = JSONObject.parseObject(jsonDataNode.toString());

        String outputFileClass = ResourceUtil.getResourceAbsolutePathByClassPath("");

        // 生成pdf路径
        // generate pdf path
        String outputFile = new File(outputFileClass)
                .getParentFile().getParent()
                + "/tmp/"
                + "example07_openhtmltopdf_"+System.currentTimeMillis() + ".pdf";

        StopWatch watch = StopWatch.createStarted();
        pdfGenerator.generatePdfFileByTplConfigFile(ResourceUtil.getResourceAbsolutePathByClassPath("examples/example7/tpl.json"),jsonData,outputFile);
        watch.stop();
        log.info("exp07SpecialTagGenerateTest duration：" +  watch.getTime() + "ms");
    }


    /**
     * example8
     * 签章
     * get page info test
     */
    @Test
    @Order(8)
    public void exp08SignTest() throws Exception {
        log.info("exp08SignTest start time：" + new Timestamp(System.currentTimeMillis()));

        String outputFileClass = ResourceUtil.getResourceAbsolutePathByClassPath("");
        String jsonDataFilePath = ResourceUtil.getResourceAbsolutePathByClassPath("examples/example6/data.json");
        JsonNode jsonDataNode = JsonSchemaValidator.getJsonNodeFromFile(jsonDataFilePath);

        //依据构建配置生成PDF
        JSONObject jsonData = JSONObject.parseObject(jsonDataNode.toString());

        // 生成pdf路径
        String outputFile = new File(outputFileClass)
                .getParentFile().getParent()
                + "/tmp/"
                + "example08_openhtmltopdf_"+System.currentTimeMillis() + ".pdf";

        StopWatch watch = StopWatch.createStarted();
        pdfGenerator.generatePdfFileByTplConfigFile(ResourceUtil.getResourceAbsolutePathByClassPath("examples/example6/tpl.json"),jsonData,outputFile);

        String pageInfoJsonStr = pdfGenerator.pdfPageInfoRead(ResourceUtil.getResourceAsStream(outputFile));
        log.info(pageInfoJsonStr);
        Gson gson = new Gson();
        RayinMeta rayinMeta = gson.fromJson(pageInfoJsonStr, RayinMeta.class);
        List<MarkInfo> markInfoList = rayinMeta.getMarkInfos();
        List<SignatureProperty> spl = new ArrayList();
        for(MarkInfo m:markInfoList){
            if(m.getKeyword().equals("sign54321")){
                SignatureProperty s = new SignatureProperty();
                s.setPageNum(m.getPageNum());
                s.setX(m.getX());
                s.setY(m.getY());
                s.setWidth(m.getWidth());
                s.setHeight(m.getHeight());
                s.setSignatureImage("examples/example8/rayinsign.gif");
                spl.add(s);
            }
        }

        pdfSign.multipleSign("123456", "examples/example8/p12sign.p12", "examples/example8/example6.pdf",
                outputFile,spl);
        watch.stop();

        log.info("exp08SignTest duration：" +  watch.getTime() + "ms");
    }


    /**
     * example09
     * 字体生成测试
     * single element generate test
     */
    @Test
    @Order(9)
    public void exp09FontsGenerateTest() throws Exception {
        log.info("exp09FontsGenerateTest start time：" + new Timestamp(System.currentTimeMillis()));

        String outputFileClass = ResourceUtil.getResourceAbsolutePathByClassPath("");

        // 生成pdf路径
        // generate pdf path
        String outputFile = new File(outputFileClass)
                .getParentFile().getParent()
                + "/tmp/"
                + "example09_openhtmltopdf_"+System.currentTimeMillis() + ".pdf";

        StopWatch watch = StopWatch.createStarted();
        //数据参数可以为空
        pdfGenerator.generatePdfFileByHtmlAndData(ResourceUtil.getResourceAbsolutePathByClassPath("examples/example9/element1.html"),null,outputFile);
        watch.stop();
        log.info("exp09FontsGenerateTest duration：" +  watch.getTime() + "ms");
    }
}
