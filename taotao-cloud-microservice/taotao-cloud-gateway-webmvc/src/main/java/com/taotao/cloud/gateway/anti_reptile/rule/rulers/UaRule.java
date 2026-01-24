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

package com.taotao.cloud.gateway.anti_reptile.rule.rulers;

import com.taotao.boot.common.utils.log.LogUtils;
import com.taotao.cloud.gateway.anti_reptile.AntiReptileProperties;
import com.taotao.cloud.gateway.anti_reptile.rule.AbstractRule;
import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.web.server.ServerWebExchange;

/**
 * UaRule
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
public class UaRule extends AbstractRule {

    private final AntiReptileProperties properties;

    public UaRule( AntiReptileProperties properties ) {
        this.properties = properties;
    }

    @Override
    protected boolean doExecute( ServerWebExchange exchange ) {
        AntiReptileProperties.UaRule uaRule = properties.getUaRule();
        String requestUrl = exchange.getRequest().getURI().getRawPath();

        UserAgent userAgent =
                UserAgent.parseUserAgentString(
                        exchange.getRequest().getHeaders().get("User-Agent").get(0));
        OperatingSystem os = userAgent.getOperatingSystem();
        OperatingSystem osGroup = userAgent.getOperatingSystem().getGroup();
        DeviceType deviceType = userAgent.getOperatingSystem().getDeviceType();

        if (DeviceType.UNKNOWN.equals(deviceType)) {
            LogUtils.info(
                    "Intercepted request, uri: "
                            + requestUrl
                            + " Unknown device, User-Agent: "
                            + userAgent.toString());
            return true;
        } else if (OperatingSystem.UNKNOWN.equals(os)
                || OperatingSystem.UNKNOWN_MOBILE.equals(os)
                || OperatingSystem.UNKNOWN_TABLET.equals(os)) {
            LogUtils.info(
                    "Intercepted request, uri: "
                            + requestUrl
                            + " Unknown OperatingSystem, User-Agent: "
                            + userAgent.toString());
            return true;
        }

        if (!uaRule.isAllowedLinux()
                && ( OperatingSystem.LINUX.equals(osGroup) || OperatingSystem.LINUX.equals(os) )) {
            LogUtils.info(
                    "Intercepted request, uri: "
                            + requestUrl
                            + " Not Allowed Linux request, User-Agent: "
                            + userAgent.toString());
            return true;
        }

        if (!uaRule.isAllowedMobile()
                && ( DeviceType.MOBILE.equals(deviceType) || DeviceType.TABLET.equals(deviceType) )) {
            LogUtils.info(
                    "Intercepted request, uri: "
                            + requestUrl
                            + " Not Allowed Mobile Device request, User-Agent: "
                            + userAgent.toString());
            return true;
        }

        if (!uaRule.isAllowedPc() && DeviceType.COMPUTER.equals(deviceType)) {
            LogUtils.info(
                    "Intercepted request, uri: "
                            + requestUrl
                            + " Not Allowed PC request, User-Agent: "
                            + userAgent.toString());
            return true;
        }

        if (!uaRule.isAllowedIot()
                && ( DeviceType.DMR.equals(deviceType)
                || DeviceType.GAME_CONSOLE.equals(deviceType)
                || DeviceType.WEARABLE.equals(deviceType) )) {
            LogUtils.info(
                    "Intercepted request, uri: "
                            + requestUrl
                            + " Not Allowed Iot Device request, User-Agent: "
                            + userAgent.toString());
            return true;
        }

        if (!uaRule.isAllowedProxy() && OperatingSystem.PROXY.equals(os)) {
            LogUtils.info(
                    "Intercepted request, uri: "
                            + requestUrl
                            + " Not Allowed Proxy request, User-Agent: "
                            + userAgent.toString());
            return true;
        }
        return false;
    }

    @Override
    public void reset( ServerWebExchange exchange, String realRequestUri ) {
        return;
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
