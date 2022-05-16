package com.taotao.cloud.common.execl.core.fill.style;

import java.util.Date;

public class FillStyleData {
    private String name;
    private Double number;
    private Date date;
    private String empty;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getNumber() {
		return number;
	}

	public void setNumber(Double number) {
		this.number = number;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getEmpty() {
		return empty;
	}

	public void setEmpty(String empty) {
		this.empty = empty;
	}
}
