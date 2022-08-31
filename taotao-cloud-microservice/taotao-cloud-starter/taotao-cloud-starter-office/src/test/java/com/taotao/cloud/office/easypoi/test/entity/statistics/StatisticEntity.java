package com.taotao.cloud.office.easypoi.test.entity.statistics;

import java.math.BigDecimal;

import cn.afterturn.easypoi.excel.annotation.Excel;

/**
 * 统计测试类
 * @author JueYue
 *   2014年12月17日 上午11:51:10
 */
public class StatisticEntity {

    @Excel(name = "名称")
    private String     name;

    @Excel(name = "int 测试", width = 15, isStatistics = true)
    private int        intTest;

    @Excel(name = "long 测试", width = 15, isStatistics = true)
    private long       longTest;

    @Excel(name = "double 测试", width = 15, isStatistics = true)
    private double     doubleTest;

    @Excel(name = "string 测试", width = 15, isStatistics = true)
    private String     stringTest;

    @Excel(name = "BigDecimal 测试", width = 15, isStatistics = true)
    private BigDecimal bigDecimalTest;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIntTest() {
        return intTest;
    }

    public void setIntTest(int intTest) {
        this.intTest = intTest;
    }

    public long getLongTest() {
        return longTest;
    }

    public void setLongTest(long longTest) {
        this.longTest = longTest;
    }

    public double getDoubleTest() {
        return doubleTest;
    }

    public void setDoubleTest(double doubleTest) {
        this.doubleTest = doubleTest;
    }

    public String getStringTest() {
        return stringTest;
    }

    public void setStringTest(String stringTest) {
        this.stringTest = stringTest;
    }

    public BigDecimal getBigDecimalTest() {
        return bigDecimalTest;
    }

    public void setBigDecimalTest(BigDecimal bigDecimalTest) {
        this.bigDecimalTest = bigDecimalTest;
    }

}
