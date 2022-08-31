package com.taotao.cloud.office.easypoi.test.entity.onettomany.hasname;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * 供需对接-需求材料
 *
 * @author Walt
 */
public class SupMaterialEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 材料名称
     */
    @Excel(name = "材料名称", width = 20, needMerge = true)
    private String name;

    /**
     * 材料类型
     */
    @Excel(name = "材料类别（数据字典）", dict = "mtype", needMerge = true)
    private Integer mtype;

    /**
     * 来源类型
     */
    @Excel(name = "材料来源方式（数据字典）", dict = "sourceType", width = 20, needMerge = true)
    private Integer sourceType;

    /**
     * 法律依据
     */
    @Excel(name = "法律依据（数据字典）", dict = "lawType", needMerge = true)
    private Integer lawType;

    /**
     * 需求数据项
     */
    @ExcelCollection(name = "需求数据项")
    private List<DataitemEntity> dataitemList = Lists.newArrayList();

    public List<DataitemEntity> getDataitemList() {
        return dataitemList;
    }

    public void setDataitemList(List<DataitemEntity> dataitemList) {
        this.dataitemList = dataitemList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMtype() {
        return mtype;
    }

    public void setMtype(Integer mtype) {
        this.mtype = mtype;
    }

    public Integer getSourceType() {
        return sourceType;
    }

    public void setSourceType(Integer sourceType) {
        this.sourceType = sourceType;
    }

    public Integer getLawType() {
        return lawType;
    }

    public void setLawType(Integer lawType) {
        this.lawType = lawType;
    }


}
