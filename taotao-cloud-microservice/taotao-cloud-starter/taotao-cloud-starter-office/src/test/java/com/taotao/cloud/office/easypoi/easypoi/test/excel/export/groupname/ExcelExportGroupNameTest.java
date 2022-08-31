package com.taotao.cloud.office.easypoi.easypoi.test.excel.export.groupname;

import cn.afterturn.easypoi.test.entity.groupname.GroupExportVo;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.test.entity.groupname.GnEntity;
import cn.afterturn.easypoi.test.entity.groupname.GnStudentEntity;
import cn.afterturn.easypoi.test.entity.groupname.GroupNameEntity;

/**
 * Created by JueYue on 2017/10/2.
 */
public class ExcelExportGroupNameTest {

    @Test
    public void base() throws Exception {

        List<GroupNameEntity> list = new ArrayList<GroupNameEntity>();
        for (int i = 0; i < 10; i++) {
            GroupNameEntity client = new GroupNameEntity();
            client.setBirthday(new Date());
            client.setCreateBy("2017-10-01");
            client.setClientName("小明" + i);
            client.setClientPhone("18797" + i);
            client.setCreateBy("JueYue");
            client.setId("1" + i);
            client.setRemark("测试" + i);
            list.add(client);
        }
        Date start = new Date();
        ExportParams params = new ExportParams("GroupName测试", "测试", ExcelType.XSSF);
        Workbook workbook = ExcelExportUtil.exportExcel(params, GroupNameEntity.class, list);
        File savefile = new File("D:/home/excel/");
        if (!savefile.exists()) {
            savefile.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream("D:/home/excel/groupName.xlsx");
        workbook.write(fos);
        fos.close();
    }

    @Test
    public void entity() throws Exception {

        List<GnEntity> list = new ArrayList<GnEntity>();
        for (int i = 0; i < 10; i++) {
            GnEntity client = new GnEntity();
            client.setClientName("小明" + i);
            client.setClientPhone("18797" + i);
            GnStudentEntity studentEntity = new GnStudentEntity();
            studentEntity.setBirthday(new Date());
            studentEntity.setRegistrationDate(new Date());
            studentEntity.setName("JueYue" + i);
            studentEntity.setSex(i % 2);
            client.setStudentEntity(studentEntity);
            list.add(client);
        }
        Date start = new Date();
        ExportParams params = new ExportParams("GroupNameGnEntity测试", "测试", ExcelType.XSSF);
        Workbook workbook = ExcelExportUtil.exportExcel(params, GnEntity.class, list);
        File savefile = new File("D:/home/excel/");
        if (!savefile.exists()) {
            savefile.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream("D:/home/excel/groupName_GnEntity.xlsx");
        workbook.write(fos);
        fos.close();
    }


    @Test
    public void groupSort() throws Exception {
        List<GroupExportVo> list = new ArrayList<GroupExportVo>();
        ExportParams params = new ExportParams("Group排序", "测试", ExcelType.XSSF);
        Workbook workbook = ExcelExportUtil.exportExcel(params, GroupExportVo.class, list);
        File savefile = new File("D:/home/excel/");
        if (!savefile.exists()) {
            savefile.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream("D:/home/excel/groupName_groupSort.xlsx");
        workbook.write(fos);
        fos.close();
    }
}
