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

package com.taotao.cloud.im.biz.platform.common.shiro;

import com.platform.common.constant.HeadConstant;
import com.platform.common.shiro.vo.LoginUser;
import com.platform.common.utils.ServletUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

/** Shiro工具类 */
public class ShiroUtils {

    public static Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    public static LoginUser getLoginUser() {
        return (LoginUser) getSubject().getPrincipal();
    }

    public static String getTokenId() {
        LoginUser loginUser = getLoginUser();
        if (loginUser != null) {
            return loginUser.getTokenId();
        }
        return ServletUtils.getRequest().getHeader(HeadConstant.TOKEN_KEY);
    }

    public static String getPhone() {
        LoginUser loginUser = getLoginUser();
        if (loginUser != null) {
            return loginUser.getPhone();
        }
        return null;
    }

    public static Long getUserId() {
        LoginUser loginUser = getLoginUser();
        if (loginUser != null) {
            return loginUser.getUserId();
        }
        return null;
    }
}
