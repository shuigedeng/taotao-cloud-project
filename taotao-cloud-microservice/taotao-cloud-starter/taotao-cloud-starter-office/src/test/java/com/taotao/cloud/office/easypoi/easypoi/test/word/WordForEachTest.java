package com.taotao.cloud.office.easypoi.easypoi.test.word;

import cn.afterturn.easypoi.entity.ImageEntity;
import cn.afterturn.easypoi.test.word.entity.Person;
import cn.afterturn.easypoi.word.WordExportUtil;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author jueyue on 20-8-21.
 */
public class WordForEachTest {

    private static SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd");

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
        image.setLocationType(ImageEntity.ABOVE);
        map.put("testCode", image);

        List<ImageEntity> listImage = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            image = new ImageEntity();
            image.setHeight(40);
            image.setWidth(100);
            image.setUrl("https://pic.xlhall.com/FnVdGOVJiIYVPImX4-KKe1Q_wUQ2?tdsourcetag=s_pctim_aiomsg");
            image.setType(ImageEntity.URL);
            listImage.add(image);
        }
        map.put("imglist",listImage);

        XWPFDocument doc = WordExportUtil.exportWord07(
                "word/imgfor.docx", map);
        FileOutputStream fos = new FileOutputStream("D:/home/excel/imgfor.docx");
        doc.write(fos);
        fos.close();
    }
}
