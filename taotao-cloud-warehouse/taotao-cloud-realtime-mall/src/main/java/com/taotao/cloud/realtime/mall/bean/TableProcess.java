package com.taotao.cloud.realtime.mall.bean;

import lombok.Data;

/**
 *
 * Date: 2021/2/1
 * Desc:  配置表对应的实体类
 */
@Data
public class TableProcess {
    //动态分流Sink常量   改为小写和脚本一致
    public static final String SINK_TYPE_HBASE = "hbase";
    public static final String SINK_TYPE_KAFKA = "kafka";
    public static final String SINK_TYPE_CK = "clickhouse";
    //来源表
    String sourceTable;
    //操作类型 insert,update,delete
    String operateType;
    //输出类型 hbase kafka
    String sinkType;
    //输出表(主题)
    String sinkTable;
    //输出字段
    String sinkColumns;
    //主键字段
    String sinkPk;
    //建表扩展
    String sinkExtend;
}

