package com.taotao.cloud.office.easypoi.easypoi.test.excel.export;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.test.entity.CourseEntity;
import cn.afterturn.easypoi.test.entity.StudentEntity;
import cn.afterturn.easypoi.test.entity.TeacherEntity;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jue on 14-4-19.
 */
public class ExcelExportUtilAutoSizeTest {

    List<CourseEntity> list = new ArrayList<CourseEntity>();
    CourseEntity       courseEntity;

    /**
     * 25W行导出测试
     * 
     * @throws Exception
     */
    @Test
    public void autoSizeTest() throws Exception {

        ExportParams params = new ExportParams("2412312", "测试", ExcelType.XSSF);
        Date start = new Date();
        params.setAutoSize(true);
        Workbook workbook = ExcelExportUtil.exportExcel(params, CourseEntity.class, list);
        System.out.println(new Date().getTime() - start.getTime());
        File savefile = new File("D:/home/excel/");
        if (!savefile.exists()) {
            savefile.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream("D:/home/excel/autoSize.xlsx");
        workbook.write(fos);
        fos.close();
    }

    @Test
    public void autoSizeHssFTest() throws Exception {

        ExportParams params = new ExportParams("2412312", "测试", ExcelType.HSSF);
        Date start = new Date();
        Workbook workbook = ExcelExportUtil.exportExcel(params, CourseEntity.class, list);
        System.out.println(new Date().getTime() - start.getTime());
        File savefile = new File("D:/home/excel/");
        if (!savefile.exists()) {
            savefile.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream("D:/home/excel/autoSize.xls");
        //设置自动列宽
        for (int i = 0; i < 7; i++) {
            workbook.getSheetAt(0).autoSizeColumn(i,true);
            //workbook.getSheetAt(0).setColumnWidth(i, workbook.getSheetAt(0).getColumnWidth(i) * 35 / 10);
        }
        workbook.write(fos);
        fos.close();
    }

    @Before
    public void testBefore() {

        for (int i = 0; i < 2; i++) {
            courseEntity = new CourseEntity();
            courseEntity.setId("1131");
            courseEntity.setName("海贼王必2222222222222222222222222222222222222修(" + (i + 1) + ")");

            TeacherEntity teacherEntity = new TeacherEntity();
            teacherEntity.setId("12131231");
            teacherEntity.setName("路飞");
            courseEntity.setChineseTeacher(teacherEntity);

            teacherEntity = new TeacherEntity();
            teacherEntity.setId("121312314312421131");
            teacherEntity.setName("老王2222222222222222222222" +i);
            courseEntity.setMathTeacher(teacherEntity);

            StudentEntity studentEntity = new StudentEntity();
            studentEntity.setId("11231");
            studentEntity.setName("撒旦法司法局1111111111111111111111111");
            studentEntity.setBirthday(new Date());
            studentEntity.setSex(1);
            List<StudentEntity> studentList = new ArrayList<StudentEntity>();
            studentList.add(studentEntity);
            studentList.add(studentEntity);
            courseEntity.setStudents(studentList);
            list.add(courseEntity);
        }
    }

}
