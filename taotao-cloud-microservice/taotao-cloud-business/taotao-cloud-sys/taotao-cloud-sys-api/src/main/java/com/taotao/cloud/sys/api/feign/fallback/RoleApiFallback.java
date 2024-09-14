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

package com.taotao.cloud.sys.api.feign.fallback;

import com.taotao.boot.common.utils.log.LogUtils;
import com.taotao.cloud.sys.api.feign.RoleApi;
import com.taotao.cloud.sys.api.feign.response.RoleQueryApiResponse;
import java.util.List;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * RemoteLogFallbackImpl
 *
 * @author shuigedeng
 * @since 2020/4/29 21:43
 */
public class RoleApiFallback implements FallbackFactory<RoleApi> {
    @Override
    public RoleApi create(Throwable throwable) {
        return new RoleApi() {
            @Override
            public List<RoleQueryApiResponse> findRoleByUserId(Long userId) {
                LogUtils.error("调用findUserInfoByUsername异常：{}", throwable, userId);
                return null;
            }
        };
    }
}
