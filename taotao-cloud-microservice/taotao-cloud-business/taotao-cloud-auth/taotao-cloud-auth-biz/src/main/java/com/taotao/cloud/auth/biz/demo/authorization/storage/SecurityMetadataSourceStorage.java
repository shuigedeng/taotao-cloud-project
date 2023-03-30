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

package com.taotao.cloud.auth.biz.demo.authorization.storage;

import cn.herodotus.engine.cache.jetcache.utils.JetCacheUtils;
import cn.herodotus.engine.oauth2.authorization.definition.HerodotusConfigAttribute;
import cn.herodotus.engine.oauth2.authorization.definition.HerodotusRequestMatcher;
import cn.herodotus.engine.oauth2.core.constants.OAuth2Constants;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import java.util.LinkedHashMap;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * Description: SecurityMetadata 本地存储
 *
 * @author : gengwei.zheng
 * @date : 2021/7/30 15:05
 */
public class SecurityMetadataSourceStorage {

    private static final Logger log = LoggerFactory.getLogger(SecurityMetadataSourceStorage.class);

    /**
     * 模式匹配权限缓存。主要存储 包含 "*"、"?" 和 "{"、"}" 等特殊字符的路径权限。 该种权限，需要通过遍历，利用 AntPathRequestMatcher 机制进行匹配
     */
    private final Cache<String, LinkedHashMap<HerodotusRequestMatcher, HerodotusConfigAttribute>>
            compatible;

    /** 直接索引权限缓存，主要存储全路径权限 该种权限，直接通过 Map Key 进行获取 */
    private final Cache<HerodotusRequestMatcher, HerodotusConfigAttribute> indexable;

    public SecurityMetadataSourceStorage() {
        this.compatible =
                JetCacheUtils.create(
                        OAuth2Constants.CACHE_NAME_SECURITY_METADATA_COMPATIBLE, CacheType.LOCAL);
        this.indexable =
                JetCacheUtils.create(
                        OAuth2Constants.CACHE_NAME_SECURITY_METADATA_INDEXABLE, CacheType.LOCAL);
    }

    private static final String KEY_COMPATIBLE = "COMPATIBLE";

    /**
     * 从 compatible 缓存中读取数据。
     *
     * @return 需要进行模式匹配的权限数据
     */
    private LinkedHashMap<HerodotusRequestMatcher, HerodotusConfigAttribute> readFromCompatible() {
        LinkedHashMap<HerodotusRequestMatcher, HerodotusConfigAttribute> compatible =
                this.compatible.get(KEY_COMPATIBLE);
        if (MapUtils.isNotEmpty(compatible)) {
            return compatible;
        }
        return new LinkedHashMap<>();
    }

    /**
     * 写入 compatible 缓存
     *
     * @param compatible 请求路径和权限配置属性映射Map
     */
    private void writeToCompatible(
            LinkedHashMap<HerodotusRequestMatcher, HerodotusConfigAttribute> compatible) {
        this.compatible.put(KEY_COMPATIBLE, compatible);
    }

    /**
     * 从 indexable 缓存中读取数据
     *
     * @param herodotusRequestMatcher 自定义扩展的 AntPathRequestMatchers {@link HerodotusRequestMatcher}
     * @return 权限配置属性对象集合
     */
    private HerodotusConfigAttribute readFromIndexable(
            HerodotusRequestMatcher herodotusRequestMatcher) {
        return this.indexable.get(herodotusRequestMatcher);
    }

    /**
     * 写入 indexable 缓存
     *
     * @param herodotusRequestMatcher 自定义扩展的 AntPathRequestMatchers {@link HerodotusRequestMatcher}
     * @param herodotusConfigAttribute 权限配置属性
     */
    private void writeToIndexable(
            HerodotusRequestMatcher herodotusRequestMatcher,
            HerodotusConfigAttribute herodotusConfigAttribute) {
        this.indexable.put(herodotusRequestMatcher, herodotusConfigAttribute);
    }

    /**
     * 根据请求的 url 和 method 获取权限对象
     *
     * @param url 请求 URL
     * @param method 请求 method
     * @return 与请求url 和 method 匹配的权限数据，或者是空集合
     */
    public HerodotusConfigAttribute getConfigAttribute(String url, String method) {
        HerodotusRequestMatcher herodotusRequestMatcher = new HerodotusRequestMatcher(url, method);
        return readFromIndexable(herodotusRequestMatcher);
    }

    /**
     * 从 compatible 缓存中获取全部不需要路径匹配的（包含*号的url）请求权限映射Map
     *
     * @return 如果缓存中存在，则返回请求权限映射Map集合，如果不存在则返回一个空的{@link LinkedHashMap}
     */
    public LinkedHashMap<HerodotusRequestMatcher, HerodotusConfigAttribute> getCompatible() {
        return readFromCompatible();
    }

    /**
     * 向 compatible 缓存中添加需要路径匹配的（包含*号的url）请求权限映射Map。
     *
     * <p>如果缓存中不存在以{@link RequestMatcher}为Key的数据，那么添加数据 如果缓存中存在以{@link RequestMatcher}为Key的数据，那么合并数据
     *
     * @param herodotusRequestMatcher 请求匹配对象 {@link HerodotusRequestMatcher}
     * @param configAttributes 权限配置 {@link ConfigAttribute}
     */
    private void appendToCompatible(
            HerodotusRequestMatcher herodotusRequestMatcher,
            HerodotusConfigAttribute configAttributes) {
        LinkedHashMap<HerodotusRequestMatcher, HerodotusConfigAttribute> compatible =
                this.getCompatible();
        //        compatible.merge(requestMatcher, configAttributes, (oldConfigAttributes,
        // newConfigAttributes) -> {
        //            newConfigAttributes.addAll(oldConfigAttributes);
        //            return newConfigAttributes;
        //        });

        // 使用merge会让整个功能的设计更加复杂，暂时改为直接覆盖已有数据，后续视情况再做变更。
        compatible.put(herodotusRequestMatcher, configAttributes);
        log.trace(
                "[Herodotus] |- Append [{}] to Compatible cache, current size is [{}]",
                herodotusRequestMatcher,
                compatible.size());
        writeToCompatible(compatible);
    }

    /**
     * 向 compatible 缓存中添加需要路径匹配的（包含*号的url）请求权限映射Map。
     *
     * <p>如果缓存中不存在以{@link RequestMatcher}为Key的数据，那么添加数据 如果缓存中存在以{@link RequestMatcher}为Key的数据，那么合并数据
     *
     * @param securityMetadata 请求权限映射Map
     */
    private void appendToCompatible(
            LinkedHashMap<HerodotusRequestMatcher, HerodotusConfigAttribute> securityMetadata) {
        securityMetadata.forEach(this::appendToCompatible);
    }

    /**
     * 向 indexable 缓存中添加需请求权限映射。
     *
     * <p>如果缓存中不存在以{@link HerodotusRequestMatcher}为Key的数据，那么添加数据 如果缓存中存在以{@link
     * HerodotusRequestMatcher}为Key的数据，那么合并数据
     *
     * @param herodotusRequestMatcher 请求匹配对象 {@link HerodotusRequestMatcher}
     * @param herodotusConfigAttribute 权限配置 {@link HerodotusConfigAttribute}
     */
    private void appendToIndexable(
            HerodotusRequestMatcher herodotusRequestMatcher,
            HerodotusConfigAttribute herodotusConfigAttribute) {
        writeToIndexable(herodotusRequestMatcher, herodotusConfigAttribute);
    }

    /**
     * 向 indexable 缓存中添加请求权限映射Map。
     *
     * @param securityMetadata 请求权限映射Map
     */
    private void appendToIndexable(
            LinkedHashMap<HerodotusRequestMatcher, HerodotusConfigAttribute> securityMetadata) {
        securityMetadata.forEach(this::appendToIndexable);
    }

    /**
     * 将权限数据添加至本地存储
     *
     * @param securityMetadata 权限数据
     * @param isIndexable true 存入 indexable cache；false 存入 compatible cache
     */
    public void addToStorage(
            LinkedHashMap<HerodotusRequestMatcher, HerodotusConfigAttribute> securityMetadata,
            boolean isIndexable) {
        if (MapUtils.isNotEmpty(securityMetadata)) {
            if (isIndexable) {
                appendToIndexable(securityMetadata);
            } else {
                appendToCompatible(securityMetadata);
            }
        }
    }

    /**
     * 将权限数据添加至本地存储，存储之前进行规则冲突校验
     *
     * @param matchers 校验资源
     * @param securityMetadata 权限数据
     * @param isIndexable true 存入 indexable cache；false 存入 compatible cache
     */
    public void addToStorage(
            LinkedHashMap<HerodotusRequestMatcher, HerodotusConfigAttribute> matchers,
            LinkedHashMap<HerodotusRequestMatcher, HerodotusConfigAttribute> securityMetadata,
            boolean isIndexable) {
        LinkedHashMap<HerodotusRequestMatcher, HerodotusConfigAttribute> result =
                new LinkedHashMap<>();
        if (MapUtils.isNotEmpty(matchers) && MapUtils.isNotEmpty(securityMetadata)) {
            result = checkConflict(matchers, securityMetadata);
        }

        addToStorage(result, isIndexable);
    }

    /**
     * 规则冲突校验
     *
     * <p>如存在规则冲突，则保留可支持最大化范围规则，冲突的其它规则则不保存
     *
     * @param matchers 校验资源
     * @param securityMetadata 权限数据
     * @return 去除冲突的权限数据
     */
    private LinkedHashMap<HerodotusRequestMatcher, HerodotusConfigAttribute> checkConflict(
            LinkedHashMap<HerodotusRequestMatcher, HerodotusConfigAttribute> matchers,
            LinkedHashMap<HerodotusRequestMatcher, HerodotusConfigAttribute> securityMetadata) {

        LinkedHashMap<HerodotusRequestMatcher, HerodotusConfigAttribute> result =
                new LinkedHashMap<>(securityMetadata);

        for (HerodotusRequestMatcher matcher : matchers.keySet()) {
            for (HerodotusRequestMatcher item : securityMetadata.keySet()) {
                if (matcher.matches(item)) {
                    result.remove(item);
                    log.trace(
                            "[Herodotus] |- Pattern [{}] is conflict with [{}], so remove it.",
                            item.getPattern(),
                            matcher.getPattern());
                }
            }
        }

        return result;
    }
}
