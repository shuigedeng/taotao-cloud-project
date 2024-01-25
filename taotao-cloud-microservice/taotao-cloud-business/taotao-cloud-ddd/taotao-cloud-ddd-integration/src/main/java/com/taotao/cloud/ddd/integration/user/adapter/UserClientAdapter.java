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

package com.taotao.cloud.ddd.integration.user.adapter;

import com.taotao.cloud.ddd.biz.integration.user.vo.UserAddressVO;
import com.taotao.cloud.ddd.biz.integration.user.vo.UserBaseInfoVO;
import com.taotao.cloud.ddd.biz.integration.user.vo.UserContactVO;

public class UserClientAdapter {

    public UserBaseInfoVO convert(UserInfoClientDTO userInfo) {
        // 基础信息
        UserBaseInfoVO userBaseInfo = new UserBaseInfoVO();
        // 联系方式
        UserContactVO contactVO = new UserContactVO();
        contactVO.setMobile(userInfo.getMobile());
        userBaseInfo.setContactInfo(contactVO);
        // 地址信息
        UserAddressVO addressVO = new UserAddressVO();
        addressVO.setCityCode(userInfo.getCityCode());
        addressVO.setAddressDetail(userInfo.getAddressDetail());
        userBaseInfo.setAddressInfo(addressVO);
        return userBaseInfo;
    }
}
