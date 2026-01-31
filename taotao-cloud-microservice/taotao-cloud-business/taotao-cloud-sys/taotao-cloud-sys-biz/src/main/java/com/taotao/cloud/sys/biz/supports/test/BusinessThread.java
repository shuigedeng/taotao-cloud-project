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

package com.taotao.cloud.sys.biz.supports.test;

import com.taotao.boot.common.utils.log.LogUtils;

//@Component
// @Scope("prototype")//spring 多例
/**
 * BusinessThread
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
public class BusinessThread implements Runnable {

    private String acceptStr;

    public BusinessThread( String acceptStr ) {
        this.acceptStr = acceptStr;
    }

    public String getAcceptStr() {
        return acceptStr;
    }

    public void setAcceptStr( String acceptStr ) {
        this.acceptStr = acceptStr;
    }

    @Override
    public void run() {
        // 业务操作
        LogUtils.info("多线程已经处理订单插入系统，订单号：" + acceptStr);

        // 线程阻塞
        /*try {
            Thread.sleep(1000);
            LogUtils.info("多线程已经处理订单插入系统，订单号："+acceptStr);
        } catch (InterruptedException e) {
            LogUtils.error(e);
        }*/
    }
}
