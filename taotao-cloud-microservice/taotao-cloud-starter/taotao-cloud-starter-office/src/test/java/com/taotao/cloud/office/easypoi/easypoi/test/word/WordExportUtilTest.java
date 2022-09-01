package com.taotao.cloud.office.easypoi.easypoi.test.word;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.afterturn.easypoi.entity.ImageEntity;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.junit.jupiter.api.Test;

import cn.afterturn.easypoi.word.WordExportUtil;

public class WordExportUtilTest {

    private static SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd");

    /**
     * 简单导出包含图片
     */
    @Test
    public void imageWordExport() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("department", "");
        map.put("person", "JueYue");
        map.put("time", format.format(new Date()));
        ImageEntity image = new ImageEntity();
        image.setHeight(200);
        image.setWidth(500);
        image.setUrl("imgs/word/testCode.png");
        image.setType(ImageEntity.URL);
        map.put("testCode", image);
        try {
            XWPFDocument doc = WordExportUtil.exportWord07(
                    "word/Image.docx", map);
            FileOutputStream fos = new FileOutputStream("D:/home/excel/image.docx");
            doc.write(fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 简单导出没有图片和Excel
     */
    @Test
    public void SimpleWordExport() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("department", "Easypoi");
        map.put("person", "JueYue");
        map.put("time", format.format(new Date()));
        map.put("me","JueYue");
        map.put("date", "2015-01-03");
        map.put("mark", "0x0051");
        try {
            XWPFDocument doc = WordExportUtil.exportWord07(
                    "word/Simple.docx", map);
            FileOutputStream fos = new FileOutputStream("D:/home/excel/simple.docx");
            doc.write(fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
