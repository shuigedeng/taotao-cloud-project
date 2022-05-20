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
package com.taotao.cloud.oss.configuration;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.oss.propeties.FastdfsProperties;
import com.taotao.cloud.oss.propeties.OssProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/10/26 10:28
 */
@AutoConfiguration
@EnableConfigurationProperties({FastdfsProperties.class})
@ConditionalOnProperty(prefix = OssProperties.PREFIX, name = "type", havingValue = "FASTDFS")
public class FdfsDfsAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(FdfsDfsAutoConfiguration.class, StarterName.OSS_STARTER);
	}

	private final FastdfsProperties properties;

	public FdfsDfsAutoConfiguration(FastdfsProperties properties) {
		this.properties = properties;
	}

	//
	//@Bean
	//public PooledConnectionFactory pooledConnectionFactory() {
	//	PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory();
	//	pooledConnectionFactory.setSoTimeout(properties.getSoTimeout());
	//	pooledConnectionFactory.setConnectTimeout(properties.getConnectTimeout());
	//	return pooledConnectionFactory;
	//}
	//
	//@Bean
	//@ConfigurationProperties(prefix = "taotao.cloud.file.fastdfs.pool")
	//public ConnectionPoolConfig connectionPoolConfig() {
	//	return new ConnectionPoolConfig();
	//}
	//
	//@Bean
	//public FdfsConnectionPool fdfsConnectionPool(PooledConnectionFactory pooledConnectionFactory,
	//	ConnectionPoolConfig connectionPoolConfig) {
	//	return new FdfsConnectionPool(pooledConnectionFactory, connectionPoolConfig);
	//}
	//
	//@Bean
	//public TrackerConnectionManager trackerConnectionManager(
	//	FdfsConnectionPool fdfsConnectionPool) {
	//	return new TrackerConnectionManager(fdfsConnectionPool, properties.getTrackerList());
	//}
	//
	//@Bean
	//public TrackerClient trackerClient(TrackerConnectionManager trackerConnectionManager) {
	//	return new DefaultTrackerClient(trackerConnectionManager);
	//}
	//
	//@Bean
	//public ConnectionManager connectionManager(FdfsConnectionPool fdfsConnectionPool) {
	//	return new ConnectionManager(fdfsConnectionPool);
	//}
	//
	//@Bean
	//public FastFileStorageClient fastFileStorageClient(TrackerClient trackerClient,
	//	ConnectionManager connectionManager) {
	//	return new DefaultFastFileStorageClient(trackerClient, connectionManager);
	//}
	//
	//@Bean
	//public AppendFileStorageClient appendFileStorageClient(TrackerClient trackerClient,
	//	ConnectionManager connectionManager) {
	//	return new DefaultAppendFileStorageClient(trackerClient, connectionManager);
	//}
	//
	//@Bean
	//public UploadFileService fileUpload(FastFileStorageClient fastFileStorageClient) {
	//	return new FastDfsFileUpload(fastFileStorageClient);
	//}

}
