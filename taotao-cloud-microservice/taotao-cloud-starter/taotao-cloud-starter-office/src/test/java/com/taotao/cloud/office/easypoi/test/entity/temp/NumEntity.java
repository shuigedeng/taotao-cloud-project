package com.taotao.cloud.office.easypoi.test.entity.temp;

import cn.afterturn.easypoi.excel.annotation.Excel;

/**
 * Created by Think on 2017/6/17.
 */
public class NumEntity {

    @Excel(name = "str", numFormat = "0.00")
    private String strTest;

    @Excel(name = "dou", type = 10, isStatistics = true)
    private Double douTest;

    @Excel(name = "long", numFormat = "#.##%")
    private long longTest;

    @Excel(name = "int", numFormat = "000.0")
    private int intTest;

    public String getStrTest() {
        return strTest;
    }

    public void setStrTest(String strTest) {
        this.strTest = strTest;
    }

    public Double getDouTest() {
        return douTest;
    }

    public void setDouTest(Double douTest) {
        this.douTest = douTest;
    }

    public long getLongTest() {
        return longTest;
    }

    public void setLongTest(long longTest) {
        this.longTest = longTest;
    }

    public int getIntTest() {
        return intTest;
    }

    public void setIntTest(int intTest) {
        this.intTest = intTest;
    }
}
