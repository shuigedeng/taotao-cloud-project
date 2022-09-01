package com.taotao.cloud.office.easypoi.easypoi.test.excel.export;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cn.afterturn.easypoi.test.entity.MsgClient;
import cn.afterturn.easypoi.test.entity.MsgClientGroup;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 当行大数据量测试
 *
 * @author JueYue
 * 2014年12月13日 下午3:42:57
 */
public class ExcelExportMsgClient {

    @Test
    public void test() throws Exception {

        Field[] fields = MsgClient.class.getFields();
        for (int i = 0; i < fields.length; i++) {
            Excel excel = fields[i].getAnnotation(Excel.class);
            System.out.println(excel);
        }

        List<MsgClient> list = new ArrayList<MsgClient>();
        for (int i = 0; i < 100; i++) {
            MsgClient client = new MsgClient();
            client.setBirthday(new Date());
            client.setClientName("小明" + i);
            client.setClientPhone("18797" + i);
            client.setCreateBy("JueYue");
            client.setId("1" + i);
            client.setRemark("测试" + i);
            MsgClientGroup group = new MsgClientGroup();
            group.setGroupName("测试" + i);
            client.setGroup(group);
            list.add(client);
        }
        Date         start  = new Date();
        ExportParams params = new ExportParams("2412312", "测试", ExcelType.XSSF);
        params.setFreezeCol(2);
        Workbook workbook = ExcelExportUtil.exportExcel(params, MsgClient.class, list);
        System.out.println(new Date().getTime() - start.getTime());
        File savefile = new File("D:/home/excel/");
        if (!savefile.exists()) {
            savefile.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream("D:/home/excel/ExcelExportMsgClient.xlsx");
        workbook.write(fos);
        fos.close();
    }

    @Test
    public void testExcelExportEntity() throws Exception {

        List<ExcelExportEntity> entity = new ArrayList<ExcelExportEntity>();
        entity.add(new ExcelExportEntity("姓名", "clientName"));
        entity.add(new ExcelExportEntity("手机", "clientPhone"));
        entity.add(new ExcelExportEntity("分组", "group.groupName"));

        List<MsgClient> list = new ArrayList<MsgClient>();
        for (int i = 0; i < 100; i++) {
            MsgClient client = new MsgClient();
            client.setBirthday(new Date());
            client.setClientName("小明" + i);
            client.setClientPhone("18797" + i);
            client.setCreateBy("JueYue");
            client.setId("1" + i);
            client.setRemark("测试" + i);
            MsgClientGroup group = new MsgClientGroup();
            group.setGroupName("测试" + i);
            client.setGroup(group);
            list.add(client);
        }
        Date         start    = new Date();
        ExportParams params   = new ExportParams("自由导出对象版本", "测试", ExcelType.XSSF);
        Workbook     workbook = ExcelExportUtil.exportExcel(params, entity, list);
        System.out.println(new Date().getTime() - start.getTime());
        File savefile = new File("D:/home/excel/");
        if (!savefile.exists()) {
            savefile.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream("D:/home/excel/ExcelExportMsgClient.testExcelExportEntity.xlsx");
        workbook.write(fos);
        fos.close();
    }


}
