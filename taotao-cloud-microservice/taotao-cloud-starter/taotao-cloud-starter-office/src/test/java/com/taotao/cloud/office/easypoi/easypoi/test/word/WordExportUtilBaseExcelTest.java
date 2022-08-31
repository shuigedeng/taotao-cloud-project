package com.taotao.cloud.office.easypoi.easypoi.test.word;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import cn.afterturn.easypoi.entity.ImageEntity;
import cn.afterturn.easypoi.test.word.entity.Person;
import cn.afterturn.easypoi.word.WordExportUtil;

import org.junit.Test;

public class WordExportUtilBaseExcelTest {

    private static SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd");

    /**
     * 简单导出包含图片
     */
    @Test
    public void imageWordExport() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("department", "Easypoi");
        map.put("time", format.format(new Date()));
        Person person = new Person();
        person.setName("JueYue");
        map.put("p", person);
        ImageEntity image = new ImageEntity();
        image.setHeight(200);
        image.setWidth(500);
        image.setUrl("imgs/word/testCode.png");
        image.setType(ImageEntity.URL);
        map.put("testCode", image);
        XWPFDocument doc = WordExportUtil.exportWord07(
                "word/Image.docx", map);
        FileOutputStream fos = new FileOutputStream("D:/home/excel/image.docx");
        doc.write(fos);
        fos.close();
    }

    /**
     * 简单导出没有图片和Excel
     */
    @Test
    public void SimpleWordExport() throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("department", "Easypoi");
        map.put("person", "JueYue");
        map.put("auditPerson", "JueYue");
        map.put("time", format.format(new Date()));
        map.put("date", new Date());
        ImageEntity image = new ImageEntity();
        image.setHeight(30);
        image.setWidth(80);
        image.setUrl("imgs/word/signName.png");
        image.setType(ImageEntity.URL);
        List<Person> list = new ArrayList<Person>();
        Person p = new Person();
        p.setName("小明");
        p.setTel("18711111111");
        p.setEmail("18711111111@139.com");
        p.setSign(image);
        list.add(p);
        p = new Person();
        p.setName("小红");
        p.setTel("18711111112");
        p.setEmail("18711111112@139.com");
        p.setSign(image);
        list.add(p);
        map.put("pList", list);
        XWPFDocument doc = WordExportUtil.exportWord07(
                "word/SimpleExcel.docx", map);
        FileOutputStream fos = new FileOutputStream("D:/home/excel/basesimpleExcel.docx");
        doc.write(fos);
        fos.close();
    }

}
