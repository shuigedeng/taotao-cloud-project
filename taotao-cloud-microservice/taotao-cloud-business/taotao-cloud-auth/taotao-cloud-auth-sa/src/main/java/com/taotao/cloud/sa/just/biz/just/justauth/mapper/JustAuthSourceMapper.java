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
import com.taotao.cloud.sa.just.biz.just.justauth.dto.JustAuthSourceDTO;
import com.taotao.cloud.sa.just.biz.just.justauth.dto.QueryJustAuthSourceDTO;
import com.taotao.cloud.sa.just.biz.just.justauth.entity.JustAuthSource;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * 租户第三方登录信息配置表 Mapper 接口
 *
 * @since 2022-05-19
 */
public interface JustAuthSourceMapper extends BaseMapper<JustAuthSource> {

    /**
     * 分页查询租户第三方登录信息配置表列表
     *
     * @param page
     * @param justAuthSourceDTO
     * @return
     */
    Page<JustAuthSourceDTO> queryJustAuthSourceList(
            Page<JustAuthSourceDTO> page, @Param("justAuthSource") QueryJustAuthSourceDTO justAuthSourceDTO);

    /**
     * 查询租户第三方登录信息配置表列表
     *
     * @param justAuthSourceDTO
     * @return
     */
    List<JustAuthSourceDTO> queryJustAuthSourceList(@Param("justAuthSource") QueryJustAuthSourceDTO justAuthSourceDTO);

    /**
     * 查询租户第三方登录信息配置表信息
     *
     * @param justAuthSourceDTO
     * @return
     */
    JustAuthSourceDTO queryJustAuthSource(@Param("justAuthSource") QueryJustAuthSourceDTO justAuthSourceDTO);

    /**
     * 排除多租户插件查询租户第三方登录信息配置表列表
     *
     * @param justAuthSourceDTO
     * @return
     */
    @InterceptorIgnore(tenantLine = "true")
    List<JustAuthSourceDTO> initJustAuthSourceList(@Param("justAuthSource") QueryJustAuthSourceDTO justAuthSourceDTO);
}
