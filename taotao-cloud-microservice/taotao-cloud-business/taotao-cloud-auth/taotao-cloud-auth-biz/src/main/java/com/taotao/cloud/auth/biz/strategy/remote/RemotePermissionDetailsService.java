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

package com.taotao.cloud.auth.biz.strategy.remote;

import com.taotao.cloud.auth.biz.strategy.AbstractStrategyPermissionDetailsService;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>远程权限服务 </p>
 *
 */
public class RemotePermissionDetailsService extends AbstractStrategyPermissionDetailsService {

    //    private final RemoteAuthorityDetailsService remoteAuthorityDetailsService;
    //
    //    public RemotePermissionDetailsService(RemoteAuthorityDetailsService
    // remoteAuthorityDetailsService) {
    //        this.remoteAuthorityDetailsService = remoteAuthorityDetailsService;
    //    }

    @Override
    public List<TtcPermission> findAll() {
        //        Result<List<SysPermission>> result = remoteAuthorityDetailsService.findAll();
        //        List<SysPermission> authorities = result.getData();
        //        if (CollectionUtils.isNotEmpty(authorities)) {
        //            return toEntities(authorities);
        //        }
        return new ArrayList<>();
    }
}
