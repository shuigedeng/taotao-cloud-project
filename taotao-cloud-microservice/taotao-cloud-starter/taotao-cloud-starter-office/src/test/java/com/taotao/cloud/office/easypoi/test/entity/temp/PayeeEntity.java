package com.taotao.cloud.office.easypoi.test.entity.temp;

import cn.afterturn.easypoi.excel.annotation.Excel;

public class PayeeEntity {

    @Excel(name = "全称")
    private String name;

    @Excel(name = "银行账号")
    private String bankAccount;

    @Excel(name = "开户银行")
    private String bankName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

}
