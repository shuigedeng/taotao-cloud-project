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

package com.taotao.cloud.message.biz.austin.stream.utils;

import com.taotao.cloud.message.biz.austin.stream.callback.RedisPipelineCallBack;
import com.taotao.cloud.message.biz.austin.stream.constants.AustinFlinkConstant;
import io.lettuce.core.LettuceFutures;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.codec.ByteArrayCodec;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author 3y
 * @date 2022/2/22 无Spring环境下使用Redis，基于Lettuce封装
 */
public class LettuceRedisUtils {

    /** 初始化 redisClient */
    private static RedisClient redisClient;

    static {
        RedisURI redisUri = RedisURI.Builder.redis(AustinFlinkConstant.REDIS_IP)
                .withPort(Integer.valueOf(AustinFlinkConstant.REDIS_PORT))
                .withPassword(AustinFlinkConstant.REDIS_PASSWORD.toCharArray())
                .build();
        redisClient = RedisClient.create(redisUri);
    }

    /** 封装pipeline操作 */
    public static void pipeline(RedisPipelineCallBack pipelineCallBack) {
        StatefulRedisConnection<byte[], byte[]> connect = redisClient.connect(new ByteArrayCodec());
        RedisAsyncCommands<byte[], byte[]> commands = connect.async();

        List<RedisFuture<?>> futures = pipelineCallBack.invoke(commands);

        commands.flushCommands();
        LettuceFutures.awaitAll(10, TimeUnit.SECONDS, futures.toArray(new RedisFuture[futures.size()]));
        connect.close();
    }
}
