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
package com.taotao.cloud.bigdata.hbase.configuration;

import com.taotao.cloud.bigdata.hbase.properties.HbaseProperties;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * HbaseAutoConfiguration
 *
 * @author dengtao
 * @date 2020/10/30 11:10
 * @since v1.0
 */
@org.springframework.context.annotation.Configuration
@EnableConfigurationProperties(HbaseProperties.class)
public class HbaseAutoConfiguration {

	private static final String HBASE_ZOOKEEPER_QUORUM = "hbase.zookeeper.quorum";
	private static final String HBASE_ZOOKEEPER_PORT = "hbase.zookeeper.port";
	private static final String HBASE_ZOOKEEPER_ZNODE = "hbase.zookeeper.znode";
	private static final String HBASE_CLIENT_KEYVALUE_MAXSIZE = "hbase.client.keyvalue.maxsize";

	private HbaseProperties hbaseProperties;

	@Autowired
	public void setHbaseProperties(HbaseProperties hbaseProperties) {
		this.hbaseProperties = hbaseProperties;
	}

	@Bean
	public org.apache.hadoop.conf.Configuration configuration() {
		org.apache.hadoop.conf.Configuration configuration = HBaseConfiguration.create();
		configuration.set(HBASE_ZOOKEEPER_QUORUM, hbaseProperties.getQuorum());
		configuration.set(HBASE_ZOOKEEPER_PORT, hbaseProperties.getPort());
		configuration.set(HBASE_ZOOKEEPER_ZNODE, hbaseProperties.getZnode());
		configuration.set(HBASE_CLIENT_KEYVALUE_MAXSIZE, hbaseProperties.getMaxsize());
		return configuration;
	}
}
