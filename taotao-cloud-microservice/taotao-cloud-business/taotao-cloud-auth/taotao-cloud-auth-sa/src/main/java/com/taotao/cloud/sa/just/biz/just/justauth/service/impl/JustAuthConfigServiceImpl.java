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

package com.taotao.cloud.sa.just.biz.just.justauth.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.sa.just.biz.just.justauth.dto.CreateJustAuthConfigDTO;
import com.taotao.cloud.sa.just.biz.just.justauth.dto.JustAuthConfigDTO;
import com.taotao.cloud.sa.just.biz.just.justauth.dto.QueryJustAuthConfigDTO;
import com.taotao.cloud.sa.just.biz.just.justauth.dto.UpdateJustAuthConfigDTO;
import com.taotao.cloud.sa.just.biz.just.justauth.entity.JustAuthConfig;
import com.taotao.cloud.sa.just.biz.just.justauth.mapper.JustAuthConfigMapper;
import com.taotao.cloud.sa.just.biz.just.justauth.service.JustAuthConfigService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/**
 * 租户第三方登录功能配置表 服务实现类
 *
 * @since 2022-05-16
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class JustAuthConfigServiceImpl extends ServiceImpl<JustAuthConfigMapper, JustAuthConfig>
        implements JustAuthConfigService {

    private final JustAuthConfigMapper justAuthConfigMapper;

    private final RedisTemplate redisTemplate;

    /** 是否开启租户模式 */
    @Value("${tenant.enable}")
    private Boolean enable;

    /**
     * 分页查询租户第三方登录功能配置表列表
     *
     * @param page
     * @param queryJustAuthConfigDTO
     * @return
     */
    @Override
    public Page<JustAuthConfigDTO> queryJustAuthConfigList(
            Page<JustAuthConfigDTO> page, QueryJustAuthConfigDTO queryJustAuthConfigDTO) {
        Page<JustAuthConfigDTO> justAuthConfigInfoList =
                justAuthConfigMapper.queryJustAuthConfigList(page, queryJustAuthConfigDTO);
        return justAuthConfigInfoList;
    }

    /**
     * 查询租户第三方登录功能配置表列表
     *
     * @param queryJustAuthConfigDTO
     * @return
     */
    @Override
    public List<JustAuthConfigDTO> queryJustAuthConfigList(QueryJustAuthConfigDTO queryJustAuthConfigDTO) {
        List<JustAuthConfigDTO> justAuthConfigInfoList =
                justAuthConfigMapper.queryJustAuthConfigList(queryJustAuthConfigDTO);
        return justAuthConfigInfoList;
    }

    /**
     * 查询租户第三方登录功能配置表详情
     *
     * @param queryJustAuthConfigDTO
     * @return
     */
    @Override
    public JustAuthConfigDTO queryJustAuthConfig(QueryJustAuthConfigDTO queryJustAuthConfigDTO) {
        JustAuthConfigDTO justAuthConfigDTO = justAuthConfigMapper.queryJustAuthConfig(queryJustAuthConfigDTO);
        return justAuthConfigDTO;
    }

    /**
     * 创建租户第三方登录功能配置表
     *
     * @param justAuthConfig
     * @return
     */
    @Override
    public boolean createJustAuthConfig(CreateJustAuthConfigDTO justAuthConfig) {
        JustAuthConfig justAuthConfigEntity = BeanCopierUtils.copyByClass(justAuthConfig, JustAuthConfig.class);
        boolean result = this.save(justAuthConfigEntity);
        if (result) {
            // 新增到缓存
            JustAuthConfig justAuthConfigCreate = this.getById(justAuthConfigEntity.getId());
            JustAuthConfigDTO justAuthConfigDTO =
                    BeanCopierUtils.copyByClass(justAuthConfigCreate, JustAuthConfigDTO.class);
            this.addOrUpdateJustAuthConfigCache(justAuthConfigDTO);
        }
        return result;
    }

    /**
     * 更新租户第三方登录功能配置表
     *
     * @param justAuthConfig
     * @return
     */
    @Override
    public boolean updateJustAuthConfig(UpdateJustAuthConfigDTO justAuthConfig) {
        JustAuthConfig justAuthConfigEntity = BeanCopierUtils.copyByClass(justAuthConfig, JustAuthConfig.class);
        boolean result = this.updateById(justAuthConfigEntity);
        if (result) {
            // 更新到缓存
            JustAuthConfig justAuthConfigUpdate = this.getById(justAuthConfig.getId());
            JustAuthConfigDTO justAuthConfigDTO =
                    BeanCopierUtils.copyByClass(justAuthConfigUpdate, JustAuthConfigDTO.class);
            this.addOrUpdateJustAuthConfigCache(justAuthConfigDTO);
        }
        return result;
    }

    /**
     * 删除租户第三方登录功能配置表
     *
     * @param justAuthConfigId
     * @return
     */
    @Override
    public boolean deleteJustAuthConfig(Long justAuthConfigId) {
        JustAuthConfig justAuthConfigDelete = this.getById(justAuthConfigId);
        // 从数据库删除
        boolean result = this.removeById(justAuthConfigId);
        // 从缓存删除
        if (result) {
            JustAuthConfigDTO justAuthConfigDTO =
                    BeanCopierUtils.copyByClass(justAuthConfigDelete, JustAuthConfigDTO.class);
            this.deleteJustAuthConfigCache(justAuthConfigDTO);
        }
        return result;
    }

    /**
     * 批量删除租户第三方登录功能配置表
     *
     * @param justAuthConfigIds
     * @return
     */
    @Override
    public boolean batchDeleteJustAuthConfig(List<Long> justAuthConfigIds) {
        List<JustAuthConfig> justAuthConfigDeleteList = this.listByIds(justAuthConfigIds);
        boolean result = this.removeByIds(justAuthConfigIds);
        // 从缓存删除
        if (result && !CollectionUtils.isEmpty(justAuthConfigDeleteList)) {
            for (JustAuthConfig justAuthConfigDelete : justAuthConfigDeleteList) {
                JustAuthConfigDTO justAuthConfigDTO =
                        BeanCopierUtils.copyByClass(justAuthConfigDelete, JustAuthConfigDTO.class);
                this.deleteJustAuthConfigCache(justAuthConfigDTO);
            }
        }
        return result;
    }

    /**
     * 初始化配置表列表
     *
     * @return
     */
    @Override
    public void initJustAuthConfigList() {
        QueryJustAuthConfigDTO queryJustAuthConfigDTO = new QueryJustAuthConfigDTO();
        queryJustAuthConfigDTO.setStatus(GitEggConstant.ENABLE);
        List<JustAuthConfigDTO> justAuthSourceInfoList =
                justAuthConfigMapper.initJustAuthConfigList(queryJustAuthConfigDTO);

        // 判断是否开启了租户模式，如果开启了，那么角色权限需要按租户进行分类存储
        if (enable) {
            Map<Long, List<JustAuthConfigDTO>> authSourceListMap =
                    justAuthSourceInfoList.stream().collect(Collectors.groupingBy(JustAuthConfigDTO::getTenantId));
            authSourceListMap.forEach((key, value) -> {
                String redisKey = AuthConstant.SOCIAL_TENANT_CONFIG_KEY + key;
                redisTemplate.delete(redisKey);
                addJustAuthConfig(redisKey, value);
            });

        } else {
            redisTemplate.delete(AuthConstant.SOCIAL_CONFIG_KEY);
            addJustAuthConfig(AuthConstant.SOCIAL_CONFIG_KEY, justAuthSourceInfoList);
        }
    }

    private void addJustAuthConfig(String key, List<JustAuthConfigDTO> configList) {
        Map<String, String> authConfigMap = new TreeMap<>();
        Optional.ofNullable(configList).orElse(new ArrayList<>()).forEach(config -> {
            try {
                authConfigMap.put(
                        enable ? config.getTenantId().toString() : AuthConstant.SOCIAL_DEFAULT,
                        JsonUtils.objToJson(config));
                redisTemplate.opsForHash().putAll(key, authConfigMap);
            } catch (Exception e) {
                log.error("初始化第三方登录失败：{}", e);
            }
        });
    }

    private void addOrUpdateJustAuthConfigCache(JustAuthConfigDTO justAuthConfig) {
        try {
            String redisKey = AuthConstant.SOCIAL_CONFIG_KEY;
            if (enable) {
                redisKey = AuthConstant.SOCIAL_TENANT_CONFIG_KEY + justAuthConfig.getTenantId();
            }
            redisTemplate
                    .opsForHash()
                    .put(
                            redisKey,
                            enable ? justAuthConfig.getTenantId().toString() : AuthConstant.SOCIAL_DEFAULT,
                            JsonUtils.objToJson(justAuthConfig));
        } catch (Exception e) {
            log.error("修改第三方登录缓存失败：{}", e);
        }
    }

    private void deleteJustAuthConfigCache(JustAuthConfigDTO justAuthConfig) {
        try {
            String redisKey = AuthConstant.SOCIAL_CONFIG_KEY;
            if (enable) {
                redisKey = AuthConstant.SOCIAL_TENANT_CONFIG_KEY + justAuthConfig.getTenantId();
            }
            redisTemplate
                    .opsForHash()
                    .delete(
                            redisKey,
                            enable ? justAuthConfig.getTenantId().toString() : AuthConstant.SOCIAL_DEFAULT,
                            JsonUtils.objToJson(justAuthConfig));
        } catch (Exception e) {
            log.error("删除第三方登录缓存失败：{}", e);
        }
    }
}
