package com.taotao.cloud.java.javaee.s2.c7_springboot.springboot.java.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Air  implements Serializable {

	private Integer id;

	private Integer districtId;

	private java.util.Date monitorTime;

	private Integer pm10;

	private Integer pm25;

	private String monitoringStation;

	private java.util.Date lastModifyTime;

}
