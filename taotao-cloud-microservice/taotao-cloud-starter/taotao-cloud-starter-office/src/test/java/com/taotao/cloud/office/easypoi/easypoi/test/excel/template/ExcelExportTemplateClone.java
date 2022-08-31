package com.taotao.cloud.office.easypoi.easypoi.test.excel.template;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelStyleType;
import cn.afterturn.easypoi.test.entity.CourseEntity;
import cn.afterturn.easypoi.test.entity.StudentEntity;
import cn.afterturn.easypoi.test.entity.TeacherEntity;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.util.*;

import static cn.afterturn.easypoi.excel.ExcelExportUtil.SHEET_NAME;

/**
 * @author by jueyue on 19-6-11.
 */
public class ExcelExportTemplateClone {

    List<CourseEntity> list = new ArrayList<CourseEntity>();
    CourseEntity       courseEntity;

    @Test
    public void cloneTest() throws Exception {
        TemplateExportParams params = new TemplateExportParams(
                "doc/exportTemp.xls", true);
        params.setHeadingRows(2);
        params.setHeadingStartRow(2);
        params.setStyle(ExcelStyleType.BORDER.getClazz());
        List<Map<String, Object>> numOneList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            //sheet 1
            map.put("year", "2013" + i);
            map.put("sunCourses", list.size());
            Map<String, Object> obj = new HashMap<String, Object>();
            map.put("obj", obj);
            obj.put("name", list.size());
            // sheet 2
            map.put("month", 10);
            Map<String, Object> temp;
            for (int j = 1; j < 8; j++) {
                temp = new HashMap<String, Object>();
                temp.put("per", j * 10 + "---" + i);
                temp.put("mon", j * 1000);
                temp.put("summon", j * 10000);
                map.put("i" + j, temp);
            }
            map.put(SHEET_NAME, "啊啊测试SHeet" + i);
            numOneList.add(map);
        }


        List<Map<String, Object>> numTowList = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            Map<String, Object> oneMap = new HashMap<String, Object>();
            oneMap.put("list", list);
            oneMap.put(SHEET_NAME, "第二个测试SHeet" + i);
            numTowList.add(oneMap);
        }


        Map<Integer, List<Map<String, Object>>> realMap = new HashMap<>();
        realMap.put(0, numOneList);
        realMap.put(1, numTowList);

        Workbook book     = ExcelExportUtil.exportExcelClone(realMap, params);
        File     savefile = new File("D:/home/excel/");
        if (!savefile.exists()) {
            savefile.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream("D:/home/excel/exportCloneTemp.xls");
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
