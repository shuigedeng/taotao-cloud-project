package com.taotao.cloud.office.easypoi.easypoi.test.excel.template;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;

import cn.afterturn.easypoi.test.entity.CourseEntity;
import cn.afterturn.easypoi.test.entity.StudentEntity;
import cn.afterturn.easypoi.test.entity.TeacherEntity;
import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelStyleType;

import org.junit.Before;
import org.junit.Test;

/**
 * 多sheet导出
 * @author JueYue
 *   2015年8月15日 下午10:39:21
 */
public class TemplateExcelManySheet {

    List<CourseEntity> list = new ArrayList<CourseEntity>();
    CourseEntity       courseEntity;

    @Test
    public void manyMap() throws Exception {
        TemplateExportParams params = new TemplateExportParams(
            "doc/exportTemp.xls", 0,1);
        params.setHeadingRows(2);
        params.setHeadingStartRow(2);
        params.setStyle(ExcelStyleType.BORDER.getClazz());
        Map<Integer,Map<String,Object>> sheetMap = new HashMap<Integer, Map<String,Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        //sheet 1
        map.put("year", "2013");
        map.put("sunCourses", list.size());
        Map<String, Object> obj = new HashMap<String, Object>();
        map.put("obj", obj);
        obj.put("n"
        		+ "ame", list.size());
        
        // sheet 2
        map.put("month", 10);
        
        //第一个sheet Map的值put进去
        sheetMap.put(0, map);
        map = new HashMap<String, Object>();
        Map<String, Object> temp;
        for (int i = 1; i < 8; i++) {
            temp = new HashMap<String, Object>();
            temp.put("per", i * 10);
            temp.put("mon", i * 1000);
            temp.put("summon", i * 10000);
            map.put("i" + i, temp);
        }
      //第二个sheet Map的值put进去
        sheetMap.put(1, map);
        Workbook book = ExcelExportUtil.exportExcel(sheetMap,params);
        File savefile = new File("D:/home/excel/");
        if (!savefile.exists()) {
            savefile.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream("D:/home/excel/exportTemp3.xls");
        book.write(fos);
        fos.close();

    }

    @Before
    public void testBefore() {
        courseEntity = new CourseEntity();
        courseEntity.setId("1131");
        courseEntity.setName("小白");

        TeacherEntity teacherEntity = new TeacherEntity();
        teacherEntity.setId("12131231");
        teacherEntity.setName("你们");
        courseEntity.setChineseTeacher(teacherEntity);

        teacherEntity = new TeacherEntity();
        teacherEntity.setId("121312314312421131");
        teacherEntity.setName("老王");
        courseEntity.setMathTeacher(teacherEntity);

        StudentEntity studentEntity = new StudentEntity();
        studentEntity.setId("11231");
        studentEntity.setName("撒旦法司法局");
        studentEntity.setBirthday(new Date());
        studentEntity.setSex(1);
        List<StudentEntity> studentList = new ArrayList<StudentEntity>();
        studentList.add(studentEntity);
        studentList.add(studentEntity);
        courseEntity.setStudents(studentList);

        for (int i = 0; i < 3; i++) {
            list.add(courseEntity);
        }
    }

}
