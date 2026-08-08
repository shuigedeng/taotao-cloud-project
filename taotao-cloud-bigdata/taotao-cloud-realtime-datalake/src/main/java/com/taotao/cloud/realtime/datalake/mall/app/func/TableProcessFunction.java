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

package com.taotao.cloud.realtime.datalake.mall.app.func;

import com.alibaba.fastjson2.JSONObject;
import com.taotao.cloud.realtime.mall.bean.TableProcess;
import com.taotao.cloud.realtime.mall.common.GmallConfig;
import com.taotao.cloud.realtime.mall.utils.MySQLUtil;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

/**
 *
 * Date: 2021/2/1
 * Desc:  配置表处理函数
 */
public class TableProcessFunction extends ProcessFunction<JSONObject, JSONObject> {
    // 因为要将维度数据通过侧输出流输出，所以我们在这里定义一个侧输出流标记
    private OutputTag<JSONObject> outputTag;

    // 用于在内存中存放配置表信息的Map <表名：操作,tableProcess>
    private Map<String, TableProcess> tableProcessMap = new HashMap<>();

    // 用于在内存中存放已经处理过的表（在phoenix中已经建过的表）
    private Set<String> existsTables = new HashSet<>();

    // 声明Phoenix的连接对象
    Connection conn = null;

    // 实例化函数对象的时候，将侧输出流标签也进行赋值
    public TableProcessFunction(OutputTag<JSONObject> outputTag) {
        this.outputTag = outputTag;
    }

    // 在函数被调用的时候执行的方法，执行一次
    @Override
    public void open(Configuration parameters) throws Exception {
        // 初始化Phoenix连接
        Class.forName("org.apache.phoenix.jdbc.PhoenixDriver");
        conn = DriverManager.getConnection(GmallConfig.PHOENIX_SERVER);

        // 初始化配置表信息
        refreshMeta();

        // 开启一个定时任务
        // 因为配置表的数据可能会发生变化，每隔一段时间就从配置表中查询一次数据，更新到map，并检查建表
        // 从现在起过delay毫秒后，每隔period执行一次
        ScheduledExecutorService timer = Executors.newScheduledThreadPool(10);

        timer.schedule(this::refreshMeta, 5000, TimeUnit.SECONDS);
    }

    private void refreshMeta() {
        // ========1.从MySQL数据库配置表中查询配置信息============
        System.out.println("查询配置表信息");
        List<TableProcess> tableProcessList =
                MySQLUtil.queryList("select * from table_process", TableProcess.class, true);
        // 对查询出来的结果集进行遍历
        for (TableProcess tableProcess : tableProcessList) {
            // 获取源表表名
            String sourceTable = tableProcess.getSourceTable();
            // 获取操作类型
            String operateType = tableProcess.getOperateType();
            // 输出类型      hbase|kafka
            String sinkType = tableProcess.getSinkType();
            // 输出目的地表名或者主题名
            String sinkTable = tableProcess.getSinkTable();
            // 输出字段
            String sinkColumns = tableProcess.getSinkColumns();
            // 表的主键
            String sinkPk = tableProcess.getSinkPk();
            // 建表扩展语句
            String sinkExtend = tableProcess.getSinkExtend();
            // 拼接保存配置的key
            String key = sourceTable + ":" + operateType;

            // ========2.将从配置表中查询到配置信息，保存到内存的map集合中=============
            tableProcessMap.put(key, tableProcess);

            // ========3.如果当前配置项是维度配置，需要向Hbase表中保存数据，那么我们需要判断phoenix中是否存在这张表=====================
            if (TableProcess.SINK_TYPE_HBASE.equals(sinkType) && "insert".equals(operateType)) {
                boolean notExist = existsTables.add(sourceTable);
                // 如果在内存Set集合中不存在这个表，那么在Phoenix中创建这种表
                if (notExist) {
                    // 检查在Phonix中是否存在这种表
                    // 有可能已经存在，只不过是应用缓存被清空，导致当前表没有缓存，这种情况是不需要创建表的
                    // 在Phoenix中，表的确不存在，那么需要将表创建出来
                    checkTable(sinkTable, sinkColumns, sinkPk, sinkExtend);
                }
            }
        }
        // 如果没有从数据库的配置表中读取到数据
        if (tableProcessMap == null || tableProcessMap.size() == 0) {
            throw new RuntimeException("没有从数据库的配置表中读取到数据");
        }
    }

    private void checkTable(String tableName, String fields, String pk, String ext) {
        // 如果在配置表中，没有配置主键 需要给一个默认主键的值
        if (pk == null) {
            pk = "id";
        }
        // 如果在配置表中，没有配置建表扩展 需要给一个默认建表扩展的值
        if (ext == null) {
            ext = "";
        }
        // 拼接建表语句
        StringBuilder createSql =
                new StringBuilder(
                        "create table if not exists "
                                + GmallConfig.HABSE_SCHEMA
                                + "."
                                + tableName
                                + "(");

        // 对建表字段进行切分
        String[] fieldsArr = fields.split(",");
        for (int i = 0; i < fieldsArr.length; i++) {
            String field = fieldsArr[i];
            // 判断当前字段是否为主键字段
            if (pk.equals(field)) {
                createSql.append(field).append(" varchar primary key ");
            } else {
                createSql.append("info.").append(field).append(" varchar ");
            }
            if (i < fieldsArr.length - 1) {
                createSql.append(",");
            }
        }
        createSql.append(")");
        createSql.append(ext);

        System.out.println("创建Phoenix表的语句:" + createSql);

        // 获取Phoenix连接
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(createSql.toString());
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw new RuntimeException("Phoenix建表失败");
                }
            }
        }
    }

    // 每过来一个元素，方法执行一次，主要任务是根据内存中配置表Map对当前进来的元素进行分流处理
    @Override
    public void processElement(JSONObject jsonObj, Context ctx, Collector<JSONObject> out)
            throws Exception {
        // 获取表名
        String table = jsonObj.getString("table");
        // 获取操作类型
        String type = jsonObj.getString("type");
        // 注意：问题修复  如果使用Maxwell的Bootstrap同步历史数据  ，这个时候它的操作类型叫bootstrap-insert
        if ("bootstrap-insert".equals(type)) {
            type = "insert";
            jsonObj.put("type", type);
        }

        if (tableProcessMap != null && tableProcessMap.size() > 0) {
            // 根据表名和操作类型拼接key
            String key = table + ":" + type;
            // 从内存的配置Map中获取当前key对象的配置信息
            TableProcess tableProcess = tableProcessMap.get(key);
            // 如果获取到了该元素对应的配置信息
            if (tableProcess != null) {
                // 获取sinkTable，指明当前这条数据应该发往何处  如果是维度数据，那么对应的是phoenix中的表名；如果是事实数据，对应的是kafka的主题名
                jsonObj.put("sink_table", tableProcess.getSinkTable());
                String sinkColumns = tableProcess.getSinkColumns();
                // 如果指定了sinkColumn，需要对保留的字段进行过滤处理
                if (sinkColumns != null && sinkColumns.length() > 0) {
                    filterColumn(jsonObj.getJSONObject("data"), sinkColumns);
                }
            } else {
                System.out.println("NO this Key:" + key + "in MySQL");
            }

            // 根据sinkType，将数据输出到不同的流
            if (tableProcess != null
                    && tableProcess.getSinkType().equals(TableProcess.SINK_TYPE_HBASE)) {
                // 如果sinkType = hbase ，说明是维度数据，通过侧输出流输出
                ctx.output(outputTag, jsonObj);
            } else if (tableProcess != null
                    && tableProcess.getSinkType().equals(TableProcess.SINK_TYPE_KAFKA)) {
                // 如果sinkType = kafka ，说明是事实数据，通过主流输出
                out.collect(jsonObj);
            }
        }
    }

    // 对Data中数据进行进行过滤
    private void filterColumn(JSONObject data, String sinkColumns) {
        // sinkColumns 表示要保留那些列     id,out_trade_no,order_id
        String[] cols = sinkColumns.split(",");
        // 将数组转换为集合，为了判断集合中是否包含某个元素
        List<String> columnList = Arrays.asList(cols);

        // 获取json对象中封装的一个个键值对   每个键值对封装为Entry类型
        Set<Map.Entry<String, Object>> entrySet = data.entrySet();

        Iterator<Map.Entry<String, Object>> it = entrySet.iterator();

        for (; it.hasNext(); ) {
            Map.Entry<String, Object> entry = it.next();
            if (!columnList.contains(entry.getKey())) {
                it.remove();
            }
        }
    }
}
