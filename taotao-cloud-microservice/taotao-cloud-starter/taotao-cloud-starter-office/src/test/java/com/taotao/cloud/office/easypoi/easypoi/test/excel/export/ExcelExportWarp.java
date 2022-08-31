package com.taotao.cloud.office.easypoi.easypoi.test.excel.export;

import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.test.entity.MsgClient;

/**
 * Excel 换行导出
 * Created by JueYue on 2017/9/15.
 */
public class ExcelExportWarp {

    @Test
    public void test03() throws Exception {

        Field[] fields =  MsgClient.class.getFields();
        for (int i = 0; i < fields.length; i++) {
            Excel excel = fields[i].getAnnotation(Excel.class);
            System.out.println(excel);
        }

        List<MsgClient> list = new ArrayList<MsgClient>();
        for (int i = 0; i < 100; i++) {
            MsgClient client = new MsgClient();
            client.setBirthday(new Date());
            client.setClientName("名称:\n完美\n小明" + i);
            client.setClientPhone("18797" + i);
            client.setCreateBy("用户:\nJueYue");
            client.setId("1" + i);
            client.setRemark("测试" + i);
            list.add(client);
        }
        Date start = new Date();
        ExportParams params = new ExportParams("换行测试", "换行测试", ExcelType.HSSF);
        params.setFreezeCol(2);
        params.setHeight((short) -1);
        params.setReadonly(true);
        Workbook workbook = ExcelExportUtil.exportExcel(params, MsgClient.class, list);
        System.out.println(new Date().getTime() - start.getTime());
        File savefile = new File("D:/home/excel/");
        if (!savefile.exists()) {
            savefile.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream("D:/home/excel/ExcelExportWarp.xls");
        workbook.write(fos);
        fos.close();
    }

    @Test
    public void test07() throws Exception {

        Field[] fields =  MsgClient.class.getFields();
        for (int i = 0; i < fields.length; i++) {
            Excel excel = fields[i].getAnnotation(Excel.class);
            System.out.println(excel);
        }

        List<MsgClient> list = new ArrayList<MsgClient>();
        for (int i = 0; i < 100; i++) {
            MsgClient client = new MsgClient();
            client.setBirthday(new Date());
            client.setClientName("名称:\n完美\n小明" + i);
            client.setClientPhone("18797" + i);
            client.setCreateBy("用户:\nJueYue");
            client.setId("1" + i);
            client.setRemark("测试" + i);
            list.add(client);
        }
        Date start = new Date();
        ExportParams params = new ExportParams("换行测试", "换行测试", ExcelType.XSSF);
        params.setFreezeCol(2);
        params.setReadonly(true);
        Workbook workbook = ExcelExportUtil.exportExcel(params, MsgClient.class, list);
        System.out.println(new Date().getTime() - start.getTime());
        File savefile = new File("D:/home/excel/");
        if (!savefile.exists()) {
            savefile.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream("D:/home/excel/ExcelExportWarp.xlsx");
        workbook.write(fos);
        fos.close();
    }


    @Test
    public void testHeight03() throws Exception {

        Field[] fields =  MsgClient.class.getFields();
        for (int i = 0; i < fields.length; i++) {
            Excel excel = fields[i].getAnnotation(Excel.class);
            System.out.println(excel);
        }

        List<MsgClient> list = new ArrayList<MsgClient>();
        for (int i = 0; i < 100; i++) {
            MsgClient client = new MsgClient();
            client.setBirthday(new Date());
            client.setClientName("名称:完美小明sdsakjdaslkjdkjlsj kjsadlkjsadlkja jsaldkjsa lkjsadlkjsad " + i);
            client.setClientPhone("18797" + i);
            client.setCreateBy("用户:\nJueYue");
            client.setId("1" + i);
            client.setRemark("测试" + i);
            list.add(client);
        }
        Date start = new Date();
        ExportParams params = new ExportParams("换行测试", "换行测试", ExcelType.HSSF);
        params.setFreezeCol(2);
        params.setHeight((short) -1);
        params.setReadonly(true);
        Workbook workbook = ExcelExportUtil.exportExcel(params, MsgClient.class, list);
        System.out.println(new Date().getTime() - start.getTime());
        File savefile = new File("D:/home/excel/");
        if (!savefile.exists()) {
            savefile.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream("D:/home/excel/ExcelExportAutoHeight.xls");
        workbook.write(fos);
        fos.close();
    }
}
