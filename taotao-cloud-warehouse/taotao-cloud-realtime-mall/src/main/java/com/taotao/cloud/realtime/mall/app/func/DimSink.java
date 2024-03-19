package com.taotao.cloud.realtime.mall.app.func;

import com.alibaba.fastjson2.JSONObject;
import com.taotao.cloud.realtime.mall.common.GmallConfig;
import com.taotao.cloud.realtime.mall.utils.DimUtil;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

/**
 * Date: 2021/2/3 Desc:  写出维度数据的Sink实现类
 */
public class DimSink extends RichSinkFunction<JSONObject> {

	//定义Phoenix连接对象
	private Connection conn = null;

	@Override
	public void open(Configuration parameters) throws Exception {
		//对连接对象进行初始化
		Class.forName("org.apache.phoenix.jdbc.PhoenixDriver");
		conn = DriverManager.getConnection(GmallConfig.PHOENIX_SERVER);
	}

	/**
	 * 对流中的数据进行处理
	 *
	 * @param jsonObj
	 * @param context
	 * @throws Exception
	 */
	@Override
	public void invoke(JSONObject jsonObj, Context context) throws Exception {
		//获取目标表的名称
		String tableName = jsonObj.getString("sink_table");
		//获取json中data数据   data数据就是经过过滤之后  保留的业务表中字段
		JSONObject dataJsonObj = jsonObj.getJSONObject("data");

		if (dataJsonObj != null && dataJsonObj.size() > 0) {
			//根据data中属性名和属性值  生成upsert语句
			String upsertSql = genUpsertSql(tableName.toUpperCase(), dataJsonObj);
			System.out.println("向Phoenix插入数据的SQL:" + upsertSql);

			//执行SQL
			PreparedStatement ps = null;
			try {
				ps = conn.prepareStatement(upsertSql);
				ps.execute();
				//注意：执行完Phoenix插入操作之后，需要手动提交事务
				conn.commit();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException("向Phoenix插入数据失败");
			} finally {
				if (ps != null) {
					ps.close();
				}
			}

			//如果当前做的是更新操作，需要将Redis中缓存的数据清除掉
			if (jsonObj.getString("type").equals("update")) {
				DimUtil.deleteCached(tableName, dataJsonObj.getString("id"));
			}
		}


	}

	// 根据data属性和值  生成向Phoenix中插入数据的sql语句
	private String genUpsertSql(String tableName, JSONObject dataJsonObj) {
        /*
            {
                "id":88,
                "tm_name":"xiaomi"
            }
        */
		//"upsert into 表空间.表名(列名.....) values (值....)"
		Set<String> keys = dataJsonObj.keySet();
		Collection<Object> values = dataJsonObj.values();
		String upsertSql = "upsert into " + GmallConfig.HABSE_SCHEMA + "." + tableName + "(" +
			StringUtils.join(keys, ",") + ")";

		String valueSql = " values ('" + StringUtils.join(values, "','") + "')";
		return upsertSql + valueSql;
	}
}
