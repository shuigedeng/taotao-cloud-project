package com.taotao.cloud.office.easypoi.test.entity.groupname;

import cn.afterturn.easypoi.excel.annotation.Excel;

import java.io.Serializable;

/**
 * 类说明
 *
 * @author lgp
 * @create 2018-01-29 16:06
 */
public class GroupExportVo implements Serializable {

    @Excel(name = "序号", mergeVertical = true)
    private String indexName;

    @Excel(name = "起降场", mergeVertical = true)
    private String takeoffPointName;

    @Excel(name = "单位", groupName = "基础", mergeRely = {1}, mergeVertical = true)
    private String companyName;

    @Excel(name = "机型/架数", groupName = "基础", mergeRely = {1}, mergeVertical = true)
    private String aerocraftInfo;

    @Excel(name = "机号", groupName = "基础", mergeRely = {1}, mergeVertical = true)
    private String aerocraft;

    @Excel(name = "空域/航线", orderNum = "3", mergeRely = {1}, mergeVertical = true)
    private String scopeName;

    @Excel(name = "高度（米）", orderNum = "11")
    private String heightDesc;

    @Excel(name = "任务类型", mergeRely = {1}, mergeVertical = true, orderNum = "12")
    private String taskType;

    @Excel(name = "计划", groupName = "开飞时间", orderNum = "2")
    private String takeOffTime;

    @Excel(name = "实施", groupName = "开飞时间", orderNum = "8")
    private String actualTakeoffTime;

    private Integer id;

    @Excel(name = "计划", groupName = "结束时间", orderNum = "7")
    private String landingTime;

    @Excel(name = "实施", groupName = "结束时间", orderNum = "4")
    private String actualLandingTime;

    @Excel(name = "备注", orderNum = "5")
    private String remark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public String getTakeoffPointName() {
        return takeoffPointName;
    }

    public void setTakeoffPointName(String takeoffPointName) {
        this.takeoffPointName = takeoffPointName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAerocraftInfo() {
        return aerocraftInfo;
    }

    public void setAerocraftInfo(String aerocraftInfo) {
        this.aerocraftInfo = aerocraftInfo;
    }

    public String getAerocraft() {
        return aerocraft;
    }

    public void setAerocraft(String aerocraft) {
        this.aerocraft = aerocraft;
    }

    public String getScopeName() {
        return scopeName;
    }

    public void setScopeName(String scopeName) {
        this.scopeName = scopeName;
    }

    public String getHeightDesc() {
        return heightDesc;
    }

    public void setHeightDesc(String heightDesc) {
        this.heightDesc = heightDesc;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getTakeOffTime() {
        return takeOffTime;
    }

    public void setTakeOffTime(String takeOffTime) {
        this.takeOffTime = takeOffTime;
    }

    public String getActualTakeoffTime() {
        return actualTakeoffTime;
    }

    public void setActualTakeoffTime(String actualTakeoffTime) {
        this.actualTakeoffTime = actualTakeoffTime;
    }

    public String getLandingTime() {
        return landingTime;
    }

    public void setLandingTime(String landingTime) {
        this.landingTime = landingTime;
    }

    public String getActualLandingTime() {
        return actualLandingTime;
    }

    public void setActualLandingTime(String actualLandingTime) {
        this.actualLandingTime = actualLandingTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
