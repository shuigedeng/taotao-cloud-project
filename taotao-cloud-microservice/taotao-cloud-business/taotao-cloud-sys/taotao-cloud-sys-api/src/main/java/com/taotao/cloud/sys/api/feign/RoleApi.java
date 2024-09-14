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

package com.taotao.cloud.sys.api.feign;

import com.taotao.boot.common.constant.ServiceName;
import com.taotao.cloud.sys.api.feign.fallback.RoleApiFallback;
import com.taotao.cloud.sys.api.feign.response.RoleQueryApiResponse;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 远程调用后台角色模块
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-25 14:09:31
 */
@FeignClient(
        name = ServiceName.TAOTAO_CLOUD_SYS,
        contextId = "IFeignRoleApi",
        fallbackFactory = RoleApiFallback.class)
public interface RoleApi {

    /**
     * 根据用户id获取角色列表
     *
     * @param userId 用户id
     * @return 角色列表
     * @since 2020/10/21 15:13
     */
    @GetMapping("/sys/feign/role/info/userId")
    List<RoleQueryApiResponse> findRoleByUserId(@RequestParam(value = "userId") Long userId);
}
