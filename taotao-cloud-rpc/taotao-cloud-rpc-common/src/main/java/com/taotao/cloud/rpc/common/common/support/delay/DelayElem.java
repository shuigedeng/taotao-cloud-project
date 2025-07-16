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

package com.taotao.cloud.rpc.common.common.support.delay;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 延迟对象元素
 * @since 0.1.7
 * @author shuigedeng
 */
public class DelayElem implements Delayed {
    /**
     * 延迟时间
     */
    private final long delayMills;

    /**
     * 到期时间
     */
    private final long expire;

    /**
     * 数据
     */
    private final Runnable msg;

    public DelayElem(long delayMills, Runnable msg) {
        this.delayMills = delayMills;
        this.msg = msg;
        // 到期时间 = 当前时间+延迟时间
        this.expire = System.currentTimeMillis() + this.delayMills;
    }

    public long delayMills() {
        return delayMills;
    }

    public Runnable msg() {
        return msg;
    }

    /**
     * 需要实现的接口，获得延迟时间
     *
     * 用过期时间-当前时间
     * @param unit 时间单位
     * @return 延迟时间
     */
    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(this.expire - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    /**
     * 用于延迟队列内部比较排序
     * <p>
     * 当前时间的延迟时间 - 比较对象的延迟时间
     *
     * @param o 比较对象
     * @return 结果
     */
    @Override
    public int compareTo(Delayed o) {
        return (int) (this.getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS));
    }

    @Override
    public String toString() {
        return "DelayElem{"
                + "delayMills="
                + delayMills
                + ", expire="
                + expire
                + ", msg='"
                + msg
                + '\''
                + '}';
    }
}
