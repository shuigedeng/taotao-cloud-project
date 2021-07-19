/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.bigdata.hive.api.service.impl;

import com.taotao.cloud.bigdata.hive.api.service.HiveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * HiveServiceImpl
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/10/30 10:12
 */
@Service
public class HiveServiceImpl implements HiveService {

	public static final Logger logger = LoggerFactory.getLogger(HiveServiceImpl.class);

	@Autowired
	@Qualifier("hiveDruidTemplate")
	private JdbcTemplate jdbcTemplate;

	@Override
	public String insert() {
		jdbcTemplate.execute("insert into hive_test(key, value) values('Neo','Chen')");
		return "Done";
	}

	@Override
	public String select() {
		String sql = "select * from HIVE_TEST";
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
		for (Map<String, Object> row : rows) {
			System.out.printf("%s\t%s%n", row.get("key"), row.get("value"));
		}
		return "Done";
	}

	@Override
	public String delete() {
		StringBuffer sql = new StringBuffer("DROP TABLE IF EXISTS ");
		sql.append("HIVE_TEST");
		logger.info(sql.toString());
		jdbcTemplate.execute(sql.toString());
		return "Done";
	}

	@Override
	public String create() {
		StringBuilder sql = new StringBuilder("create table IF NOT EXISTS ");
		sql.append("HIVE_TEST");
		sql.append("(KEY INT, VALUE STRING)");
		sql.append("PARTITIONED BY (CTIME DATE)"); // 分区存储
		sql.append(
			"ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t' LINES TERMINATED BY '\n' "); // 定义分隔符
		sql.append("STORED AS TEXTFILE"); // 作为文本存储

		logger.info(sql.toString());
		jdbcTemplate.execute(sql.toString());

		return "OK";

	}
}
