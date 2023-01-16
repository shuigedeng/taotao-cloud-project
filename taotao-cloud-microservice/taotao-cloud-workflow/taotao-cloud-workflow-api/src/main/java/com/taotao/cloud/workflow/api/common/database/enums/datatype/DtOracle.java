package com.taotao.cloud.workflow.api.common.database.enums.datatype;

/**
 * 类功能
 * @date 2021/10/25
 */
public enum DtOracle implements DtInterface {
    /**
     *
     */
    VARCHAR2(
            "VARCHAR2",
            ViewDataTypeConst.VARCHAR,
            50,
            4000
    ),
    /**
     *
     */
    NVARCHAR2(
            "NVARCHAR2",
            ViewDataTypeConst.VARCHAR,
            50,
            2000
    ),
    /**
     * 默认长度：7
     */
    DATA(
            "DATE",
            ViewDataTypeConst.DATE_TIME,
            false
    ),
    /**
     * 默认长度：4000
     */
    CLOB(
            "CLOB",
            ViewDataTypeConst.TEXT,
            false
    ),
    /**
     * 用于显示数字类型
     */
    NUMBER_VIEW(
            "NUMBER",
            ViewDataTypeConst.ORACLE_NUMBER,
            11,
            38,
            0,
            127
    ),
    /**
     * 长度:1---38
     * 精度:-84---127
     */
    NUMBER_DECIMAL(
            "NUMBER",
            ViewDataTypeConst.DECIMAL,
            11,
            38,
            0,
            127
    ),
    /**
     *
     */
    NUMBER_INT(
            "NUMBER",
            ViewDataTypeConst.INT,
            11,
            38,
            0,
            127
    ),
    /**
     *
     */
    NUMBER_BIG(
            "NUMBER",
            ViewDataTypeConst.BIGINT,
            11,
            38,
            0,
            127
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

    DtOracle(String dbFieldType, String viewDataType, Integer defaultLength, Integer lengthMax,
             Integer defaultPrecision, Integer precisionMax){
        this.dbFieldType = dbFieldType;
        this.lengthModifyFlag = true;
        this.viewDataType = viewDataType;
        this.defaultLength = defaultLength;
        this.lengthMax = lengthMax;
        this.defaultPrecision = defaultPrecision;
        this.precisionMax = precisionMax;
    }

    DtOracle(String dbFieldType, String viewDataType, Integer defaultLength, Integer lengthMax){
        this.dbFieldType = dbFieldType;
        this.lengthModifyFlag = true;
        this.viewDataType = viewDataType;
        this.defaultLength = defaultLength;
        this.lengthMax = lengthMax;
        this.defaultPrecision = null;
        this.precisionMax = null;
    }

    DtOracle(String dbFieldType, String viewDataType, Boolean lengthModifyFlag){
        this.dbFieldType = dbFieldType;
        this.lengthModifyFlag = lengthModifyFlag;
        this.viewDataType = viewDataType;
        this.defaultLength = null;
        this.lengthMax = null;
        this.defaultPrecision = null;
        this.precisionMax = null;
    }

    /**
     * 获取当前数据类型模型
     * @return ignore
     */
    @Override
    public DataTypeModel getDataTypeModel(){
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
