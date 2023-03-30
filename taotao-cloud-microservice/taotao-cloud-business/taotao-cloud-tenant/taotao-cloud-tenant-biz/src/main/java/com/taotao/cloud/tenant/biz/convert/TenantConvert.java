/*
 * COPYRIGHT (C) 2022 Art AUTHORS(cloud@gmail.com). ALL RIGHTS RESERVED.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.tenant.biz.convert;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.taotao.cloud.tenant.api.model.dto.TenantDTO;
import com.taotao.cloud.tenant.biz.entity.TenantDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author 
 * @version 0.0.1
 * @date 2022/11/26 14:31
 */
@Mapper
public interface TenantConvert {

	TenantConvert INSTANCE = Mappers.getMapper(TenantConvert.class);

	TenantDO convert(TenantDTO tenant);

	TenantDTO convert(TenantDO tenant);

	List<TenantDTO> convert(List<TenantDO> tenantDOList);

	Page<TenantDTO> convert(Page<TenantDO> tenantDOList);

}