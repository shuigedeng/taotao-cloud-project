package com.taotao.cloud.office.easypoi.test.entity.temp;

import java.io.Serializable;
import java.util.List;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import cn.afterturn.easypoi.excel.annotation.ExcelEntity;

/**
 * 模板导出 测试类
 * @author JueYue
 *   2014年12月26日 上午9:39:13
 */
public class TemplateExcelExportEntity implements Serializable {

    /**
     * 
     */
    private static final long          serialVersionUID = 1L;

    @Excel(name = "序号")
    private String                     index;

    @Excel(name = "资金性质")
    private String                     accountType;

    @ExcelCollection(name = "预算科目")
    private List<BudgetAccountsEntity> budgetAccounts;

    @Excel(name = "项目名称")
    private String                     projectName;

    @ExcelEntity(name = "收款人")
    private PayeeEntity                payee;

    @Excel(name = "申请金额")
    private String                     amountApplied;

    @Excel(name = "核定金额")
    private String                     approvedAmount;

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public List<BudgetAccountsEntity> getBudgetAccounts() {
        return budgetAccounts;
    }

    public void setBudgetAccounts(List<BudgetAccountsEntity> budgetAccounts) {
        this.budgetAccounts = budgetAccounts;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public PayeeEntity getPayee() {
        return payee;
    }

    public void setPayee(PayeeEntity payee) {
        this.payee = payee;
    }

    public String getAmountApplied() {
        return amountApplied;
    }

    public void setAmountApplied(String amountApplied) {
        this.amountApplied = amountApplied;
    }

    public String getApprovedAmount() {
        return approvedAmount;
    }

    public void setApprovedAmount(String approvedAmount) {
        this.approvedAmount = approvedAmount;
    }

}
