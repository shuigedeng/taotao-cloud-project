package com.taotao.cloud.office.easypoi.test.addressList;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.taotao.cloud.office.easypoi.test.en.Sex;

/**
 * 地址配置
 *
 * @author jueyue on 20-4-26.
 */
public class AddressListEntity {
    @Excel(name = "名字")
    private String name;
    @Excel(name = "性别")
    private Sex sex;
    @Excel(name = "B站登记", dict = "level", addressList = true)
    private int    bilibili;
    @Excel(name = "状态", replace = {"初始化_0", "正常_1", "注销_2"}, addressList = true)
    private String status;

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

    public int getBilibili() {
        return bilibili;
    }

    public void setBilibili(int bilibili) {
        this.bilibili = bilibili;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
