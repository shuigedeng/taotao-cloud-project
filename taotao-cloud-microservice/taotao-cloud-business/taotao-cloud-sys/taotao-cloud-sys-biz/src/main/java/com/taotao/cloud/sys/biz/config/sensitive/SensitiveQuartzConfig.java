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

package com.taotao.cloud.sys.biz.config.sensitive;
//
// import org.quartz.JobBuilder;
// import org.quartz.JobDetail;
// import org.quartz.SimpleScheduleBuilder;
// import org.quartz.Trigger;
// import org.quartz.TriggerBuilder;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
//
/// **
// * 定时执行配置
// *
// * @author shuigedeng
// * @version 2022.03
// * @since 2022-03-25 15:01:33
// */
// @Configuration
// public class SensitiveQuartzConfig {
//
//	@Bean
//	public JobDetail sensitiveQuartzDetail() {
//		return JobBuilder.newJob(SensitiveWordsJob.class)
//			.withIdentity("sensitiveQuartz")
//			.storeDurably()
//			.build();
//	}
//
//	@Bean
//	public Trigger sensitiveQuartzTrigger() {
//		SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
//			.withIntervalInSeconds(3600)
//			.repeatForever();
//
//		return TriggerBuilder.newTrigger()
//			.forJob(sensitiveQuartzDetail())
//			.withIdentity("sensitiveQuartz")
//			.withSchedule(scheduleBuilder)
//			.build();
//	}
//
// }
