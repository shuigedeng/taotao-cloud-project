/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * <http://www.apache.org/licenses/LICENSE-2.0>
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Dante Engine 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Dante Cloud 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 <https://gitee.com/herodotus/dante-engine>
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 <https://gitee.com/herodotus/dante-engine>
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.auth.biz.dante.authorization.processor;

import cn.herodotus.engine.assistant.core.definition.constants.SymbolConstants;
import cn.herodotus.engine.oauth2.authorization.definition.HerodotusConfigAttribute;
import cn.herodotus.engine.oauth2.authorization.definition.HerodotusRequest;
import cn.herodotus.engine.oauth2.authorization.enums.Category;
import cn.herodotus.engine.oauth2.core.definition.domain.SecurityAttribute;
import cn.herodotus.engine.oauth2.core.enums.PermissionExpression;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * <p>Description: SecurityMetadata异步处理Service </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/8/1 17:43
 */
@Component
public class SecurityMetadataSourceAnalyzer {

    private static final Logger log = LoggerFactory.getLogger(SecurityMetadataSourceAnalyzer.class);

    private final SecurityMetadataSourceStorage securityMetadataSourceStorage;
    private final SecurityMatcherConfigurer securityMatcherConfigurer;

    public SecurityMetadataSourceAnalyzer(SecurityMetadataSourceStorage securityMetadataSourceStorage, SecurityMatcherConfigurer securityMatcherConfigurer) {
        this.securityMetadataSourceStorage = securityMetadataSourceStorage;
        this.securityMatcherConfigurer = securityMatcherConfigurer;
    }

    /**
     * 直接使用 {@link org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer} 中的方法
     *
     * @param authority 权限
     * @return 权限表达式
     */
    private String hasAuthority(String authority) {
        return "hasAuthority('" + authority + "')";
    }

    /**
     * 将解析后的数据添加对应的分组中
     *
     * @param container 分组结果数据容器
     * @param category  分组类别
     * @param resources 权限数据
     */
    private void appendToGroup(Map<Category, LinkedHashMap<HerodotusRequest, List<HerodotusConfigAttribute>>> container, Category category, LinkedHashMap<HerodotusRequest, List<HerodotusConfigAttribute>> resources) {
        LinkedHashMap<HerodotusRequest, List<HerodotusConfigAttribute>> value = new LinkedHashMap<>();

        if (container.containsKey(category)) {
            value = container.get(category);
        }

        value.putAll(resources);

        container.put(category, value);
    }

    /**
     * 将各个服务配置的静态权限数据分组
     *
     * @param securityMatchers 静态权限数据
     * @return 分组后的权限数据
     */
    private Map<Category, LinkedHashMap<HerodotusRequest, List<HerodotusConfigAttribute>>> groupSecurityMatchers(LinkedHashMap<HerodotusRequest, List<HerodotusConfigAttribute>> securityMatchers) {

        Map<Category, LinkedHashMap<HerodotusRequest, List<HerodotusConfigAttribute>>> group = new LinkedHashMap<>();

        securityMatchers.forEach((key, value) -> {
            LinkedHashMap<HerodotusRequest, List<HerodotusConfigAttribute>> resources = new LinkedHashMap<>();
            resources.put(key, value);
            appendToGroup(group, Category.getCategory(key.getPattern()), resources);
        });

        log.debug("[Herodotus] |- Grouping security matcher by category.");
        return group;
    }

    /**
     * 解析并动态组装所需要的权限。
     * <p>
     * 1. 原 spring-security-oauth
     * Spring Security 基础权限规则，来源于org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer.AuthorizedUrl
     * OAuth2 权限规则来源于 org.springframework.security.oauth2.provider.expression.OAuth2SecurityExpressionMethods
     * 2. 新 spring-authorization-server
     * Spring Security 基础权限规则，来源于{@link org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer}
     * OAuth2 权限规则来源于 目前还没有
     * <p>
     * 具体解析采用的是 Security 的 {@link org.springframework.security.access.AccessDecisionVoter} 方式，而不是采用自定义的 {@link org.springframework.security.access.AccessDecisionManager} 该方式会与默认的 httpsecurity 配置覆盖。
     * · 基本的权限验证采用的是：{@link org.springframework.security.access.vote.RoleVoter}
     * · scope权限采用两种方式：
     * 一种是：Spring Security org.springframework.security.oauth2.provider.vote.ScopeVoter 目前已取消
     * 另一种是：OAuth2 'hasScope'和'hasAnyScope'方式  org.springframework.security.oauth2.provider.expression.OAuth2SecurityExpressionMethods#hasAnyScope(String...)
     * <p>
     * 如果实际应用不满足可以，自己扩展AccessDecisionVoter或者AccessDecisionManager
     *
     * @param securityAttribute {@link SecurityAttribute}
     * @return security权限定义集合
     */
    private List<HerodotusConfigAttribute> analysis(SecurityAttribute securityAttribute) {

        List<HerodotusConfigAttribute> attributes = new ArrayList<>();

        if (StringUtils.isNotBlank(securityAttribute.getPermissions())) {
            String[] permissions = org.springframework.util.StringUtils.commaDelimitedListToStringArray(securityAttribute.getPermissions());
            Arrays.stream(permissions).forEach(item -> attributes.add(new HerodotusConfigAttribute(hasAuthority(item))));
        }

        if (StringUtils.isNotBlank(securityAttribute.getWebExpression())) {
            attributes.add(new HerodotusConfigAttribute(securityAttribute.getWebExpression()));
        }

        return attributes;
    }

    /**
     * 创建请求和权限的映射数据
     *
     * @param url              请求url
     * @param methods          请求method
     * @param configAttributes Security权限{@link ConfigAttribute}
     * @return 保存请求和权限的映射的Map
     */
    private LinkedHashMap<HerodotusRequest, List<HerodotusConfigAttribute>> convert(String url, String methods, List<HerodotusConfigAttribute> configAttributes) {
        LinkedHashMap<HerodotusRequest, List<HerodotusConfigAttribute>> result = new LinkedHashMap<>();
        if (StringUtils.isBlank(methods)) {
            result.put(new HerodotusRequest(url), configAttributes);
        } else {
            // 如果methods是以逗号分隔的字符串，那么进行拆分处理
            if (StringUtils.contains(methods, SymbolConstants.COMMA)) {
                String[] multiMethod = StringUtils.split(methods, SymbolConstants.COMMA);
                for (String method : multiMethod) {
                    result.put(new HerodotusRequest(url, method), configAttributes);
                }
            } else {
                result.put(new HerodotusRequest(url, methods), configAttributes);
            }
        }

        return result;
    }

    /**
     * 将 UPMS 分发的 SecurityAttributes 数据进行权限转换并分组
     *
     * @param securityAttributes 权限数据
     * @return 分组后的权限数据
     */
    private Map<Category, LinkedHashMap<HerodotusRequest, List<HerodotusConfigAttribute>>> groupingSecurityMetadata(List<SecurityAttribute> securityAttributes) {

        Map<Category, LinkedHashMap<HerodotusRequest, List<HerodotusConfigAttribute>>> group = new LinkedHashMap<>();

        securityAttributes.forEach(securityAttribute -> {
            LinkedHashMap<HerodotusRequest, List<HerodotusConfigAttribute>> resources = convert(securityAttribute.getUrl(), securityAttribute.getRequestMethod(), analysis(securityAttribute));
            appendToGroup(group, Category.getCategory(securityAttribute.getUrl()), resources);
        });

        log.debug("[Herodotus] |- Grouping security metadata by category.");
        return group;
    }

    /**
     * 获取 SecurityFilterChain 中配置的 RequestMatchers 信息。
     * <p>
     * RequestMatchers 可以理解为“静态配置”，将其与平台后端的“动态配置”有机结合。同时，防止因动态配置导致的静态配置失效的问题。
     * <p>
     * 目前只处理了 permitAll 类型。其它内容根据后续使用情况再行添加。
     *
     * @return RequestMatchers 中配置的权限数据
     */
    private LinkedHashMap<HerodotusRequest, List<HerodotusConfigAttribute>> getRequestMatchers() {

        List<String> permitAllMatcher = securityMatcherConfigurer.getPermitAllList();

        if (CollectionUtils.isNotEmpty(permitAllMatcher)) {
            LinkedHashMap<HerodotusRequest, List<HerodotusConfigAttribute>> result = new LinkedHashMap<>();
            permitAllMatcher.forEach(item -> {
                result.put(new HerodotusRequest(item), List.of(new HerodotusConfigAttribute(PermissionExpression.PERMIT_ALL.getValue())));
            });
            return result;
        }
        return new LinkedHashMap<>();
    }

    /**
     * 各个服务静态化配置的权限过滤，通常为通配符型或者全路径型，很少有站位符型。即：大多数情况为 {@code Category.WILDCARD} 和 {@code Category.PLACEHOLDER}，很少有 {@code Category.FULL_PATH}
     * <p>
     * 此处的逻辑是：
     * 1. 先处理各个服务静态化配置的权限，当前假设不会有{@code Category.FULL_PATH}类型的权限。后期如果该种权限较多再补充即可。
     * 同时，静态服务都是开发人员手工配置，假定手工配置时就会对是否冲突进行处理，当然也可能出现冲突，那么这个开发人员得多不负责。
     * 2. 经过考虑，服务本地接口扫描完，就对所有的 RequestMapping 做一遍解析，现在感觉意义不大。
     * 因为，RequestMapping 汇总至 UPMS 后，还会做一次统一的分发。所以当前的设计思路是不对 RequestMapping 进行处理。后续根据需要再补充即可。
     */
    public void processRequestMatchers() {

        log.debug("[Herodotus] |- [3] Process local configured security metadata.");

        LinkedHashMap<HerodotusRequest, List<HerodotusConfigAttribute>> requestMatchers = getRequestMatchers();
        if (MapUtils.isNotEmpty(requestMatchers)) {
            Map<Category, LinkedHashMap<HerodotusRequest, List<HerodotusConfigAttribute>>> grouping = groupSecurityMatchers(requestMatchers);

            LinkedHashMap<HerodotusRequest, List<HerodotusConfigAttribute>> wildcards = grouping.get(Category.WILDCARD);
            securityMetadataSourceStorage.addToStorage(wildcards, false);

            LinkedHashMap<HerodotusRequest, List<HerodotusConfigAttribute>> fullPaths = grouping.get(Category.FULL_PATH);
            securityMetadataSourceStorage.addToStorage(fullPaths, true);
        }
    }

    /**
     * 处理分发的 SecurityAttribute，将其转换、解析为表达式权限，并存入本地缓存，用于权限校验
     * <p>
     * 处理过程中，会根据规则对权限类型分组，然后进行去重的操作。
     *
     * @param securityAttributes 权限数据
     */
    public void processSecurityAttribute(List<SecurityAttribute> securityAttributes) {

        // 从缓存中获取全部带有特殊字符的匹配规则
        LinkedHashMap<HerodotusRequest, List<HerodotusConfigAttribute>> compatibles = securityMetadataSourceStorage.getCompatible();
        // 创建一个临时的 Matcher 容器
        LinkedHashMap<HerodotusRequest, List<HerodotusConfigAttribute>> matchers = new LinkedHashMap<>(compatibles);

        // 对分发的 SecurityAttribute 进行分组
        Map<Category, LinkedHashMap<HerodotusRequest, List<HerodotusConfigAttribute>>> grouping = groupingSecurityMetadata(securityAttributes);

        // 拿到带有通配符的分组数据
        LinkedHashMap<HerodotusRequest, List<HerodotusConfigAttribute>> wildcards = grouping.get(Category.WILDCARD);
        if (MapUtils.isNotEmpty(wildcards)) {
            matchers.putAll(wildcards);
        }

        // 拿到带有占位符的分组数据，并检测是否存在冲突的匹配规则，然后将结果存入本地存储
        LinkedHashMap<HerodotusRequest, List<HerodotusConfigAttribute>> placeholders = grouping.get(Category.PLACEHOLDER);
        log.debug("[Herodotus] |- Store placeholder type security attributes.");
        securityMetadataSourceStorage.addToStorage(matchers, placeholders, false);

        // 拿到全路径的分组数据，并检测是否存在冲突的匹配规则，然后将结果存入本地存储
        LinkedHashMap<HerodotusRequest, List<HerodotusConfigAttribute>> fullPaths = grouping.get(Category.FULL_PATH);
        log.debug("[Herodotus] |- Store full path type security attributes.");
        securityMetadataSourceStorage.addToStorage(matchers, fullPaths, true);

        log.debug("[Herodotus] |- [7] Security attributes process has FINISHED!");
    }
}
