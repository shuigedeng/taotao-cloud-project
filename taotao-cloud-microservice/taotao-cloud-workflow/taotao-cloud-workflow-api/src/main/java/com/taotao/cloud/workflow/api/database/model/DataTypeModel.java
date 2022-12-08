package com.taotao.cloud.workflow.api.database.model;

import lombok.Data;

@Data
public class DataTypeModel {

	/**
	 * 数据类型名
	 */
	private String dbFieldType;

	/**
	 * 页面类型模板
	 */
	private String viewDataType;

	/**
	 * 当前数据长度（宽度）
	 */
	private Integer currentLength;

	/**
	 * 当前数据精度
	 */
	private Integer currentPrecision;

	/**
	 * 默认长度
	 */
	private Integer defaultLength;

	/**
	 * 最大长度
	 */
	private Integer lengthMax;

	/**
	 * true：可修改。false：不可修改
	 */
	private Boolean lengthModifyFlag;

	/**
	 * 默认精度
	 */
	private Integer defaultPrecision;

	/**
	 * 最大精度
	 */
	private Integer precisionMax;

	public DataTypeModel(String dbFieldType, String viewDataType, Integer defaultLength,
		Integer lengthMax,
		Boolean lengthModifyFlag, Integer defaultPrecision, Integer precisionMax) {
		this.dbFieldType = dbFieldType;
		this.viewDataType = viewDataType;
		this.defaultLength = defaultLength;
		this.lengthMax = lengthMax;
		this.lengthModifyFlag = lengthModifyFlag;
		this.defaultPrecision = defaultPrecision;
		this.precisionMax = precisionMax;
	}

}
