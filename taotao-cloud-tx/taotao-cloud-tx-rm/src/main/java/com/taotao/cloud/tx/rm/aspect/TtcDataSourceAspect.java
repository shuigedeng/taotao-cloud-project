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

package com.taotao.cloud.tx.rm.aspect;

import com.taotao.cloud.tx.rm.connection.TtcConnection;
import com.taotao.cloud.tx.rm.transactional.TtcTxParticipant;

import java.sql.Connection;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

// 剥夺并接管Spring事务控制权的切面
/**
 * TtcDataSourceAspect
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
@Aspect
@Component
public class TtcDataSourceAspect {

    @Around("execution(* javax.sql.DataSource.getConnection(..))")
    public Connection dataSourceAround( ProceedingJoinPoint proceedingJoinPoint ) throws Throwable {
        System.out.println("事务切面成功拦截，正在接管控制权......");

        // 如果当前调用事务接口的线程正在参与分布式事务，
        // 则返回自定义的Connection对象接管事务控制权
        if (TtcTxParticipant.getCurrent() != null) {
            System.out.println("返回自定义的Connection对象.......");
            Connection connection = (Connection) proceedingJoinPoint.proceed();
            return new TtcConnection(connection, TtcTxParticipant.getCurrent());
        }

        // 如果当前线程没有参与分布式事务，让其正常提交/回滚事务
        System.out.println("返回JDBC的Connection对象.............");
        return (Connection) proceedingJoinPoint.proceed();
    }
}
