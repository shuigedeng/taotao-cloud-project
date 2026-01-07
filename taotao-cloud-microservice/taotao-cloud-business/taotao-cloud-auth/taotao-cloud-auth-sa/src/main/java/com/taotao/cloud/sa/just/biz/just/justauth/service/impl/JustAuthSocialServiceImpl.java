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
import com.taotao.boot.common.exception.BusinessException;
import com.taotao.cloud.sa.just.biz.just.justauth.dto.CreateJustAuthSocialDTO;
import com.taotao.cloud.sa.just.biz.just.justauth.dto.JustAuthSocialDTO;
import com.taotao.cloud.sa.just.biz.just.justauth.dto.QueryJustAuthSocialDTO;
import com.taotao.cloud.sa.just.biz.just.justauth.dto.UpdateJustAuthSocialDTO;
import com.taotao.cloud.sa.just.biz.just.justauth.entity.JustAuthSocial;
import com.taotao.cloud.sa.just.biz.just.justauth.mapper.JustAuthSocialMapper;
import com.taotao.cloud.sa.just.biz.just.justauth.service.JustAuthSocialService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 第三方用户信息 服务实现类
 *
 * @since 2022-05-23
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class JustAuthSocialServiceImpl extends ServiceImpl<JustAuthSocialMapper, JustAuthSocial>
        implements JustAuthSocialService {

    private final JustAuthSocialMapper justAuthSocialMapper;

    /**
     * 分页查询第三方用户信息列表
     *
     * @param page
     * @param queryJustAuthSocialDTO
     * @return
     */
    @Override
    public Page<JustAuthSocialDTO> queryJustAuthSocialList(
            Page<JustAuthSocialDTO> page, QueryJustAuthSocialDTO queryJustAuthSocialDTO) {
        Page<JustAuthSocialDTO> justAuthSocialInfoList =
                justAuthSocialMapper.queryJustAuthSocialList(page, queryJustAuthSocialDTO);
        return justAuthSocialInfoList;
    }

    /**
     * 查询第三方用户信息列表
     *
     * @param queryJustAuthSocialDTO
     * @return
     */
    @Override
    public List<JustAuthSocialDTO> queryJustAuthSocialList(QueryJustAuthSocialDTO queryJustAuthSocialDTO) {
        List<JustAuthSocialDTO> justAuthSocialInfoList =
                justAuthSocialMapper.queryJustAuthSocialList(queryJustAuthSocialDTO);
        return justAuthSocialInfoList;
    }

    /**
     * 查询第三方用户信息详情
     *
     * @param queryJustAuthSocialDTO
     * @return
     */
    @Override
    public JustAuthSocialDTO queryJustAuthSocial(QueryJustAuthSocialDTO queryJustAuthSocialDTO) {
        JustAuthSocialDTO justAuthSocialDTO = justAuthSocialMapper.queryJustAuthSocial(queryJustAuthSocialDTO);
        return justAuthSocialDTO;
    }

    @Override
    public Long queryUserIdBySocial(QueryJustAuthSocialDTO justAuthSocialDTO) {
        return justAuthSocialMapper.queryUserIdBySocial(justAuthSocialDTO);
    }

    /**
     * 创建第三方用户信息
     *
     * @param justAuthSocial
     * @return
     */
    @Override
    public JustAuthSocial createJustAuthSocial(CreateJustAuthSocialDTO justAuthSocial) {
        JustAuthSocial justAuthSocialEntity = BeanCopierUtils.copyByClass(justAuthSocial, JustAuthSocial.class);
        this.save(justAuthSocialEntity);
        return justAuthSocialEntity;
    }

    /**
     * 更新第三方用户信息
     *
     * @param justAuthSocial
     * @return
     */
    @Override
    public boolean updateJustAuthSocial(UpdateJustAuthSocialDTO justAuthSocial) {
        JustAuthSocial justAuthSocialEntity = BeanCopierUtils.copyByClass(justAuthSocial, JustAuthSocial.class);
        boolean result = this.updateById(justAuthSocialEntity);
        return result;
    }

    /**
     * 创建或第三方用户信息
     *
     * @param justAuthSocial
     * @return
     */
    @Override
    public JustAuthSocial createOrUpdateJustAuthSocial(UpdateJustAuthSocialDTO justAuthSocial) {
        boolean result;
        JustAuthSocial justAuthSocialEntity = BeanCopierUtils.copyByClass(justAuthSocial, JustAuthSocial.class);
        QueryJustAuthSocialDTO queryJustAuthSocialDTO = new QueryJustAuthSocialDTO();
        queryJustAuthSocialDTO.setSource(justAuthSocial.getSource());
        queryJustAuthSocialDTO.setUuid(justAuthSocial.getUuid());
        JustAuthSocialDTO justAuthSocialDTO = this.queryJustAuthSocial(queryJustAuthSocialDTO);
        if (null == justAuthSocialDTO) {
            result = this.save(justAuthSocialEntity);
        } else {
            justAuthSocialEntity.setId(justAuthSocialDTO.getId());
            result = this.updateById(justAuthSocialEntity);
        }
        if (!result) {
            throw new BusinessException("第三方用户信息保存失败");
        }

        return justAuthSocialEntity;
    }

    /**
     * 删除第三方用户信息
     *
     * @param justAuthSocialId
     * @return
     */
    @Override
    public boolean deleteJustAuthSocial(Long justAuthSocialId) {
        boolean result = this.removeById(justAuthSocialId);
        return result;
    }

    /**
     * 批量删除第三方用户信息
     *
     * @param justAuthSocialIds
     * @return
     */
    @Override
    public boolean batchDeleteJustAuthSocial(List<Long> justAuthSocialIds) {
        boolean result = this.removeByIds(justAuthSocialIds);
        return result;
    }
}
