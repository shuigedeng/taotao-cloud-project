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

package com.taotao.cloud.workflow.biz.common.util;

import com.github.yitter.contract.IdGeneratorOptions;
import com.github.yitter.idgen.YitIdHelper;
import java.util.Random;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

/** */
@Component
@Slf4j
public class RandomUtil {

    private static final String ID_IDX = CacheKeyUtil.IDGENERATOR + "Index_";

    @Autowired
    private RedisTemplate<String, Long> redisTemplate;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    // ID缓存有效时间 定时刷新有效期
    private static long CacheTimeout = 60 * 60 * 24;
    // 30分钟续期一次 如果Redis被清空可以早点续期
    private static long ScheduleTimeout = 60 * 30;
    private static byte WorkerIdBitLength = 16;
    // 65535 参数为shot 最大值为Short.MAX_VALUE
    private static int MaxWorkerIdNumberByMode =
            (1 << WorkerIdBitLength) - 1 > Short.MAX_VALUE ? Short.MAX_VALUE : (1 << WorkerIdBitLength) - 1;
    private static ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;

    private short workerId = -1;
    private String cacheKey;

    /** 初始化雪花生成器WorkerID， 通过Redis实现集群获取不同的编号， 如果相同会出现ID重复 */
    @Bean
    private void initIdWorker() {
        redisTemplate = SpringContext.getBean("redisTemplate");
        redisTemplate = SpringContext.getBean("redisTemplate");
        if (redisTemplate != null) {
            RedisAtomicLong redisAtomicLong = new RedisAtomicLong(ID_IDX, redisTemplate.getConnectionFactory());
            for (int i = 0; i <= MaxWorkerIdNumberByMode; i++) {
                long andInc = redisAtomicLong.getAndIncrement();
                long result = andInc % (MaxWorkerIdNumberByMode + 1);
                // 计数超出上限之后重新计数
                if (andInc >= MaxWorkerIdNumberByMode) {
                    redisAtomicLong.set(andInc % (MaxWorkerIdNumberByMode));
                }
                cacheKey = ID_IDX + result;
                boolean useSuccess = redisTemplate
                        .opsForValue()
                        .setIfAbsent(cacheKey, System.currentTimeMillis(), CacheTimeout, TimeUnit.SECONDS);
                if (useSuccess) {
                    workerId = (short) result;
                    break;
                }
            }
            if (workerId == -1) {
                throw new RuntimeException(String.format("已尝试生成%d个ID生成器编号, 无法获取到可用编号", MaxWorkerIdNumberByMode + 1));
            }
        } else {
            workerId = (short) new Random().nextInt(MaxWorkerIdNumberByMode);
        }
        log.info("当前ID生成器编号: " + workerId);
        IdGeneratorOptions options = new IdGeneratorOptions(workerId);
        options.WorkerIdBitLength = WorkerIdBitLength;
        YitIdHelper.setIdGenerator(options);
        scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(
                1, threadPoolTaskExecutor.getThreadPoolExecutor().getThreadFactory());
        // 提前一分钟续期
        scheduledThreadPoolExecutor.scheduleWithFixedDelay(
                resetExpire, ScheduleTimeout, ScheduleTimeout, TimeUnit.SECONDS);
    }

    private Runnable resetExpire = () -> {
        // 重新设值, 如果Redis被意外清空或者掉线可以把当前编号重新锁定
        redisTemplate.opsForValue().set(cacheKey, System.currentTimeMillis(), CacheTimeout, TimeUnit.SECONDS);
    };

    @PreDestroy
    private void onDestroy() {
        // 正常关闭时删除当前生成器编号
        if (redisTemplate != null) {
            redisTemplate.delete(cacheKey);
        }
    }

    /**
     * 生成主键id
     *
     * @return
     */
    public static String uuId() {
        long newId = YitIdHelper.nextId();
        return newId + "";
    }

    /**
     * 生成6位数随机英文
     *
     * @return
     */
    public static String enUuid() {
        String str = "";
        for (int i = 0; i < 6; i++) {
            // 你想生bai成几个字符的，du就把3改成zhi几，dao如果改成１,那就生成一个1653随机字母．
            str = str + (char) (Math.random() * 26 + 'a');
        }
        return str;
    }

    /**
     * 生成排序编码
     *
     * @return
     */
    public static Long parses() {
        Long time = 0L;
        return time;
    }

    /**
     * 生成短信验证码
     *
     * @return
     */
    public static String getRandomCode() {
        String code = "";
        Random rand = new Random();
        for (int i = 0; i < 6; i++) {
            int ran = rand.nextInt(10);
            code = code + ran;
        }
        return code;
    }
}
