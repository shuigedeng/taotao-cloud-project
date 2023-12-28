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
//
// import org.quartz.JobExecutionContext;
// import org.springframework.scheduling.quartz.QuartzJobBean;
//
//
/// **
// * 定时更新敏感词信息
// */
// public class SensitiveWordsJob extends QuartzJobBean {
//
//	//@Autowired
//	//private RedisRepository redisRepository;
//	//private static volatile Integer code = 0;
//
//	@Override
//	@SuppressWarnings("unchecked")
//	protected void executeInternal(JobExecutionContext jobExecutionContext) {
//		//Future<Integer> submit = EXECUTOR.submit(() -> {
//		//	Object words = redisRepository.get(RedisConstant.SENSITIVE_WORDS_KEY);
//		//	if (Objects.nonNull(words)) {
//		//		List<String> sensitives = (List<String>) words;
//		//		if (sensitives.isEmpty()) {
//		//			return -1;
//		//		}
//		//		int code = words.hashCode();
//		//		LogUtils.info("敏感词更新，code={}", code);
//		//		LogUtils.info("敏感词更新，this.code={}", SensitiveWordsJob.code);
//		//		if (SensitiveWordsJob.code != code) {
//		//			SensitiveWordsFilter.init(sensitives);
//		//			return code;
//		//		}
//		//	}
//		//
//		//	return -1;
//		//});
//		//
//		//try {
//		//	Integer integer = submit.get();
//		//	if (integer != -1) {
//		//		SensitiveWordsJob.code = integer;
//		//	}
//		//} catch (InterruptedException | ExecutionException e) {
//		//	LogUtils.error(e);
//		//}
//
//	}
// }
