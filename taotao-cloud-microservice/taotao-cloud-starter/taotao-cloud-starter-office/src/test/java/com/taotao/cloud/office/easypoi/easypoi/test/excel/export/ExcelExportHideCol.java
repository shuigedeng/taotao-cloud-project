package com.taotao.cloud.office.easypoi.easypoi.test.excel.export;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.test.entity.StudentEntity;
import cn.afterturn.easypoi.test.entity.StudentHiderEntity;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 隐藏列测试
 * Created by jueyue on 18-1-26.
 */
public class ExcelExportHideCol {

    @Test
    public void testStudentList() throws Exception {
        Date start = new Date();
        StudentHiderEntity studentEntity = new StudentHiderEntity();
        studentEntity.setId("11231");
        studentEntity.setName("撒旦法司法局");
        studentEntity.setBirthday(new Date());
        studentEntity.setRegistrationDate(new Date());
        studentEntity.setSex(1);
        List<StudentHiderEntity> studentList = new ArrayList<StudentHiderEntity>();
        studentList.add(studentEntity);
        studentList.add(studentEntity);
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("计算机一班学生","学生"),
                StudentHiderEntity.class, studentList);
        System.out.println(new Date().getTime() - start.getTime());
        File savefile = new File("D:/home/excel/");
        if (!savefile.exists()) {
            savefile.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream("D:/home/excel/ExcelExportHideCol.xls");
        workbook.write(fos);
        fos.close();
    }

}
