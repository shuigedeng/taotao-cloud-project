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
package com.taotao.cloud.uc.biz;

import com.taotao.cloud.dingtalk.annatations.DingerScan;
import com.taotao.cloud.dingtalk.annatations.EnableMultiDinger;
import com.taotao.cloud.web.annotation.TaoTaoCloudApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;

/**
 * TaoTaoCloudUcApplication
 * 抑制java9 module 报错
 * --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.lang.reflect=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens jdk.management/com.sun.management.internal=ALL-UNNAMED
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/11/30 下午3:33
 */
@DingerScan(basePackages = "com.taotao.cloud.uc.biz.dingtalk")
@MapperScan(basePackages = "com.taotao.cloud.uc.biz.mapper")
@EnableMultiDinger
@TaoTaoCloudApplication
public class TaoTaoCloudUcApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaoTaoCloudUcApplication.class, args);
	}

}
