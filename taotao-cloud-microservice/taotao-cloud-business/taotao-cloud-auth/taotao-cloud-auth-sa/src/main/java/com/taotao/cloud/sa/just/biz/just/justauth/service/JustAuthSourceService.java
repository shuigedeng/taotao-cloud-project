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
import com.taotao.cloud.sa.just.biz.just.justauth.dto.CreateJustAuthSourceDTO;
import com.taotao.cloud.sa.just.biz.just.justauth.dto.JustAuthSourceDTO;
import com.taotao.cloud.sa.just.biz.just.justauth.dto.QueryJustAuthSourceDTO;
import com.taotao.cloud.sa.just.biz.just.justauth.dto.UpdateJustAuthSourceDTO;
import com.taotao.cloud.sa.just.biz.just.justauth.entity.JustAuthSource;
import java.util.List;

/**
 * 租户第三方登录信息配置表 服务类
 *
 * @since 2022-05-19
 */
public interface JustAuthSourceService extends IService<JustAuthSource> {

    /**
     * 分页查询租户第三方登录信息配置表列表
     *
     * @param page
     * @param queryJustAuthSourceDTO
     * @return
     */
    Page<JustAuthSourceDTO> queryJustAuthSourceList(
            Page<JustAuthSourceDTO> page, QueryJustAuthSourceDTO queryJustAuthSourceDTO);

    /**
     * 查询租户第三方登录信息配置表列表
     *
     * @param queryJustAuthSourceDTO
     * @return
     */
    List<JustAuthSourceDTO> queryJustAuthSourceList(QueryJustAuthSourceDTO queryJustAuthSourceDTO);

    /**
     * 查询租户第三方登录信息配置表详情
     *
     * @param queryJustAuthSourceDTO
     * @return
     */
    JustAuthSourceDTO queryJustAuthSource(QueryJustAuthSourceDTO queryJustAuthSourceDTO);

    /**
     * 创建租户第三方登录信息配置表
     *
     * @param justAuthSource
     * @return
     */
    boolean createJustAuthSource(CreateJustAuthSourceDTO justAuthSource);

    /**
     * 更新租户第三方登录信息配置表
     *
     * @param justAuthSource
     * @return
     */
    boolean updateJustAuthSource(UpdateJustAuthSourceDTO justAuthSource);

    /**
     * 删除租户第三方登录信息配置表
     *
     * @param justAuthSourceId
     * @return
     */
    boolean deleteJustAuthSource(Long justAuthSourceId);

    /**
     * 批量删除租户第三方登录信息配置表
     *
     * @param justAuthSourceIds
     * @return
     */
    boolean batchDeleteJustAuthSource(List<Long> justAuthSourceIds);

    /**
     * 初始化配置表列表
     *
     * @return
     */
    void initJustAuthSourceList();
}
