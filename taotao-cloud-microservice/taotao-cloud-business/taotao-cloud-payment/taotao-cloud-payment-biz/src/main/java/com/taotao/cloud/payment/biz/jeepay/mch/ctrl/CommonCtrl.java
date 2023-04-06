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

package com.taotao.cloud.payment.biz.jeepay.mch.ctrl;

import com.taotao.cloud.payment.biz.jeepay.core.constants.ApiCodeEnum;
import com.taotao.cloud.payment.biz.jeepay.core.constants.CS;
import com.taotao.cloud.payment.biz.jeepay.core.ctrls.AbstractCtrl;
import com.taotao.cloud.payment.biz.jeepay.core.entity.SysUser;
import com.taotao.cloud.payment.biz.jeepay.core.model.ApiRes;
import com.taotao.cloud.payment.biz.jeepay.core.model.security.JeeUserDetails;
import com.taotao.cloud.payment.biz.jeepay.service.impl.SysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 通用ctrl类
 *
 * @author terrfly
 * @modify zhuxiao
 * @site https://www.jeequan.com
 * @date 2021-04-27 15:50
 */
public abstract class CommonCtrl extends AbstractCtrl {

    @Autowired
    protected SystemYmlConfig mainConfig;

    @Autowired
    private SysConfigService sysConfigService;

    /** 获取当前用户ID */
    protected JeeUserDetails getCurrentUser() {

        return (JeeUserDetails)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /** 获取当前商户ID * */
    protected String getCurrentMchNo() {
        return getCurrentUser().getSysUser().getBelongInfoId();
    }

    /**
     * 获取当前用户登录IP
     *
     * @return
     */
    protected String getIp() {
        return getClientIp();
    }

    /**
     * 校验当前用户是否为超管
     *
     * @return
     */
    protected ApiRes checkIsAdmin() {
        SysUser sysUser = getCurrentUser().getSysUser();
        if (sysUser.getIsAdmin() != CS.YES) {
            return ApiRes.fail(ApiCodeEnum.SYS_PERMISSION_ERROR);
        } else {
            return null;
        }
    }
}
