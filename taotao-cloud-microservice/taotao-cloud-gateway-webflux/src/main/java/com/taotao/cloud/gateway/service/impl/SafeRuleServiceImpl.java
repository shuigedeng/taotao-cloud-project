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

package com.taotao.cloud.gateway.service.impl;

import com.alibaba.csp.sentinel.util.StringUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Stopwatch;
import com.taotao.boot.common.constant.CommonConstants;
import com.taotao.boot.common.utils.date.DateUtils;
import com.taotao.boot.common.utils.log.LogUtils;
import com.taotao.boot.common.utils.servlet.RequestUtils;
import com.taotao.boot.common.utils.servlet.ResponseUtils;
import com.taotao.cloud.gateway.model.BlackList;
import com.taotao.cloud.gateway.model.RuleConstant;
import com.taotao.cloud.gateway.service.IRuleCacheService;
import com.taotao.cloud.gateway.service.ISafeRuleService;
import java.net.URI;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 安全规则业务实现类
 *
 * @author shuigedeng
 */
@Service
public class SafeRuleServiceImpl implements ISafeRuleService {

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    private final IRuleCacheService ruleCacheService;

    public SafeRuleServiceImpl(IRuleCacheService ruleCacheService) {
        this.ruleCacheService = ruleCacheService;
    }

    @Override
    public Mono<Void> filterBlackList(ServerWebExchange exchange) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        try {
            URI originUri = getOriginRequestUri(exchange);
            String requestIp = RequestUtils.getRemoteAddr(request);
            String requestMethod = request.getMethod().name();
            AtomicBoolean forbid = new AtomicBoolean(false);
            // 从缓存中获取黑名单信息
            Set<Object> blackLists = ruleCacheService.getBlackList(requestIp);
            blackLists.addAll(ruleCacheService.getBlackList());
            // 检查是否在黑名单中
            checkBlackLists(forbid, blackLists, originUri, requestMethod);
            LogUtils.debug("黑名单检查完成 - {}", stopwatch.stop());
            if (forbid.get()) {
                LogUtils.info("属于黑名单地址 - {}", originUri.getPath());
                return ResponseUtils.fail(exchange, "已列入黑名单，访问受限");
            }
        } catch (Exception e) {
            LogUtils.error("黑名单检查异常: {} - {}", e.getMessage(), stopwatch.stop());
        }
        return null;
    }

    /**
     * 获取网关请求URI
     *
     * @param exchange ServerWebExchange
     * @return URI
     */
    private URI getOriginRequestUri(ServerWebExchange exchange) {
        return exchange.getRequest().getURI();
    }

    /**
     * 检查是否满足黑名单的条件
     *
     * @param forbid        是否黑名单判断
     * @param blackLists    黑名列表
     * @param uri           资源
     * @param requestMethod 请求方法
     */
    private void checkBlackLists(
            AtomicBoolean forbid, Set<Object> blackLists, URI uri, String requestMethod) {
        for (Object bl : blackLists) {
            BlackList blackList = JSONObject.parseObject(bl.toString(), BlackList.class);
            if (antPathMatcher.match(blackList.getRequestUri(), uri.getPath())
                    && RuleConstant.BLACKLIST_OPEN.equals(blackList.getStatus())) {
                if (RuleConstant.ALL.equalsIgnoreCase(blackList.getRequestMethod())
                        || StringUtils.equalsIgnoreCase(
                                requestMethod, blackList.getRequestMethod())) {
                    if (StringUtil.isNotBlank(blackList.getStartTime())
                            && StringUtil.isNotBlank(blackList.getEndTime())) {
                        if (DateUtils.between(
                                DateUtils.parseLocalTime(
                                        blackList.getStartTime(), CommonConstants.DATETIME_FORMAT),
                                DateUtils.parseLocalTime(
                                        blackList.getEndTime(), CommonConstants.DATETIME_FORMAT))) {
                            forbid.set(Boolean.TRUE);
                        }
                    } else {
                        forbid.set(Boolean.TRUE);
                    }
                }
            }
            if (forbid.get()) {
                break;
            }
        }
    }
}
