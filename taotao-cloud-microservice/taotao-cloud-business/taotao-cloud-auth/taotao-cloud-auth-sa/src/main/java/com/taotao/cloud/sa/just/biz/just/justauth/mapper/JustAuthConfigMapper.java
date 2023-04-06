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

package com.taotao.cloud.sa.just.biz.just.justauth.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.taotao.cloud.sa.just.biz.just.justauth.dto.JustAuthConfigDTO;
import com.taotao.cloud.sa.just.biz.just.justauth.dto.QueryJustAuthConfigDTO;
import com.taotao.cloud.sa.just.biz.just.justauth.entity.JustAuthConfig;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * 租户第三方登录功能配置表 Mapper 接口
 *
 * @since 2022-05-16
 */
public interface JustAuthConfigMapper extends BaseMapper<JustAuthConfig> {

    /**
     * 分页查询租户第三方登录功能配置表列表
     *
     * @param page
     * @param justAuthConfigDTO
     * @return
     */
    Page<JustAuthConfigDTO> queryJustAuthConfigList(
            Page<JustAuthConfigDTO> page, @Param("justAuthConfig") QueryJustAuthConfigDTO justAuthConfigDTO);

    /**
     * 查询租户第三方登录功能配置表列表
     *
     * @param justAuthConfigDTO
     * @return
     */
    List<JustAuthConfigDTO> queryJustAuthConfigList(@Param("justAuthConfig") QueryJustAuthConfigDTO justAuthConfigDTO);

    /**
     * 查询租户第三方登录功能配置表信息
     *
     * @param justAuthConfigDTO
     * @return
     */
    JustAuthConfigDTO queryJustAuthConfig(@Param("justAuthConfig") QueryJustAuthConfigDTO justAuthConfigDTO);

    /**
     * 查询租户第三方登录功能配置表列表
     *
     * @param justAuthConfigDTO
     * @return
     */
    @InterceptorIgnore(tenantLine = "true")
    List<JustAuthConfigDTO> initJustAuthConfigList(@Param("justAuthConfig") QueryJustAuthConfigDTO justAuthConfigDTO);
}
