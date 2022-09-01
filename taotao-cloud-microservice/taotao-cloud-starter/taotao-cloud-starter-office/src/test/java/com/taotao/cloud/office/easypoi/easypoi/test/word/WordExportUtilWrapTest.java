package com.taotao.cloud.office.easypoi.easypoi.test.word;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import cn.afterturn.easypoi.test.word.entity.Person;
import cn.afterturn.easypoi.word.WordExportUtil;

import org.junit.jupiter.api.Test;

public class WordExportUtilWrapTest {

    private static SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd");

    

    /**
     * 简单导出没有图片和Excel
     */
    @Test
    public void SimpleWordExport() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("department", "Jee\r\ncg");
        map.put("person", "Jue\r\nYue");
        map.put("auditPerson", "JueYue");
        map.put("time", format.format(new Date()));
        map.put("date", new Date());
        List<Person> list = new ArrayList<Person>();
        Person p = new Person();
        p.setName("小明");
        p.setTel("18711111111");
        p.setEmail("18711111111@\\r\\n139.com");
        list.add(p);
        p = new Person();
        p.setName("小红");
        p.setTel("18711111112");
        p.setEmail("18711111112@\r\n139.com");
        list.add(p);
        map.put("pList", list);
        try {
            XWPFDocument doc = WordExportUtil.exportWord07(
                    "word/SimpleExcel.docx", map);
            FileOutputStream fos = new FileOutputStream("D:/home/excel/wrapExcel.docx");
            doc.write(fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
