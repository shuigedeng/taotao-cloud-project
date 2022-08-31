package com.taotao.cloud.office.easypoi.easypoi;

import cn.afterturn.easypoi.excel.annotation.Excel;


public class ComInfoDTO{


    @Excel(name = "公司名称")
    private String companyName;
    
    @Excel(name = "法定代表人")
    private String legalPerson;
    
    @Excel(name = "注册资本")
    private String registeredCapital;
    
    @Excel(name = "成立日期")
    private String registrationDate;
    
    @Excel(name = "经营状态")
    private String state;
    
    @Excel(name = "所属省份")
    private String sheng;
    
    @Excel(name = "所属市区")
    private String shi;
    
    @Excel(name = "所属区县")
    private String xian;
    
    @Excel(name = "公司类型")
    private String enterpriseType;
    
    @Excel(name = "统一社会信用代码")
    private String creditCode;
    
    @Excel(name = "企业公示的联系电话")
    private String contactWay;
    
    @Excel(name = "企业公示的联系电话（更多号码）")
    private String contactWays;
    
    @Excel(name = "企业公示的地址")
    private String registeredAddress;
    
    @Excel(name = "企业公示的网址")
    private String website;
    
    @Excel(name = "企业公示的邮箱")
    private String mailbox;
    
    @Excel(name = "经营范围")
    private String businessScope;

    
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getLegalPerson() {
		return legalPerson;
	}

	public void setLegalPerson(String legalPerson) {
		this.legalPerson = legalPerson;
	}

	public String getRegisteredCapital() {
		return registeredCapital;
	}

	public void setRegisteredCapital(String registeredCapital) {
		this.registeredCapital = registeredCapital;
	}

	public String getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(String registrationDate) {
		this.registrationDate = registrationDate;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getSheng() {
		return sheng;
	}

	public void setSheng(String sheng) {
		this.sheng = sheng;
	}

	public String getShi() {
		return shi;
	}

	public void setShi(String shi) {
		this.shi = shi;
	}

	public String getXian() {
		return xian;
	}

	public void setXian(String xian) {
		this.xian = xian;
	}

	public String getEnterpriseType() {
		return enterpriseType;
	}

	public void setEnterpriseType(String enterpriseType) {
		this.enterpriseType = enterpriseType;
	}

	public String getCreditCode() {
		return creditCode;
	}

	public void setCreditCode(String creditCode) {
		this.creditCode = creditCode;
	}

	public String getContactWay() {
		return contactWay;
	}

	public void setContactWay(String contactWay) {
		this.contactWay = contactWay;
	}

	public String getContactWays() {
		return contactWays;
	}

	public void setContactWays(String contactWays) {
		this.contactWays = contactWays;
	}

	public String getRegisteredAddress() {
		return registeredAddress;
	}

	public void setRegisteredAddress(String registeredAddress) {
		this.registeredAddress = registeredAddress;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getMailbox() {
		return mailbox;
	}

	public void setMailbox(String mailbox) {
		this.mailbox = mailbox;
	}

	public String getBusinessScope() {
		return businessScope;
	}

	public void setBusinessScope(String businessScope) {
		this.businessScope = businessScope;
	}
    
	
    
    

}
