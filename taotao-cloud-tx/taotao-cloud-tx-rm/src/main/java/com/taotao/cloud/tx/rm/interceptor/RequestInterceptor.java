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

package com.taotao.cloud.tx.rm.interceptor;

import com.taotao.cloud.tx.rm.transactional.TtcTxParticipant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

// 自定义的拦截器：负责传递事务组ID和事务数量的拦截器(其他RPC框架使用)
/**
 * RequestInterceptor
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
@Component
public class RequestInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler ) {
        String groupId = request.getHeader("groupId");
        String transactionalCount = request.getHeader("transactionalCount");
        // 如果上游服务传递了组ID，获取并放入自身的ThreadLocal中
        if (null != groupId || !"".equals(groupId)) {
            TtcTxParticipant.setCurrentGroupId(groupId);
            TtcTxParticipant.setTransactionCount(
                    Integer.valueOf(transactionalCount == null ? "0" : transactionalCount));
        }

        return true;
    }

    @Override
    public void postHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            ModelAndView modelAndView )
            throws Exception {
        //        String groupId = request.getHeader("groupId");
        //        System.out.println("中置拦截器：" + groupId);
        // 拦截事务参与者和事务管理者之间的数据包
        // 从中获取当前的事务组ID、事务数量
        //        System.out.println("我进入中部拦截器啦.......");
        //        System.err.println("拦截器："+Thread.currentThread().getId());
        //
        //        String groupId = request.getHeader("groupId");
        //        String transactionalCount = request.getHeader("transactionalCount");

        //        if (TtcTxParticipant.getCurrentGroupId() == null){
        //            if (null != groupId){
        //                // 把获取到的事务组ID，设置到前面的ThreadLocal对象中
        //                TtcTxParticipant.setCurrentGroupId(groupId);
        //                TtcTxParticipant.setTransactionCount(
        //                        Integer.valueOf(transactionalCount == null ? "0" :
        // transactionalCount));
        //            }
        //        }
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex )
            throws Exception {
        //        // 当RPC调用结束后，会进入后置处理器
        //        System.out.println("我进入后置拦截器啦.......");
        //        System.err.println("后置拦截器：" + TtcTxParticipant.getCurrentGroupId());
    }
}
