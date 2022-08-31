package com.taotao.cloud.office.easypoi.test.entity.temp;

import cn.afterturn.easypoi.excel.annotation.Excel;

/**
 * 预算
 * @author JueYue
 *   2014年12月27日 下午9:08:03
 */
public class BudgetAccountsEntity {

    @Excel(name = "编码")
    private String code;

    @Excel(name = "名称")
    private String name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
