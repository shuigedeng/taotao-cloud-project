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

package com.taotao.cloud.ip2region.impl;

import com.taotao.cloud.ip2region.model.Ip2regionSearcher;
import com.taotao.cloud.ip2region.model.IpInfo;
import com.taotao.cloud.ip2region.model.Searcher;
import com.taotao.cloud.ip2region.properties.Ip2regionProperties;
import com.taotao.cloud.ip2region.utils.IpInfoUtil;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StreamUtils;

/**
 * ip2region 初始化
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-06-28 17:41:41
 */
public class Ip2regionSearcherImpl implements InitializingBean, DisposableBean, Ip2regionSearcher {

	private final ResourceLoader resourceLoader;
	private final Ip2regionProperties properties;

	private Searcher searcher;

	public Ip2regionSearcherImpl(ResourceLoader resourceLoader, Ip2regionProperties properties) {
		this.resourceLoader = resourceLoader;
		this.properties = properties;
	}

	@Override
	public IpInfo memorySearch(long ip) {
		try {
			return IpInfoUtil.toIpInfo(searcher.search(ip));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public IpInfo memorySearch(String ip) {
		try {
			return IpInfoUtil.toIpInfo(searcher.search(ip));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Resource resource = resourceLoader.getResource(properties.getDbFileLocation());
		try (InputStream inputStream = resource.getInputStream()) {
			this.searcher = Searcher.newWithBuffer(StreamUtils.copyToByteArray(inputStream));
		}
	}

	@Override
	public void destroy() throws Exception {
		if (this.searcher != null) {
			this.searcher.close();
		}
	}

}
