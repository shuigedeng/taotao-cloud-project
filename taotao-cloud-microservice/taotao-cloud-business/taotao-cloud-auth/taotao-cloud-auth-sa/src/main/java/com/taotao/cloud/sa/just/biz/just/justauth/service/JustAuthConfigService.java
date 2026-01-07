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

package com.taotao.cloud.sa.just.biz.just.justauth.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.sa.just.biz.just.justauth.dto.CreateJustAuthConfigDTO;
import com.taotao.cloud.sa.just.biz.just.justauth.dto.JustAuthConfigDTO;
import com.taotao.cloud.sa.just.biz.just.justauth.dto.QueryJustAuthConfigDTO;
import com.taotao.cloud.sa.just.biz.just.justauth.dto.UpdateJustAuthConfigDTO;
import com.taotao.cloud.sa.just.biz.just.justauth.entity.JustAuthConfig;
import java.util.List;

/**
 * 租户第三方登录功能配置表 服务类
 *
 * @since 2022-05-16
 */
public interface JustAuthConfigService extends IService<JustAuthConfig> {

    /**
     * 分页查询租户第三方登录功能配置表列表
     *
     * @param page
     * @param queryJustAuthConfigDTO
     * @return
     */
    Page<JustAuthConfigDTO> queryJustAuthConfigList(
            Page<JustAuthConfigDTO> page, QueryJustAuthConfigDTO queryJustAuthConfigDTO);

    /**
     * 查询租户第三方登录功能配置表列表
     *
     * @param queryJustAuthConfigDTO
     * @return
     */
    List<JustAuthConfigDTO> queryJustAuthConfigList(QueryJustAuthConfigDTO queryJustAuthConfigDTO);

    /**
     * 查询租户第三方登录功能配置表详情
     *
     * @param queryJustAuthConfigDTO
     * @return
     */
    JustAuthConfigDTO queryJustAuthConfig(QueryJustAuthConfigDTO queryJustAuthConfigDTO);

    /**
     * 创建租户第三方登录功能配置表
     *
     * @param justAuthConfig
     * @return
     */
    boolean createJustAuthConfig(CreateJustAuthConfigDTO justAuthConfig);

    /**
     * 更新租户第三方登录功能配置表
     *
     * @param justAuthConfig
     * @return
     */
    boolean updateJustAuthConfig(UpdateJustAuthConfigDTO justAuthConfig);

    /**
     * 删除租户第三方登录功能配置表
     *
     * @param justAuthConfigId
     * @return
     */
    boolean deleteJustAuthConfig(Long justAuthConfigId);

    /**
     * 批量删除租户第三方登录功能配置表
     *
     * @param justAuthConfigIds
     * @return
     */
    boolean batchDeleteJustAuthConfig(List<Long> justAuthConfigIds);

    /**
     * 初始化配置表列表
     *
     * @return
     */
    void initJustAuthConfigList();
}
