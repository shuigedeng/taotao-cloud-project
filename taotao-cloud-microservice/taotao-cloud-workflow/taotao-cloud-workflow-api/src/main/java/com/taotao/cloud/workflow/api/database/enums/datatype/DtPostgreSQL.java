package com.taotao.cloud.workflow.api.database.enums.datatype;


public enum DtPostgreSQL implements DtInterface {

	/**
	 *
	 */
	VARCHAR(
		"varchar",
		ViewDataTypeConst.VARCHAR,
		50,
		10485760
	),
	/**
	 * 默认长度：6
	 */
	TIME_STAMP(
		"timestamp",
		ViewDataTypeConst.DATE_TIME,
		false
	),
	/**
	 *
	 */
	NUMERIC(
		"numeric",
		ViewDataTypeConst.DECIMAL,
		0,
		1000,
		0,
		100
	),
	/**
	 * 默认长度：无
	 */
	TEXT(
		"text",
		ViewDataTypeConst.TEXT,
		false
	),
	/**
	 * 默认长度：32
	 */
	INT4(
		"int4",
		ViewDataTypeConst.INT,
		false
	),
	/**
	 * 默认长度：64
	 */
	INT8(
		"int8",
		ViewDataTypeConst.BIGINT,
		false
	);

	/**
	 * 数据库字段类型
	 */
	private final String dbFieldType;

	/**
	 * 1：可修改。0：不可修改
	 */
	private final Boolean lengthModifyFlag;

	/**
	 * 前端显示数据类型
	 */
	private final String viewDataType;

	/**
	 * 默认长度
	 */
	private final Integer defaultLength;

	/**
	 * 长度范围
	 */
	private final Integer lengthMax;

	/**
	 * 默认精度
	 */
	private final Integer defaultPrecision;

	/**
	 * 精度范围
	 */
	private final Integer precisionMax;

	DtPostgreSQL(String dbFieldType, String viewDataType, Integer defaultLength, Integer lengthMax,
		Integer defaultPrecision, Integer precisionMax) {
		this.dbFieldType = dbFieldType;
		this.lengthModifyFlag = true;
		this.viewDataType = viewDataType;
		this.defaultLength = defaultLength;
		this.lengthMax = lengthMax;
		this.defaultPrecision = defaultPrecision;
		this.precisionMax = precisionMax;
	}

	DtPostgreSQL(String dbFieldType, String viewDataType, Integer defaultLength,
		Integer lengthMax) {
		this.dbFieldType = dbFieldType;
		this.lengthModifyFlag = true;
		this.viewDataType = viewDataType;
		this.defaultLength = defaultLength;
		this.lengthMax = lengthMax;
		this.defaultPrecision = null;
		this.precisionMax = null;
	}

	DtPostgreSQL(String dbFieldType, String viewDataType, Boolean lengthModifyFlag) {
		this.dbFieldType = dbFieldType;
		this.lengthModifyFlag = lengthModifyFlag;
		this.viewDataType = viewDataType;
		this.defaultLength = null;
		this.lengthMax = null;
		this.defaultPrecision = null;
		this.precisionMax = null;
	}


	@Override
	public DataTypeModel getDataTypeModel() {
		return new DataTypeModel(
			this.dbFieldType,
			this.viewDataType,
			this.defaultLength,
			this.lengthMax,
			this.lengthModifyFlag,
			this.defaultPrecision,
			this.precisionMax
		);
	}

	@Override
	public String getDbFieldType() {
		return dbFieldType;
	}

}
