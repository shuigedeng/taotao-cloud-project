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

package com.taotao.cloud.tenant.biz.infrastructure.repository;

import com.taotao.boot.common.enums.GlobalStatusEnum;
import com.taotao.boot.common.utils.id.IdGeneratorUtils;
import com.taotao.boot.ddd.model.domain.repository.light.BaseLightDomainRepository;
import com.taotao.cloud.tenant.biz.application.dto.command.TenantAddCommand;
import com.taotao.cloud.tenant.biz.domain.aggregate.TenantAgg;
import com.taotao.cloud.tenant.biz.domain.repository.TenantDomainRepository;
import com.taotao.cloud.tenant.biz.domain.valobj.CodeVal;
import com.taotao.cloud.tenant.biz.infrastructure.persistent.mapper.TenantMapper;
import com.taotao.cloud.tenant.biz.infrastructure.persistent.mapper.TenantPackageMapper;
import com.taotao.cloud.tenant.biz.infrastructure.persistent.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * DeptDomainRepositoryImpl
 *
 * @author shuigedeng
 * @version 2026.04
 * @since 2025-12-19 09:30:45
 */
@Service
@RequiredArgsConstructor
public class TenantDomainRepositoryImpl extends BaseLightDomainRepository<TenantAgg, TenantMapper> implements
	TenantDomainRepository {

	private final TenantPackageMapper tenantPackageMapper;
	private final TenantRepository tenantRepository;

	@Override
	public void insert( TenantAddCommand tenantAddCommand ) {
		TenantAgg tenantAgg1 = new TenantAgg();
		CodeVal codeVal = new CodeVal();
		codeVal.setApplyNo(IdGeneratorUtils.getIdStr());
		tenantAgg1.setTenantAdminId( 1L);
		tenantAgg1.setCodeVal( codeVal);
		tenantAgg1.setName("xxx");
		tenantAgg1.setStatus(GlobalStatusEnum.ENABLE);
		tenantAgg1.setExpireTime(LocalDateTime.now());
		tenantAgg1.setAccountCount(1);

//		selfMapper.insert(tenantAgg1);

		codeVal.setApplyNo(IdGeneratorUtils.getIdStr());
		tenantAgg1.setCodeVal(codeVal);
		tenantRepository.save(tenantAgg1);
	}

	@Override
	public void select() {
//		TenantAgg tenantAgg = selfMapper.selectById(1L);
		Optional<TenantAgg> byId = tenantRepository.findById(2L);
		System.out.println("=============");
	}

}
