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

package com.taotao.cloud.tenant.biz.convert;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.taotao.cloud.tenant.api.model.dto.TenantPackageDTO;
import com.taotao.cloud.tenant.biz.entity.TenantPackage;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 租户包转换
 *
 * @author shuigedeng
 * @version 2023.04
 * @since 2023-05-10 10:58:58
 */
@Mapper
public interface TenantPackageConvert {

    TenantPackageConvert INSTANCE = Mappers.getMapper(TenantPackageConvert.class);

    TenantPackageDTO convert(TenantPackage tenantPackage);

    TenantPackage convert(TenantPackageDTO tenantPackage);

    List<TenantPackageDTO> convert(List<TenantPackage> list);

    Page<TenantPackageDTO> convert(Page<TenantPackage> list);
}
