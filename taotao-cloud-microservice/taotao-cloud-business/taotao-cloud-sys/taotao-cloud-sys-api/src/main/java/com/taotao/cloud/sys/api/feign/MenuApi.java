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
import com.taotao.cloud.sys.api.feign.fallback.MenuApiFallback;
import com.taotao.cloud.sys.api.feign.response.MenuQueryApiResponse;
import java.util.List;
import java.util.Set;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 远程调用后台菜单模块
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-25 14:09:10
 */
@FeignClient(
        name = ServiceName.TAOTAO_CLOUD_SYS,
        contextId = "IFeignMenuApi",
        fallbackFactory = MenuApiFallback.class)
public interface MenuApi {

    /**
     * 根据角色code列表获取角色列表
     *
     * @param codes 角色code列表
     * @return 角色列表
     * @since 2020/10/21 15:24
     */
    @GetMapping("/sys/feign/menu/info/codes")
    List<MenuQueryApiResponse> findResourceByCodes(@RequestParam(value = "codes") Set<String> codes);
}
