package com.taotao.cloud.office.easypoi.test.en;

import cn.afterturn.easypoi.excel.annotation.Excel;

/**
 * 枚举测试类
 * @author by jueyue on 18-4-2.
 */
public class EnumDataEntity {

    @Excel(name ="名字")
    private String name;
    @Excel(name ="性别")
    private Sex sex;
    @Excel(name ="基础状态" )
    private StatusEnum baseStatus;
    @Excel(name ="状态" , enumExportField = "message", enumImportMethod = "getByMessage")
    private StatusEnum status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    public StatusEnum getBaseStatus() {
        return baseStatus;
    }

    public void setBaseStatus(StatusEnum baseStatus) {
        this.baseStatus = baseStatus;
    }
}
