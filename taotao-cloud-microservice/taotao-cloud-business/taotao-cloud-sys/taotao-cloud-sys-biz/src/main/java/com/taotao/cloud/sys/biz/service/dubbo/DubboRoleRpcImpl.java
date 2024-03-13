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

package com.taotao.cloud.sys.biz.service.dubbo;

import com.taotao.cloud.sys.api.dubbo.IDubboRoleRpc;
import com.taotao.cloud.sys.biz.mapper.IRoleMapper;
import com.taotao.cloud.sys.biz.model.entity.system.QRole;
import com.taotao.cloud.sys.biz.model.entity.system.Role;
import com.taotao.cloud.sys.biz.repository.cls.RoleRepository;
import com.taotao.cloud.sys.biz.repository.inf.IRoleRepository;
import com.taotao.cloud.sys.biz.service.business.IRoleResourceService;
import com.taotao.cloud.web.base.service.impl.BaseSuperServiceImpl;
import lombok.AllArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

/**
 * RoleServiceImpl
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 20:46:26
 */
@Service
@AllArgsConstructor
@DubboService(interfaceClass = IDubboRoleRpc.class, validation = "true")
public class DubboRoleRpcImpl extends BaseSuperServiceImpl< Role, Long,IRoleMapper, RoleRepository, IRoleRepository>
        implements IDubboRoleRpc {

    private static final QRole ROLE = QRole.role;

//    private final IRoleResourceService roleResourceService;
}
