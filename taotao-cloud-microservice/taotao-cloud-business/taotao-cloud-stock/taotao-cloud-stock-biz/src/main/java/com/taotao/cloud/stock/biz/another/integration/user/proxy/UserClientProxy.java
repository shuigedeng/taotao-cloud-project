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

package com.taotao.cloud.stock.biz.another.integration.user.proxy;

import com.taotao.cloud.ddd.biz.integration.user.adapter.UserClientAdapter;
import com.taotao.cloud.stock.biz.another.integration.user.vo.UserBaseInfoVO;

public class UserClientProxy {

    @Resource private UserClientService userClientService;
    @Resource private UserClientAdapter userIntegrationAdapter;

    // 查询用户
    public UserBaseInfoVO getUserInfo(String userId) {
        UserInfoClientDTO user = userClientService.getUserInfo(userId);
        UserBaseInfoVO result = userIntegrationAdapter.convert(user);
        return result;
    }
}
