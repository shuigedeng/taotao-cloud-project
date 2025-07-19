/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.realtime.datalake.mall.bean;

import lombok.Data;

/**
 *
 * Date: 2021/2/1
 * Desc:  配置表对应的实体类
 */
@Data
public class TableProcess {
    // 动态分流Sink常量   改为小写和脚本一致
    public static final String SINK_TYPE_HBASE = "hbase";
    public static final String SINK_TYPE_KAFKA = "kafka";
    public static final String SINK_TYPE_CK = "clickhouse";
    // 来源表
    String sourceTable;
    // 操作类型 insert,update,delete
    String operateType;
    // 输出类型 hbase kafka
    String sinkType;
    // 输出表(主题)
    String sinkTable;
    // 输出字段
    String sinkColumns;
    // 主键字段
    String sinkPk;
    // 建表扩展
    String sinkExtend;
}
