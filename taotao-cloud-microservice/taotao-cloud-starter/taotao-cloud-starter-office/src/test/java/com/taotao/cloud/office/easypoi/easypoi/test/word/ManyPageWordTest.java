package com.taotao.cloud.office.easypoi.easypoi.test.word;

import cn.afterturn.easypoi.word.WordExportUtil;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author by jueyue on 19-5-27.
 */
public class ManyPageWordTest {

    @Test
    public  void testPage() {
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", "小明" + i);
            list.add(map);
        }
        //----------------------------------------------
        try {
            XWPFDocument doc = WordExportUtil
                    .exportWord07("word/loan.docx", list);
            FileOutputStream fos = new FileOutputStream("D:/home/excel/ManyPageWordTest.拼接多页测试.docx");
            doc.write(fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
