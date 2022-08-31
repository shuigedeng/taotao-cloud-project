package com.taotao.cloud.office.easypoi.test.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;

/**
 * @Title: Entity
 * @Description: 课程老师
 * @author JueYue
 *   2013-08-31 22:52:17
 * @version V1.0
 * 
 */
@SuppressWarnings("serial")
@ExcelTarget("teacherEntity")
public class TeacherEntity implements java.io.Serializable {
    /** id */
    //@Excel(name = "主讲老师", orderNum = "2",isImportField = "true_major,true_absent")
    private String id;
    /** name */
    @Excel(name = "主讲老师_major,代课老师_absent", orderNum = "1",needMerge = true,isImportField = "true_major,true_absent")
    private String name;

    /*
     * @Excel(exportName="老师照片",orderNum="3",exportType=2,exportFieldHeight=15,
     * exportFieldWidth=20) private String pic;
     */

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String id
     */

    public String getId() {
        return this.id;
    }

    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String name
     */
    public String getName() {
        return this.name;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String name
     */
    public void setName(String name) {
        this.name = name;
    }

    /*
     * public String getPic() { // if(StringUtils.isEmpty(pic)){ // pic =
     * "plug-in/login/images/Easypoi.png"; // } return pic; }
     * 
     * public void setPic(String pic) { this.pic = pic; }
     */
}
