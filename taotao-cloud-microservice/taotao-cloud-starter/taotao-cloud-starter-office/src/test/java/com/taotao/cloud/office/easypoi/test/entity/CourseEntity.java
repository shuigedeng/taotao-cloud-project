package com.taotao.cloud.office.easypoi.test.entity;

import java.util.List;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import cn.afterturn.easypoi.excel.annotation.ExcelEntity;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;

/**
 * @Title: Entity
 * @Description: 课程
 * @author JueYue
 *   2013-08-31 22:53:07
 * @version V1.0
 * 
 */
@SuppressWarnings("serial")
@ExcelTarget("courseEntity")
public class CourseEntity implements java.io.Serializable {
    /** 主键 */
    private String        id;
    /** 课程名称 */
    @Excel(name = "课程名称", orderNum = "1", width = 25,needMerge = true)
    private String        name;
    /** 老师主键 */
    //@ExcelEntity(id = "major")
    private TeacherEntity chineseTeacher;
    /** 老师主键 */
    @ExcelEntity(id = "absent")
    private TeacherEntity mathTeacher;

    @ExcelCollection(name = "学生", orderNum = "4")
    private List<StudentEntity> students;

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 主键
     */

    public String getId() {
        return this.id;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 课程名称
     */
    public String getName() {
        return this.name;
    }

    public TeacherEntity getMathTeacher() {
        return mathTeacher;
    }

    public List<StudentEntity> getStudents() {
        return students;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 老师主键
     */
    public TeacherEntity getChineseTeacher() {
        return chineseTeacher;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 主键
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 课程名称
     */
    public void setName(String name) {
        this.name = name;
    }

    public void setMathTeacher(TeacherEntity shuxueteacher) {
        this.mathTeacher = shuxueteacher;
    }

    public void setStudents(List<StudentEntity> students) {
        this.students = students;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String 老师主键
     */
    public void setChineseTeacher(TeacherEntity teacher) {
        this.chineseTeacher = teacher;
    }
}
