package com.taotao.cloud.office.easypoi.test.entity.onettomany.hasname;

import cn.afterturn.easypoi.excel.annotation.Excel;

/**
 * @author by jueyue on 18-8-5.
 */
public class DeptEntity {


    public DeptEntity(){}

    public DeptEntity(String name){
        this.name = name;
    }

    @Excel(name = "需求部门标记数据来源部门")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
