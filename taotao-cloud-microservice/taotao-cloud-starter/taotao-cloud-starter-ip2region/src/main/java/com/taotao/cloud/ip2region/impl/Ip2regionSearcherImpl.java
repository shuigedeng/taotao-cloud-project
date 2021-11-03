/*
 * Copyright (c) 2019-2029, Dreamlu 卢春梦 (596392912@qq.com & www.dreamlu.net).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.ip2region.impl;

import com.taotao.cloud.common.utils.Exceptions;
import com.taotao.cloud.ip2region.model.DbConfig;
import com.taotao.cloud.ip2region.model.DbSearcher;
import com.taotao.cloud.ip2region.model.Ip2regionSearcher;
import com.taotao.cloud.ip2region.model.IpInfo;
import com.taotao.cloud.ip2region.properties.Ip2regionProperties;
import com.taotao.cloud.ip2region.utils.IpInfoUtil;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StreamUtils;

/**
 * ip2region 初始化
 *
 * @author dream.lu
 */
public class Ip2regionSearcherImpl implements InitializingBean, Ip2regionSearcher {

	private final ResourceLoader resourceLoader;
	private final Ip2regionProperties properties;
	private DbSearcher searcher;

	public Ip2regionSearcherImpl(ResourceLoader resourceLoader,
		Ip2regionProperties properties) {
		this.resourceLoader = resourceLoader;
		this.properties = properties;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		DbConfig config = new DbConfig();
		Resource resource = resourceLoader.getResource(properties.getDbFileLocation());
		try (InputStream inputStream = resource.getInputStream()) {
			this.searcher = new DbSearcher(config,
				new ByteArrayDBReader(StreamUtils.copyToByteArray(inputStream)));
		}
	}

	@Override
	public IpInfo memorySearch(long ip) {
		try {
			return IpInfoUtil.toIpInfo(searcher.memorySearch(ip));
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
	}

	@Override
	public IpInfo memorySearch(String ip) {
		try {
			return IpInfoUtil.toIpInfo(searcher.memorySearch(ip));
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
	}

	@Override
	public IpInfo getByIndexPtr(long ptr) {
		try {
			return IpInfoUtil.toIpInfo(searcher.getByIndexPtr(ptr));
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
	}

	@Override
	public IpInfo btreeSearch(long ip) {
		try {
			return IpInfoUtil.toIpInfo(searcher.btreeSearch(ip));
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
	}

	@Override
	public IpInfo btreeSearch(String ip) {
		try {
			return IpInfoUtil.toIpInfo(searcher.btreeSearch(ip));
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
	}

	@Override
	public IpInfo binarySearch(long ip) {
		try {
			return IpInfoUtil.toIpInfo(searcher.binarySearch(ip));
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
	}

	@Override
	public IpInfo binarySearch(String ip) {
		try {
			return IpInfoUtil.toIpInfo(searcher.binarySearch(ip));
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
	}

}
