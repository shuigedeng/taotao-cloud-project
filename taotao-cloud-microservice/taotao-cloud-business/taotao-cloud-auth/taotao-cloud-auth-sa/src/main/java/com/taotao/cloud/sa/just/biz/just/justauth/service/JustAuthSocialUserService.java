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
import com.taotao.cloud.sa.just.biz.just.justauth.dto.CreateJustAuthSocialUserDTO;
import com.taotao.cloud.sa.just.biz.just.justauth.dto.JustAuthSocialUserDTO;
import com.taotao.cloud.sa.just.biz.just.justauth.dto.QueryJustAuthSocialUserDTO;
import com.taotao.cloud.sa.just.biz.just.justauth.dto.UpdateJustAuthSocialUserDTO;
import com.taotao.cloud.sa.just.biz.just.justauth.entity.JustAuthSocialUser;
import java.util.List;

/**
 * 租户第三方登录功能配置表 服务类
 *
 * @since 2022-05-19
 */
public interface JustAuthSocialUserService extends IService<JustAuthSocialUser> {

    /**
     * 分页查询租户第三方登录功能配置表列表
     *
     * @param page
     * @param queryJustAuthSocialUserDTO
     * @return
     */
    Page<JustAuthSocialUserDTO> queryJustAuthSocialUserList(
            Page<JustAuthSocialUserDTO> page, QueryJustAuthSocialUserDTO queryJustAuthSocialUserDTO);

    /**
     * 查询租户第三方登录功能配置表列表
     *
     * @param queryJustAuthSocialUserDTO
     * @return
     */
    List<JustAuthSocialUserDTO> queryJustAuthSocialUserList(QueryJustAuthSocialUserDTO queryJustAuthSocialUserDTO);

    /**
     * 查询租户第三方登录功能配置表详情
     *
     * @param queryJustAuthSocialUserDTO
     * @return
     */
    JustAuthSocialUserDTO queryJustAuthSocialUser(QueryJustAuthSocialUserDTO queryJustAuthSocialUserDTO);

    /**
     * 创建租户第三方登录功能配置表
     *
     * @param justAuthSocialUser
     * @return
     */
    JustAuthSocialUser createJustAuthSocialUser(CreateJustAuthSocialUserDTO justAuthSocialUser);

    /**
     * 更新租户第三方登录功能配置表
     *
     * @param justAuthSocialUser
     * @return
     */
    boolean updateJustAuthSocialUser(UpdateJustAuthSocialUserDTO justAuthSocialUser);

    /**
     * 删除租户第三方登录功能配置表
     *
     * @param justAuthSocialUserId
     * @return
     */
    boolean deleteJustAuthSocialUser(Long justAuthSocialUserId);

    /**
     * 批量删除租户第三方登录功能配置表
     *
     * @param justAuthSocialUserIds
     * @return
     */
    boolean batchDeleteJustAuthSocialUser(List<Long> justAuthSocialUserIds);
}
