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

package com.taotao.cloud.order.application.config.aop.aftersale;

import com.taotao.boot.common.enums.UserEnum;
import com.taotao.boot.common.utils.log.LogUtils;
import com.taotao.boot.common.utils.spel.SpelUtils;
import com.taotao.cloud.order.application.event.aftersale.AfterSaleLogEvent;
import com.taotao.cloud.order.application.model.entity.aftersale.AfterSaleLog;
import java.util.HashMap;
import java.util.Map;

import com.taotao.boot.security.spring.model.SecurityUser;
import com.taotao.boot.security.spring.utils.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * 订单操作日志
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:56:31
 */
@Aspect
@Component
public class AfterSaleOperationAspect {

    @Autowired
    private ApplicationEventPublisher publisher;

    @AfterReturning(
            returning = "rvt",
            pointcut = "@annotation(com.taotao.cloud.order.application.aop.aftersale.AfterSaleLogPoint)")
    public void afterReturning(JoinPoint joinPoint, Object rvt) {
        try {
            SecurityUser securityUser = SecurityUtils.getCurrentUser();
            // 日志对象拼接
            // 默认操作人员，系统操作
            String userName = "系统操作";
            Long id = -1L;
            String role = UserEnum.SYSTEM.name();
            if (securityUser != null) {
                // 日志对象拼接
                userName = securityUser.getUsername();
                id = securityUser.getUserId();
                role = UserEnum.getByCode(securityUser.getType());
            }

            Map<String, String> afterSaleLogPoints = spelFormat(joinPoint, rvt);
            AfterSaleLog afterSaleLog = new AfterSaleLog(
                    afterSaleLogPoints.get("sn"), id, role, userName, afterSaleLogPoints.get("description"));

            publisher.publishEvent(new AfterSaleLogEvent(afterSaleLog));
        } catch (Exception e) {
            LogUtils.error("售后日志错误", e);
        }
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param joinPoint 切点
     * @return 方法描述
     */
    public static Map<String, String> spelFormat(JoinPoint joinPoint, Object rvt) {
        Map<String, String> result = new HashMap<>(2);
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        AfterSaleLogPoint afterSaleLogPoint = signature.getMethod().getAnnotation(AfterSaleLogPoint.class);
        String description = SpelUtils.compileParams(joinPoint, rvt, afterSaleLogPoint.description());
        String sn = SpelUtils.compileParams(joinPoint, rvt, afterSaleLogPoint.sn());
        result.put("description", description);
        result.put("sn", sn);
        return result;
    }
}
