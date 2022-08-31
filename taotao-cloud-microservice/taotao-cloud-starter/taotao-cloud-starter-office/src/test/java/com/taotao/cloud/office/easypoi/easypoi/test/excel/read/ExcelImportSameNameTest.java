package com.taotao.cloud.office.easypoi.easypoi.test.excel.read;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.poi.ss.usermodel.Workbook;

import cn.afterturn.easypoi.test.entity.CourseEntity;
import cn.afterturn.easypoi.test.entity.MsgClient;
import cn.afterturn.easypoi.test.entity.StudentEntity;
import cn.afterturn.easypoi.test.entity.samename.ClassName;
import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.util.PoiPublicUtil;

import org.junit.Test;

/**
 * 同名列导入测试
 * @author JueYue
 *   2015年5月2日 上午11:19:40
 */
public class ExcelImportSameNameTest {

    @Test
    public void exportTest() {
        StudentEntity studentEntity = new StudentEntity();
        studentEntity.setId("11231");
        studentEntity.setName("撒旦法司法局");
        studentEntity.setBirthday(new Date());
        studentEntity.setRegistrationDate(new java.sql.Time(new Date().getTime()));
        studentEntity.setSex(1);
        List<StudentEntity> studentList = new ArrayList<StudentEntity>();
        List<StudentEntity> studentList2 = new ArrayList<StudentEntity>();
        studentList.add(studentEntity);
        studentList.add(studentEntity);
        studentList2.add(studentEntity);
        studentList2.add(studentEntity);
        studentList2.add(studentEntity);
        studentList2.add(studentEntity);

        List<ClassName> list = new ArrayList<ClassName>();
        ClassName classes = new ClassName();
        classes.setName("班级1");
        classes.setArrA(studentList2);
        classes.setArrB(studentList);
        list.add(classes);
        classes = new ClassName();
        classes.setName("班级2");
        classes.setArrA(studentList);
        classes.setArrB(studentList2);
        list.add(classes);
        ExportParams params = new ExportParams();
        try {
            Workbook workbook = ExcelExportUtil.exportExcel(params, ClassName.class, list);
            FileOutputStream fos = new FileOutputStream("D:/home/excel/sameName.xls");
            workbook.write(fos);
            fos.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void importTest() {
        ImportParams params = new ImportParams();
        params.setHeadRows(2);
        long start = new Date().getTime();
        List<ClassName> list = ExcelImportUtil.importExcel(
            new File(FileUtilTest.getWebRootPath("import/sameName.xls")), ClassName.class, params);
        System.out.println(new Date().getTime() - start);
        System.out.println(list.size());
        System.out.println(ReflectionToStringBuilder.toString(list.get(0)));
        System.out.println(ReflectionToStringBuilder.toString(list.get(0).getArrA().get(0)));

    }

}
