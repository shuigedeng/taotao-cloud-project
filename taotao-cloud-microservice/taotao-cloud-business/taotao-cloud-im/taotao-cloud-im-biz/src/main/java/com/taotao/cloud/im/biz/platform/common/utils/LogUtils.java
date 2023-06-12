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

package com.taotao.cloud.im.biz.platform.common.utils;

import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.utils.ip.AddressUtils;
import com.platform.common.utils.ip.IpUtils;
import lombok.extern.slf4j.Slf4j;

/** 日志工具类 */
@Slf4j
public class LogUtils {

    /** 记录登陆信息 */
    public static void recordLogin(
            final String username, final YesOrNoEnum status, final String message, final Object... args) {
        final UserAgent userAgent =
                UserAgentUtil.parse(ServletUtils.getRequest().getHeader("User-Agent"));
        final String ipAddr = IpUtils.getIpAddr(ServletUtils.getRequest());
        String ipLocation = AddressUtils.getRealAddressByIP(ipAddr);
        StringBuilder s = new StringBuilder();
        s.append(getBlock(ipAddr));
        s.append(ipLocation);
        s.append(getBlock(username));
        s.append(status.getInfo());
        s.append(getBlock(message));
        // 打印信息到日志
        log.info(s.toString(), args);
        // 获取客户端操作系统
        String os = userAgent.getOs().getName();
        // 获取客户端浏览器
        String browser = userAgent.getBrowser().getName();
        // 封装对象
        //        LogLogin logLogin = new LogLogin();
        //        logLogin.setUserName(username);
        //        logLogin.setIpAddr(ipAddr);
        //        logLogin.setIpLocation(ipLocation);
        //        logLogin.setBrowser(browser);
        //        logLogin.setOs(os);
        //        logLogin.setMsg(message);
        //        // 日志状态
        //        logLogin.setStatus(status);
        //        // 时间
        //        logLogin.setCreateTime(DateUtil.date());
        ThreadUtil.execAsync(() -> {
            // 插入数据
            //            SpringUtil.getBean(LogLoginService.class).add(logLogin);
        });
    }

    private static String getBlock(Object msg) {
        if (msg == null) {
            msg = "";
        }
        return "[" + msg.toString() + "]";
    }
}
