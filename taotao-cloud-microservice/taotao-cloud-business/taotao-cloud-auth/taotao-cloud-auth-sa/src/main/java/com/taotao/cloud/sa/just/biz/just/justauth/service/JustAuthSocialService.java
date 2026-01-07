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
import com.taotao.cloud.sa.just.biz.just.justauth.dto.CreateJustAuthSocialDTO;
import com.taotao.cloud.sa.just.biz.just.justauth.dto.JustAuthSocialDTO;
import com.taotao.cloud.sa.just.biz.just.justauth.dto.QueryJustAuthSocialDTO;
import com.taotao.cloud.sa.just.biz.just.justauth.dto.UpdateJustAuthSocialDTO;
import com.taotao.cloud.sa.just.biz.just.justauth.entity.JustAuthSocial;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * 第三方用户信息 服务类
 *
 * @since 2022-05-23
 */
public interface JustAuthSocialService extends IService<JustAuthSocial> {

    /**
     * 分页查询第三方用户信息列表
     *
     * @param page
     * @param queryJustAuthSocialDTO
     * @return
     */
    Page<JustAuthSocialDTO> queryJustAuthSocialList(
            Page<JustAuthSocialDTO> page, QueryJustAuthSocialDTO queryJustAuthSocialDTO);

    /**
     * 查询第三方用户信息列表
     *
     * @param queryJustAuthSocialDTO
     * @return
     */
    List<JustAuthSocialDTO> queryJustAuthSocialList(QueryJustAuthSocialDTO queryJustAuthSocialDTO);

    /**
     * 查询第三方用户信息详情
     *
     * @param queryJustAuthSocialDTO
     * @return
     */
    JustAuthSocialDTO queryJustAuthSocial(QueryJustAuthSocialDTO queryJustAuthSocialDTO);

    /**
     * 查询第三方用户绑定的系统用户id
     *
     * @param justAuthSocialDTO
     * @return
     */
    Long queryUserIdBySocial(@Param("justAuthSocial") QueryJustAuthSocialDTO justAuthSocialDTO);

    /**
     * 创建第三方用户信息
     *
     * @param justAuthSocial
     * @return
     */
    JustAuthSocial createJustAuthSocial(CreateJustAuthSocialDTO justAuthSocial);

    /**
     * 更新第三方用户信息
     *
     * @param justAuthSocial
     * @return
     */
    boolean updateJustAuthSocial(UpdateJustAuthSocialDTO justAuthSocial);

    /**
     * 创建或第三方用户信息
     *
     * @param justAuthSocial
     * @return
     */
    JustAuthSocial createOrUpdateJustAuthSocial(UpdateJustAuthSocialDTO justAuthSocial);

    /**
     * 删除第三方用户信息
     *
     * @param justAuthSocialId
     * @return
     */
    boolean deleteJustAuthSocial(Long justAuthSocialId);

    /**
     * 批量删除第三方用户信息
     *
     * @param justAuthSocialIds
     * @return
     */
    boolean batchDeleteJustAuthSocial(List<Long> justAuthSocialIds);
}
