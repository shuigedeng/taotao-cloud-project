package com.taotao.cloud.office.easypoi.test.entity.groupname;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelEntity;

/**
 * Created by JueYue on 2017/10/20.
 */
public class GnEntity {

    @Excel(name = "电话号码", groupName = "联系方式", orderNum = "1")
    private String clientPhone = null;
    @Excel(name = "姓名")
    private String clientName = null;
    @ExcelEntity(name = "学生", show = true)
    private GnStudentEntity studentEntity;

    public String getClientPhone() {
        return clientPhone;
    }

    public void setClientPhone(String clientPhone) {
        this.clientPhone = clientPhone;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public GnStudentEntity getStudentEntity() {
        return studentEntity;
    }

    public void setStudentEntity(GnStudentEntity studentEntity) {
        this.studentEntity = studentEntity;
    }
}
