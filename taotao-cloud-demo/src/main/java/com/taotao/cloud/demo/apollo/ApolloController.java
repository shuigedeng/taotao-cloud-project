///*
// * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      https://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package com.taotao.cloud.demo.apollo;
//
//import com.taotao.cloud.common.support.lock.DistributedLock;
//import com.taotao.cloud.common.support.lock.ZLock;
//import com.taotao.cloud.common.model.Result;
//import com.taotao.cloud.zookeeper.model.ZkIdGenerator;
//import com.taotao.cloud.zookeeper.template.ZookeeperTemplate;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * DemoController
// *
// * @author shuigedeng
// * @version 2022.04 1.0.0
// * @since 2021/11/29 14:29
// */
//@RestController
//@RequestMapping("/demo")
//public class ApolloController {
//	@Autowired
//	private ZookeeperTemplate zookeeperTemplate;
//	@Autowired
//	private ZkIdGenerator zkIdGenerator;
//	@Autowired
//	private DistributedLock distributedLock;
//
//	@GetMapping("/zookeeper")
//	public Result<List<String>> getClassification() {
//		try {
//			String hello = zookeeperTemplate.createNode("/test", "hello");
//
//			String s = zkIdGenerator.genId().get();
//
//			ZLock lock = distributedLock.lock("lock", 3000L, TimeUnit.SECONDS);
//
//			System.out.println("///////////");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		List<String> result = new ArrayList<>();
//		return Result.success(result);
//	}
//
//}
