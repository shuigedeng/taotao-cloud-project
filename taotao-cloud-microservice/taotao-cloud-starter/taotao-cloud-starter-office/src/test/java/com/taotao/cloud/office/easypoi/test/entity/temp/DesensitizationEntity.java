package com.taotao.cloud.office.easypoi.test.entity.temp;

import cn.afterturn.easypoi.excel.annotation.Excel;

/**
 * @author jueyue on 21-2-8.
 */
public class DesensitizationEntity {

    @Excel(name = "姓名", desensitizationRule = "1,6")
    private String name;
    @Excel(name = "身份证", desensitizationRule = "6_4")
    private String card;
    @Excel(name = "手机号", desensitizationRule = "3_4")
    private String phone;
    @Excel(name = "邮箱", desensitizationRule = "3~@")
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
