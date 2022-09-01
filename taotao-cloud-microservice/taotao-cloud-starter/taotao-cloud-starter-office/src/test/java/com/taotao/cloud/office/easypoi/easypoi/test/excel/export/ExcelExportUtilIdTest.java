package com.taotao.cloud.office.easypoi.easypoi.test.excel.export;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.ss.usermodel.Workbook;

import cn.afterturn.easypoi.test.entity.CourseEntity;
import cn.afterturn.easypoi.test.entity.StudentEntity;
import cn.afterturn.easypoi.test.entity.TeacherEntity;
import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.export.styler.ExcelExportStylerBorderImpl;

import org.junit.Before;
import org.junit.jupiter.api.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 关系导出测试 Created by JueYue on 14-4-19.
 */
public class ExcelExportUtilIdTest {

    List<CourseEntity>  courseList   = new ArrayList<CourseEntity>();
    List<TeacherEntity> telist = new ArrayList<TeacherEntity>();
    List<StudentEntity> stuList=new ArrayList<StudentEntity>();
    List<Map<String,Object>> schoolCourseList=new ArrayList<Map<String,Object>>();
    CourseEntity        courseEntity;

    @Before
    public void testBefor2() {
        TeacherEntity teacherEntity = new TeacherEntity();
        teacherEntity.setId("12132131231231231");
        teacherEntity.setName("你们");

        for (int i = 0; i < 3; i++) {
            telist.add(teacherEntity);
        }
    }
    
    @Before
    public void initStuData () throws Exception{
    	stuList.clear();
    	StudentEntity stu1=new StudentEntity();
    	stu1.setId("stu_001");
    	stu1.setName("李雷");
    	stu1.setSex(1);
    	stu1.setBirthday(DateUtils.parseDate("1988-08-03","yyyy-MM-dd"));
    	stu1.setRegistrationDate(DateUtils.parseDate("2004-01-05","yyyy-MM-dd"));
    	StudentEntity stu2=new StudentEntity();
    	stu2.setId("stu_002");
    	stu2.setName("韩梅梅");
    	stu2.setSex(2);
    	stu2.setBirthday(DateUtils.parseDate("1990-04-16","yyyy-MM-dd"));
    	stu2.setRegistrationDate(DateUtils.parseDate("2005-03-09","yyyy-MM-dd"));
    	stuList.add(stu1);
    	stuList.add(stu2);
    	
    }

    @Before
    public void initCourseData() {
        CourseEntity c1 = new CourseEntity();
        c1.setId("c001");
        c1.setName("语文");

        TeacherEntity t1 = new TeacherEntity();
        t1.setId("t001");
        t1.setName("李白");
        TeacherEntity t2 = new TeacherEntity();
        t2.setId("t002");
        t2.setName("杜甫");
        c1.setChineseTeacher(t1);//主讲老师
        c1.setMathTeacher(t2);//代课老师

        StudentEntity s1 = new StudentEntity();
        s1.setId("s001");
        s1.setName("奥巴马");
        s1.setBirthday(new Date());
        s1.setRegistrationDate(new  java.sql.Time(new Date().getTime()));
        s1.setSex(1);
        
        StudentEntity s2 = new StudentEntity();
        s2.setId("s002");
        s2.setName("希拉里");
        s2.setBirthday(new Date());
        s2.setRegistrationDate(new  java.sql.Time(new Date().getTime()));
        s2.setSex(2);
        List<StudentEntity> studentList = new ArrayList<StudentEntity>();
        studentList.add(s1);
        studentList.add(s2);
        c1.setStudents(studentList);
        courseList.add(c1);
        
        CourseEntity c2 = new CourseEntity();
        c2.setId("c002");
        c2.setName("数学");
        
        TeacherEntity t3 = new TeacherEntity();
        t3.setId("t002");
        t3.setName("阿基米德");
        TeacherEntity t4 = new TeacherEntity();
        t4.setId("t004");
        t4.setName("华罗庚");
        c2.setChineseTeacher(t3);//主讲老师
        c2.setMathTeacher(t4);//代课老师
        
        StudentEntity s3 = new StudentEntity();
        s3.setId("s003");
        s3.setName("达芬奇");
        s3.setBirthday(new Date());
        s3.setRegistrationDate(new  java.sql.Time(new Date().getTime()));
        s3.setSex(1);
        
        StudentEntity s4 = new StudentEntity();
        s4.setId("s004");
        s4.setName("居里夫人");
        s4.setBirthday(new Date());
        s4.setRegistrationDate(new java.sql.Time(new Date().getTime()));
        s4.setSex(2);
        
        StudentEntity s5 = new StudentEntity();
        s5.setId("s005");
        s5.setName("牛顿");
        s5.setBirthday(new Date());
        s5.setRegistrationDate(new java.sql.Time(new Date().getTime()));
        s5.setSex(1);
        List<StudentEntity> studentList2 = new ArrayList<StudentEntity>();
        studentList2.add(s3);
        studentList2.add(s4);
        studentList2.add(s5);
        c2.setStudents(studentList2);
        courseList.add(c2);
        
        /*
         * 以下是为了多sheet导出准备数据
         */
        for(int i=1;i<=1;i++){
        	Map<String,Object> mapData=new HashMap<String, Object>();
            ExportParams params = new ExportParams("0328课程表", "日期：2016-03-28", "六年"+i+"班");
            params.setStyle(ExcelExportStylerBorderImpl.class);
        	mapData.put("title",params);
        	mapData.put("entity",CourseEntity.class);
        	mapData.put("data",courseList);
        	schoolCourseList.add(mapData);
        }
        
        
    }

    /**
     * 单sheet嵌套注解实体导出
     * @throws Exception
     */
    @Test
    public void testExportExcel_1() throws Exception {
        ExportParams params = new ExportParams("0328课程表", "日期：2016-03-28", "六年一班");
        params.setStyle(ExcelExportStylerBorderImpl.class);
        Workbook workbook = ExcelExportUtil.exportExcel(params, CourseEntity.class,courseList);
        FileOutputStream fos = new FileOutputStream("D:/home/excel/0328课程表.xls");
        workbook.write(fos);
        fos.close();
    }
    /**
     * 单sheet单注解实体导出
     * @throws Exception
     */
    @Test
    public void testExportExcel_2() throws Exception {
    	ExportParams params = new ExportParams("学生列表", "日期：2017-03-27","六年级一班");
    	params.setStyle(ExcelExportStylerBorderImpl.class);
    	Workbook workbook = ExcelExportUtil.exportExcel(params,StudentEntity.class,stuList);
    	FileOutputStream fos = new FileOutputStream("D:/home/excel/学生列表.xls");
    	workbook.write(fos);
    	fos.close();
    }
    
    /**
     * 基础模板导出
     * @throws Exception
     */
    @Test
    public void templateExportDemo_1() throws Exception {
        TemplateExportParams params = new TemplateExportParams(
            "doc/kyoffice_address_book.xls",1,2);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("companyName",";智能");
        data.put("editDate",new Date());
        List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
        Map<String,Object> emp1=Maps.newHashMap();
        emp1.put("deptId","0001");
        emp1.put("deptName","人事部");
        emp1.put("staffName","小红");
        emp1.put("gender",0);
        emp1.put("mobile","13876543210");
        emp1.put("email","xiaohong@kyoffice.com");
        emp1.put("workDate",new Date());
        emp1.put("salary",8000);
        emp1.put("remark","新人");
        
        Map<String,Object> emp2=Maps.newHashMap();
        emp2.put("deptId","0002");
        emp2.put("deptName","研发部");
        emp2.put("staffName","小明");
        emp2.put("gender",1);
        emp2.put("mobile","18670044463");
        emp2.put("email","xiaoming@kyoffice.com");
        emp2.put("workDate",new Date());
        emp2.put("salary",20000);
        emp2.put("remark","老司机");
        for (int i = 0; i < 4; i++) {
            listMap.add(emp1);
            listMap.add(emp2);
        }
        data.put("employeeList", listMap);
        data.put("employeeList2", listMap);

        Workbook workbook = ExcelExportUtil.exportExcel(params, data);
        File savefile = new File("D:/home/excel/");
        if (!savefile.exists()) {
            savefile.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream("D:/home/excel/;员工通讯录_0402.xls");
        workbook.write(fos);
        fos.close();
    }    
    @Test
    public void templateExportDemo_2() throws Exception {
    	TemplateExportParams params = new TemplateExportParams(
    			"doc/kyoffice_address_book.xls",3);
    	Map<String, Object> data = new HashMap<String, Object>();
    	List<Map<String,Object>> listMap=Lists.newArrayList();
    	for(int i=1;i<=12;i++){
    		Map<String,Object> item=Maps.newHashMap();
    		item.put("month",i+"月");
    		item.put("salary1",3000+(i-1)*100);
    		item.put("salary2",2800+(i-1)*150);
    		listMap.add(item);
    	}
    	data.put("salaryList", listMap);
    	
    	Workbook workbook = ExcelExportUtil.exportExcel(params, data);
    	File savefile = new File("D:/home/excel/");
    	if (!savefile.exists()) {
    		savefile.mkdirs();
    	}
    	FileOutputStream fos = new FileOutputStream("D:/home/excel/;员工通讯录_0402.xls");
    	workbook.write(fos);
    	fos.close();
    }    
    
    //@Test//有问题
    public void testExportExcel_3() throws Exception{
    	Workbook workbook = ExcelExportUtil.exportExcel(schoolCourseList,ExcelType.HSSF);
    	FileOutputStream fos = new FileOutputStream("D:/home/excel/六年级课程表.xls");
    	workbook.write(fos);
    	fos.close();    	
    }

}
