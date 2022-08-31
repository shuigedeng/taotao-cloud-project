package com.taotao.cloud.office.easypoi.test.entity;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.taotao.cloud.office.easypoi.hanlder.ViliGroupOne;
import com.taotao.cloud.office.easypoi.hanlder.ViliGroupTwo;

/**
 * Excel导入校验类
 * @author JueYue
 *   2015年2月24日 下午2:21:07
 */
public class ExcelVerifyEntity {

    /**
     * Email校验
     */
    @Excel(name = "Email", width = 25)
    private String email;
    /**
     * 最大
     */
    @Excel(name = "Max")
    @Max(value = 15,message = "max 最大值不能超过15" ,groups = {ViliGroupOne.class})
    private int    max;
    /**
     * 最小
     */
    @Excel(name = "Min")
    @Min(value = 3, groups = {ViliGroupTwo.class})
    private int    min;
    /**
     * 非空校验
     */
    @Excel(name = "NotNull")
    @NotNull
    private String notNull;
    /**
     * 正则校验
     */
    @Excel(name = "Regex")
    @Pattern(regexp = "[\u4E00-\u9FA5]*", message = "不是中文")
    private String regex;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public String getNotNull() {
        return notNull;
    }

    public void setNotNull(String notNull) {
        this.notNull = notNull;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

}
