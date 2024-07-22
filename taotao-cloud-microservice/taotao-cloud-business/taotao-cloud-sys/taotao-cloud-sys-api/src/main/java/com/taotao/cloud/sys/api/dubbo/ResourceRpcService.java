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

package com.taotao.cloud.sys.api.dubbo;

import com.taotao.cloud.sys.api.dubbo.request.MenuQueryRpcRequest;
import java.util.List;

/**
 * 后台菜单服务接口
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-25 14:13:19
 */
public interface ResourceRpcService {

    /**
     * 根据id获取菜单信息
     *
     * @param id id
     * @return 菜单信息
     * @since 2022-03-25 14:13:34
     */
    List<MenuQueryRpcRequest> queryAllById(Long id);
}
