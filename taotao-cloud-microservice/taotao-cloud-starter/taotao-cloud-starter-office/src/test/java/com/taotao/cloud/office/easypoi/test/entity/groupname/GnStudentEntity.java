package com.taotao.cloud.office.easypoi.test.entity.groupname;

import java.util.Date;

import cn.afterturn.easypoi.excel.annotation.Excel;

/**
 * @author JueYue
 * @version V1.0
 * @Title: Entity
 * @Description: 学生 2013-08-31 22:53:34
 */
@SuppressWarnings("serial")
public class GnStudentEntity implements java.io.Serializable {

    @Excel(name = "学生姓名", height = 20, width = 30, orderNum = "2")
    private String name;

    @Excel(name = "学生性别", replace = {"男_1", "女_0"}, suffix = "生", orderNum = "3")
    private int sex;

    @Excel(name = "出生日期", format = "yyyy-MM-dd", width = 20, orderNum = "4")
    private Date birthday;

    @Excel(name = "进校日期", format = "yyyy-MM-dd", orderNum = "5")
    private Date registrationDate;

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 学生姓名
     */
    public String getName() {
        return this.name;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 学生性别
     */
    public int getSex() {
        return this.sex;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String 学生姓名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String 学生性别
     */
    public void setSex(int sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

}
