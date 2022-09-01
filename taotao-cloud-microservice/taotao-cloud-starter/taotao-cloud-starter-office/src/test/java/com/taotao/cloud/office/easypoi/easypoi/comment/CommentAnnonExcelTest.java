package com.taotao.cloud.office.easypoi.easypoi.comment;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.handler.inter.ICommentHandler;
import com.taotao.cloud.office.easypoi.test.entity.temp.NumEntity;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 注解,excel 带有批注
 */
public class CommentAnnonExcelTest {

    @Test
    public void test() throws Exception {

        List<NumEntity> list = new ArrayList<NumEntity>();
        for (int i = 0; i < 20; i++) {
            NumEntity client = new NumEntity();
            client.setDouTest(i % 3 == 0 ? i + 0.0D : null);
            client.setIntTest((int) (Math.random() * 1000000000));
            client.setLongTest((long) (Math.random() * 100000000000000000L));
            client.setStrTest(Math.random() * 100000000000000000D + "");
            list.add(client);
        }
        Date start = new Date();
        ExportParams params = new ExportParams();
        params.setCommentHandler(new ICommentHandler() {
            @Override
            public String getComment(String name) {
                switch (name) {
                    case "str":
                        return "这是一个大的字符串";
                    case "int":
                        return "easypoi功能如同名字easy,主打的功能就是容易,让一个没见接触过poi的人员 就可以方便的写出Excel导出,Excel模板导出,Excel导入,Word模板导出,通过简单的注解和模板 语言(熟悉的表达式语法),完成以前复杂的写法";
                }
                return null;
            }

            @Override
            public String getAuthor() {
                return "悟耘开源";
            }

            @Override
            public String getComment(String name, Object val) {
                if (val == null) {
                    return null;
                }
                try {
                    if (Math.round(Double.valueOf(val.toString())) == 12) {
                        return "数值注解";
                    }
                } catch (NumberFormatException e) {
                }
                return null;
            }
        });
        Workbook workbook = ExcelExportUtil.exportExcel(params, NumEntity.class, list);
        System.out.println(new Date().getTime() - start.getTime());
        File savefile = new File("D:/home/excel/");
        if (!savefile.exists()) {
            savefile.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream("D:/home/excel/CommentAnnonExcelTest.test.xlsx");
        workbook.write(fos);
        fos.close();
    }
}
