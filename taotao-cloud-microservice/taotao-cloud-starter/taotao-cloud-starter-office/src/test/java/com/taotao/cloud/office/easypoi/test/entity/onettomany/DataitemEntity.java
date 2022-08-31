package com.taotao.cloud.office.easypoi.test.entity.onettomany;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;

import java.util.List;

/**
 * 供需对接-数据项表
 *
 * @author Walt
 */
public class DataitemEntity {

    private static final long serialVersionUID = 1L;


    /**
     * 数据项名称
     */
    @Excel(name = "包含的数据项（每个数据项需单行填写）", width = 30)
    private String name;

    /**
     * 数源部门Ids
     */
    //@ExcelCollection(name = "需求部门标记数据来源部门")
    private List<String> sdepts;

    /**
     * 备注内容
     */
    @Excel(name = "备注")
    private String remark;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<String> getSdepts() {
        return sdepts;
    }

    public void setSdepts(List<String> sdepts) {
        this.sdepts = sdepts;
    }
}
