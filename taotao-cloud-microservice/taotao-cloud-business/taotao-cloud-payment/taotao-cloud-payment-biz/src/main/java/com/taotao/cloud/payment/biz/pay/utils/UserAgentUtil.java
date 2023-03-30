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

package com.taotao.cloud.payment.biz.pay.utils;

import com.xhuicloud.common.core.constant.CommonConstants;
import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpHeaders;

/**
 * @program: XHuiCloud
 * @description: UserAgentUtil
 * @author: Sinda
 * @create: 2020-06-08 11:24
 */
@UtilityClass
public class UserAgentUtil {

    public Boolean isWeChat(HttpServletRequest request) {
        String UA = request.getHeader(HttpHeaders.USER_AGENT);
        return UA.contains(CommonConstants.MICRO_MESSENGER);
    }
}
