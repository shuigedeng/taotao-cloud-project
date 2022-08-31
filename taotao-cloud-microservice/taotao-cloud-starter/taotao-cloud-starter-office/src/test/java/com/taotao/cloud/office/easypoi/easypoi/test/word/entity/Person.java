package com.taotao.cloud.office.easypoi.easypoi.test.word.entity;

import cn.afterturn.easypoi.entity.ImageEntity;
import cn.afterturn.easypoi.excel.annotation.Excel;

/**
 * 测试人员类
 *
 * @author JueYue
 * 2014年7月26日 下午10:51:30
 */
public class Person {

    @Excel(name = "姓名")
    private String      name;
    @Excel(name = "手机")
    private String      tel;
    @Excel(name = "Email")
    private String      email;
    private ImageEntity sign;

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getTel() {
        return tel;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public ImageEntity getSign() {
        return sign;
    }

    public void setSign(ImageEntity sign) {
        this.sign = sign;
    }
}
