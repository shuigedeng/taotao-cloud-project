package com.taotao.cloud.common.execl.core.converter;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.data.ReadCellData;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Date;

public class ConverterReadData {
    @ExcelProperty("日期")
    private Date date;
    @ExcelProperty("本地日期")
    private LocalDateTime localDateTime;
    @ExcelProperty("布尔")
    private Boolean booleanData;
    @ExcelProperty("大数")
    private BigDecimal bigDecimal;
    @ExcelProperty("大整数")
    private BigInteger bigInteger;
    @ExcelProperty("长整型")
    private long longData;
    @ExcelProperty("整型")
    private Integer integerData;
    @ExcelProperty("短整型")
    private Short shortData;
    @ExcelProperty("字节型")
    private Byte byteData;
    @ExcelProperty("双精度浮点型")
    private double doubleData;
    @ExcelProperty("浮点型")
    private Float floatData;
    @ExcelProperty("字符串")
    private String string;
    @ExcelProperty("自定义")
    private ReadCellData<?> cellData;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public LocalDateTime getLocalDateTime() {
		return localDateTime;
	}

	public void setLocalDateTime(LocalDateTime localDateTime) {
		this.localDateTime = localDateTime;
	}

	public Boolean getBooleanData() {
		return booleanData;
	}

	public void setBooleanData(Boolean booleanData) {
		this.booleanData = booleanData;
	}

	public BigDecimal getBigDecimal() {
		return bigDecimal;
	}

	public void setBigDecimal(BigDecimal bigDecimal) {
		this.bigDecimal = bigDecimal;
	}

	public BigInteger getBigInteger() {
		return bigInteger;
	}

	public void setBigInteger(BigInteger bigInteger) {
		this.bigInteger = bigInteger;
	}

	public long getLongData() {
		return longData;
	}

	public void setLongData(long longData) {
		this.longData = longData;
	}

	public Integer getIntegerData() {
		return integerData;
	}

	public void setIntegerData(Integer integerData) {
		this.integerData = integerData;
	}

	public Short getShortData() {
		return shortData;
	}

	public void setShortData(Short shortData) {
		this.shortData = shortData;
	}

	public Byte getByteData() {
		return byteData;
	}

	public void setByteData(Byte byteData) {
		this.byteData = byteData;
	}

	public double getDoubleData() {
		return doubleData;
	}

	public void setDoubleData(double doubleData) {
		this.doubleData = doubleData;
	}

	public Float getFloatData() {
		return floatData;
	}

	public void setFloatData(Float floatData) {
		this.floatData = floatData;
	}

	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}

	public ReadCellData<?> getCellData() {
		return cellData;
	}

	public void setCellData(ReadCellData<?> cellData) {
		this.cellData = cellData;
	}
}
