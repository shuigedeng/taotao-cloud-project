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
package com.taotao.cloud.sys.biz.controller.feign;

import static com.taotao.cloud.web.version.VersionEnum.V2022_07;
import static com.taotao.cloud.web.version.VersionEnum.V2022_08;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.model.SecurityUser;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.core.configuration.AsyncAutoConfiguration.AsyncThreadPoolTaskExecutor;
import com.taotao.cloud.idempotent.annotation.Idempotent;
import com.taotao.cloud.limit.annotation.GuavaLimit;
import com.taotao.cloud.limit.annotation.Limit;
import com.taotao.cloud.security.springsecurity.annotation.NotAuth;
import com.taotao.cloud.sys.api.feign.IFeignDictApi;
import com.taotao.cloud.sys.api.feign.IFeignUserApi;
import com.taotao.cloud.sys.api.feign.response.FeignDictResponse;
import com.taotao.cloud.sys.api.model.vo.user.UserQueryVO;
import com.taotao.cloud.sys.biz.model.convert.DictConvert;
import com.taotao.cloud.sys.biz.model.entity.dict.Dict;
import com.taotao.cloud.sys.biz.model.entity.system.User;
import com.taotao.cloud.sys.biz.service.business.IDictService;
import com.taotao.cloud.sys.biz.service.business.IUserService;
import com.taotao.cloud.web.base.controller.BaseFeignController;
import com.taotao.cloud.web.request.annotation.RequestLogger;
import com.taotao.cloud.web.version.ApiInfo;
import com.yomahub.tlog.core.annotation.TLogAspect;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.AsyncContext;
import jakarta.servlet.AsyncEvent;
import jakarta.servlet.AsyncListener;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.context.request.async.WebAsyncTask;

/**
 * 内部服务端-字典API
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-10-09 14:24:19
 */
@Validated
@RestController
public class FeignUserController extends BaseFeignController<IUserService, User, Long> implements
	IFeignUserApi {

	@Override
	public UserQueryVO findUserInfoByUsername(String username) {
		return null;
	}

	@Override
	public SecurityUser getUserInfoBySocial(String providerId, int providerUserId) {
		return null;
	}

	@Override
	public SecurityUser getSysSecurityUser(String nicknameOrUserNameOrPhoneOrEmail) {
		return null;
	}
}
