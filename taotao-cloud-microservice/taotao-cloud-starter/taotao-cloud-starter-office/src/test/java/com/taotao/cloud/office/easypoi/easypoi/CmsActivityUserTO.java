package com.taotao.cloud.office.easypoi.easypoi;

import cn.afterturn.easypoi.excel.annotation.Excel;

import javax.validation.constraints.Pattern;


public class CmsActivityUserTO {
    private String uuid;
    private String company_uuid;
    private String unit_uuid;
    private String city_uuid;
    private String department_uuid;
    private String area_uuid;
    @Excel(name = "公司", width = 20,isImportField = "true")
    private String company;
    @Excel(name = "单位",isImportField = "true")
    private String unit;
    @Excel(name = "部门",isImportField = "true" ,isWrap = false)
    private String department;
    @Excel(name = "城市",isImportField = "true")
    private String city;
    @Excel(name = "园区",isImportField = "true")
    private String area;
    @Excel(name = "姓名",isImportField = "true")
    private String name;

    @Excel(name = "工号", width = 15,isImportField = "true")
    private String job_num;
    @Excel(name = "手机号", width = 13,isImportField = "true")
    private String phone;
    @Excel(name = "状态",isImportField = "true")
    private String dimission;

    private Integer is_dimission;

    public Integer getIs_dimission() {
        if ("在职".equals(dimission)) {
            is_dimission = 0;
        } else {
            is_dimission = 1;
        }
        return is_dimission;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getCompany_uuid() {
        return company_uuid;
    }

    public void setCompany_uuid(String company_uuid) {
        this.company_uuid = company_uuid;
    }

    public String getUnit_uuid() {
        return unit_uuid;
    }

    public void setUnit_uuid(String unit_uuid) {
        this.unit_uuid = unit_uuid;
    }

    public String getCity_uuid() {
        return city_uuid;
    }

    public void setCity_uuid(String city_uuid) {
        this.city_uuid = city_uuid;
    }

    public String getDepartment_uuid() {
        return department_uuid;
    }

    public void setDepartment_uuid(String department_uuid) {
        this.department_uuid = department_uuid;
    }

    public String getArea_uuid() {
        return area_uuid;
    }

    public void setArea_uuid(String area_uuid) {
        this.area_uuid = area_uuid;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJob_num() {
        return job_num;
    }

    public void setJob_num(String job_num) {
        this.job_num = job_num;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDimission() {
        return dimission;
    }

    public void setDimission(String dimission) {
        this.dimission = dimission;
    }

    public void setIs_dimission(Integer is_dimission) {
        this.is_dimission = is_dimission;
    }
}
