package com.taotao.cloud.office.easypoi.test.entity.img;

import cn.afterturn.easypoi.excel.annotation.Excel;

/**
 * 公司信息 Created by JueYue on 2017/8/25.
 */
public class CompanyHasImgModel {

    public CompanyHasImgModel() {
    }

    public CompanyHasImgModel(String companyName, String companyLogo, String companyAddr) {
        this.companyName = companyName;
        this.companyLogo = companyLogo;
        this.companyAddr = companyAddr;
    }

    @Excel(name = "公司名称")
    private String companyName;
    @Excel(name = "公司LOGO", type = 2 ,width = 40 , height = 30,imageType = 1)
    private String companyLogo;
    @Excel(name = "公司地址" ,width = 60)
    private String companyAddr;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyLogo() {
        return companyLogo;
    }

    public void setCompanyLogo(String companyLogo) {
        this.companyLogo = companyLogo;
    }

    public String getCompanyAddr() {
        return companyAddr;
    }

    public void setCompanyAddr(String companyAddr) {
        this.companyAddr = companyAddr;
    }
}
