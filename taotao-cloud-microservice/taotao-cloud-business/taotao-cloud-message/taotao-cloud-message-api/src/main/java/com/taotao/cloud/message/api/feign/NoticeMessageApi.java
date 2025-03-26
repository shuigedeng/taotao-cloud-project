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

package com.taotao.cloud.message.api.feign;

 import com.taotao.boot.common.constant.ServiceNameConstants;
import com.taotao.cloud.message.api.feign.fallback.NoticeMessageApiFallback;
import com.taotao.cloud.message.api.feign.request.NoticeMessageApiRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 远程调用售后模块
 *
 * @author shuigedeng
 * @since 2020/5/2 16:42
 */
@FeignClient(value = ServiceNameConstants.TAOTAO_CLOUD_MESSAGE, fallbackFactory = NoticeMessageApiFallback.class)
public interface NoticeMessageApi {

	@GetMapping(value = "/noticeMessage/sms")
	void noticeMessage(NoticeMessageApiRequest noticeMessageDTO);

	@GetMapping(value = "/message/sms")
	boolean sendSms();

	/**
	 * 站内信
	 *
	 * @return
	 */
	@GetMapping(value = "/message/message")
	boolean sendMessage();

	@GetMapping(value = "/message/dingtalk")
	boolean sendDingtalk();

	@GetMapping(value = "/message/wechat")
	boolean sendWechat();

	@GetMapping(value = "/message/email")
	boolean sendEmail();

	@GetMapping(value = "/message/store")
	boolean sendStoreMessage();
}
