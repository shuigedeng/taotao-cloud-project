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

package com.taotao.cloud.sa.just.biz.sa.sso.h5;

import cn.dev33.satoken.sso.SaSsoConsts;
import cn.dev33.satoken.sso.SaSsoUtil;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaFoxUtil;
import cn.dev33.satoken.util.SaResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 前后台分离架构下集成SSO所需的代码 （SSO-Server端）
 *
 * <p>（注：如果不需要前后端分离架构下集成SSO，可删除此包下所有代码）
 *
 * @author kong
 */
@RestController
public class H5Controller {

    /** 获取 redirectUrl */
    @RequestMapping("/sso/getRedirectUrl")
    private Object getRedirectUrl(String redirect, String mode) {
        // 未登录情况下，返回 code=401
        if (StpUtil.isLogin() == false) {
            return SaResult.code(401);
        }
        // 已登录情况下，构建 redirectUrl
        if (SaSsoConsts.MODE_SIMPLE.equals(mode)) {
            // 模式一
            SaSsoUtil.checkRedirectUrl(SaFoxUtil.decoderUrl(redirect));
            return SaResult.data(redirect);
        } else {
            // 模式二或模式三
            String redirectUrl = SaSsoUtil.buildRedirectUrl(StpUtil.getLoginId(), "", redirect);
            return SaResult.data(redirectUrl);
        }
    }

    // 全局异常拦截
    @ExceptionHandler
    public SaResult handlerException(Exception e) {
        LogUtils.error(e);
        return SaResult.error(e.getMessage());
    }
}
