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
package com.taotao.cloud.ip2region.configuration;

import com.taotao.cloud.ip2region.impl.Ip2regionSearcherImpl;
import com.taotao.cloud.ip2region.model.Ip2regionSearcher;
import com.taotao.cloud.ip2region.properties.Ip2regionProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ResourceLoader;

/**
 * ip2region 自动化配置
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 20:01:42
 */
@AutoConfiguration
@EnableConfigurationProperties({Ip2regionProperties.class})
@ConditionalOnProperty(prefix = Ip2regionProperties.PREFIX, name = "enabled", havingValue = "true")
public class Ip2regionAutoConfiguration {

	@Bean
	public Ip2regionSearcher ip2regionSearcher(ResourceLoader resourceLoader,
		Ip2regionProperties properties) {
		return new Ip2regionSearcherImpl(resourceLoader, properties);
	}

	//@Bean
	//public Searcher searcher() {
	//	String dbPath = "";
	//	File file;
	//
	//	try {
	//		file = ResourceUtils.getFile("classpath:lionsoul/ip2region.xdb");
	//
	//		//Resource resource = resourceLoader.getResource("classpath:lionsoul/ip2region.xdb");
	//		//File file1 = resource.getFile();
	//	} catch (IOException e) {
	//		System.out.printf("failed to create searcher with `%s`: %s\n", dbPath, e);
	//		throw new RuntimeException(e);
	//	}
	//
	//	Searcher searcher;
	//
	//	//// 完全基于文件的查询
	//	//try {
	//	//	searcher = Searcher.newWithFileOnly(dbPath);
	//	//} catch (IOException e) {
	//	//	System.out.printf("failed to create searcher with `%s`: %s\n", dbPath, e);
	//	//	throw new RuntimeException(e);
	//	//}
	//	//
	//	////缓存 VectorIndex 索引
	//	////我们可以提前从 xdb 文件中加载出来 VectorIndex 数据，然后全局缓存，每次创建 Searcher 对象的时候使用全局的 VectorIndex 缓存可以减少一次固定的 IO 操作，从而加速查询，减少 IO 压力。
	//	//// 1、从 dbPath 中预先加载 VectorIndex 缓存，并且把这个得到的数据作为全局变量，后续反复使用。
	//	//byte[] vIndex;
	//	//try {
	//	//	vIndex = Searcher.loadVectorIndexFromFile(dbPath);
	//	//} catch (Exception e) {
	//	//	System.out.printf("failed to load vector index from `%s`: %s\n", dbPath, e);
	//	//	throw new RuntimeException(e);
	//	//}
	//	//
	//	//// 2、使用全局的 vIndex 创建带 VectorIndex 缓存的查询对象。
	//	//try {
	//	//	searcher = Searcher.newWithVectorIndex(dbPath, vIndex);
	//	//} catch (Exception e) {
	//	//	System.out.printf("failed to create vectorIndex cached searcher with `%s`: %s\n", dbPath, e);
	//	//	throw new RuntimeException(e);
	//	//}
	//
	//	//缓存整个 xdb 数据
	//	//预先加载整个 ip2region.xdb 的数据到内存，然后基于这个数据创建查询对象来实现完全基于文件的查询，类似之前的 memory search。
	//	// 1、从 dbPath 加载整个 xdb 到内存。
	//	byte[] cBuff;
	//	try {
	//		RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
	//		cBuff = Searcher.loadContent(randomAccessFile);
	//	} catch (Exception e) {
	//		System.out.printf("failed to load content from `%s`: %s\n", dbPath, e);
	//		throw new RuntimeException(e);
	//	}
	//
	//	// 2、使用上述的 cBuff 创建一个完全基于内存的查询对象。
	//	try {
	//		searcher = Searcher.newWithBuffer(cBuff);
	//	} catch (Exception e) {
	//		System.out.printf("failed to create content cached searcher: %s\n", e);
	//		throw new RuntimeException(e);
	//	}
	//
	//	//查询
	//	//try {
	//	//	String ip = "1.2.3.4";
	//	//	long sTime = System.nanoTime();
	//	//	String region = searcher.searchByStr(ip);
	//	//	long cost = TimeUnit.NANOSECONDS.toMicros((long) (System.nanoTime() - sTime));
	//	//	System.out.printf("{region: %s, ioCount: %d, took: %d μs}\n", region, searcher.getIOCount(), cost);
	//	//} catch (Exception e) {
	//	//	System.out.printf("failed to search(%s): %s\n", ip, e);
	//	//	return;
	//	//}
	//
	//	return searcher;
	//}

}
