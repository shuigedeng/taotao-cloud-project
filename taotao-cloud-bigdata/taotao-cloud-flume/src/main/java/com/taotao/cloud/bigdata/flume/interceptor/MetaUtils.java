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
package com.taotao.cloud.bigdata.flume.interceptor;

import com.alibaba.fastjson.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/11/25 下午5:42
 */
public class MetaUtils {

	public static JSONObject getLogMetaJson() {
		try {
			Connection conn = JDBCUtils.getConn();
			Statement statement = conn.createStatement();
			ResultSet resultSet = statement.executeQuery(
				"select field, field_type from `taotao-cloud-log-meta` where meta_type = 0");

			JSONObject jsonMeta = new JSONObject();
			while (resultSet.next()) {
				String field = resultSet.getString("field");
				String fieldType = resultSet.getString("field_type");

				genSimpleJsonMeta(jsonMeta, field.trim(), fieldType);
			}
			return jsonMeta;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static void genSimpleJsonMeta(JSONObject jsonObj, String field, String fieldType) {
		String type = fieldType.toLowerCase();
		switch (type) {
			case "string":
				jsonObj.put(field, "");
				return;
			case "int":
			case "double":
				jsonObj.put(field, 0);
				return;
			case "bigint":
				jsonObj.put(field, 0L);
				return;
			case "datetime":
				jsonObj.put(field, null);
				return;
			case "array":
				jsonObj.put(field, new ArrayList<>());
				return;
			default:
		}
	}
}
