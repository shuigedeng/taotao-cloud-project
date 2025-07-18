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

package com.taotao.cloud.shortlink.biz.shortlink.repository.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.log.api.api.dto.LinkGroupDTO;
import com.taotao.cloud.log.api.api.enums.BooleanEnum;
import com.taotao.cloud.shortlink.biz.shortlink.repository.mapper.LinkGroupMapper;
import com.taotao.cloud.shortlink.biz.shortlink.repository.model.Domain;
import com.taotao.cloud.shortlink.biz.shortlink.repository.model.LinkGroup;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * This is Description
 *
 * @since 2022/05/03
 */
@Service
public class LinkGroupManager extends ServiceImpl<LinkGroupMapper, LinkGroup> {

    @Resource
    private LinkGroupMapper linkGroupMapper;

    public Optional<LinkGroupDTO> findLinkGroup(Long accountId, Long groupId) {
        QueryWrapper<LinkGroup> queryWrapper = new QueryWrapper<LinkGroup>()
                .eq(LinkGroup.COL_ID, groupId)
                .eq(LinkGroup.COL_ACCOUNT_NO, accountId)
                .eq(Domain.COL_IS_DELETED, BooleanEnum.FALSE.getCode());

        List<LinkGroup> LinkGroups = linkGroupMapper.selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(LinkGroups)) {
            LinkGroup linkGroup = LinkGroups.get(0);

            LinkGroupDTO domainDTO = LinkGroupDTO.builder()
                    .id(linkGroup.getId())
                    .accountNo(linkGroup.getAccountNo())
                    .title(linkGroup.getTitle())
                    .build();
            return Optional.of(domainDTO);
        }

        return Optional.empty();
    }
}
