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

package com.taotao.cloud.sys.biz.shortlink.src.main.java.com.taotao.cloud.shortlink.biz.shortlink.repository.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.log.api.api.dto.DomainDTO;
import com.taotao.cloud.log.api.api.enums.BooleanEnum;
import com.taotao.cloud.log.api.api.enums.ShortLinkDomainTypeEnum;
import com.taotao.cloud.shortlink.biz.shortlink.repository.mapper.DomainMapper;
import com.taotao.cloud.shortlink.biz.shortlink.repository.model.Domain;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * This is Description
 *
 * @since 2022/05/03
 */
@Service
public class DomainManager extends ServiceImpl<DomainMapper, Domain> {

    @Resource
    private DomainMapper domainMapper;

    public Optional<DomainDTO> findDomain(Long accountId, Long domainId, Integer domainType) {
        QueryWrapper<Domain> queryWrapper = new QueryWrapper<Domain>()
                .eq(Domain.COL_ID, domainId)
                .eq(Domain.COL_DOMAIN_TYPE, domainType)
                .eq(Domain.COL_IS_DELETED, BooleanEnum.FALSE.getCode());

        List<Domain> domains = domainMapper.selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(domains)) {
            Domain domain = domains.get(0);
            if (ShortLinkDomainTypeEnum.CUSTOMER.getCode().equals(domainType)
                    && !domain.getAccountNo().equals(accountId)) {
                return Optional.empty();
            }

            DomainDTO domainDTO = DomainDTO.builder()
                    .id(domain.getId())
                    .domainType(domain.getDomainType())
                    .accountNo(domain.getAccountNo())
                    .value(domain.getValue())
                    .build();
            return Optional.of(domainDTO);
        }

        return Optional.empty();
    }
}
