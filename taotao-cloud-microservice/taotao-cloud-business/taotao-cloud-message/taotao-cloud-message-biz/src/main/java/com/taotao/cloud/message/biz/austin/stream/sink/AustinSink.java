package com.taotao.cloud.message.biz.austin.stream.sink;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.text.StrPool;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import com.taotao.cloud.message.biz.austin.common.constant.AustinConstant;
import com.taotao.cloud.message.biz.austin.common.domain.AnchorInfo;
import com.taotao.cloud.message.biz.austin.common.domain.SimpleAnchorInfo;
import com.taotao.cloud.message.biz.austin.stream.utils.LettuceRedisUtils;
import io.lettuce.core.RedisFuture;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 消息进 redis/hive
 *
 * @author shuigedeng
 */
@Slf4j
public class AustinSink implements SinkFunction<AnchorInfo> {

    @Override
    public void invoke(AnchorInfo anchorInfo, Context context){
        realTimeData(anchorInfo);
    }


    /**
     * 实时数据存入Redis
     * 1.用户维度(查看用户当天收到消息的链路详情)，数量级大，只保留当天
     * 2.消息模板维度(查看消息模板整体下发情况)，数量级小，保留30天
     *
     * @param info
     */
    private void realTimeData(AnchorInfo info) {
        try {
            LettuceRedisUtils.pipeline(redisAsyncCommands -> {
                List<RedisFuture<?>> redisFutures = new ArrayList<>();

                /**
                 * 0.构建messageId维度的链路信息 数据结构list:{key,list}
                 * key:Austin:MessageId:{messageId},listValue:[{timestamp,state,businessId},{timestamp,state,businessId}]
                 */
                String redisMessageKey = CharSequenceUtil.join(StrPool.COLON, AustinConstant.CACHE_KEY_PREFIX, AustinConstant.MESSAGE_ID, info.getMessageId());
                SimpleAnchorInfo messageAnchorInfo = SimpleAnchorInfo.builder().businessId(info.getBusinessId()).state(info.getState()).timestamp(info.getLogTimestamp()).build();
                redisFutures.add(redisAsyncCommands.lpush(redisMessageKey.getBytes(StandardCharsets.UTF_8), JSON.toJSONString(messageAnchorInfo).getBytes(StandardCharsets.UTF_8)));
                redisFutures.add(redisAsyncCommands.expire(redisMessageKey.getBytes(StandardCharsets.UTF_8), Duration.ofDays(3).toMillis() / 1000));

                /**
                 * 1.构建userId维度的链路信息 数据结构list:{key,list}
                 * key:userId,listValue:[{timestamp,state,businessId},{timestamp,state,businessId}]
                 */
                SimpleAnchorInfo userAnchorInfo = SimpleAnchorInfo.builder().businessId(info.getBusinessId()).state(info.getState()).timestamp(info.getLogTimestamp()).build();
                for (String id : info.getIds()) {
                    redisFutures.add(redisAsyncCommands.lpush(id.getBytes(StandardCharsets.UTF_8), JSON.toJSONString(userAnchorInfo).getBytes(StandardCharsets.UTF_8)));
                    redisFutures.add(redisAsyncCommands.expire(id.getBytes(StandardCharsets.UTF_8), (DateUtil.endOfDay(new Date()).getTime() - DateUtil.current()) / 1000));
                }

                /**
                 * 2.构建消息模板维度的链路信息 数据结构hash:{key,hash}
                 * key:businessId,hashValue:{state,stateCount}
                 */
                redisFutures.add(redisAsyncCommands.hincrby(String.valueOf(info.getBusinessId()).getBytes(StandardCharsets.UTF_8),
                        String.valueOf(info.getState()).getBytes(StandardCharsets.UTF_8), info.getIds().size()));
                redisFutures.add(redisAsyncCommands.expire(String.valueOf(info.getBusinessId()).getBytes(StandardCharsets.UTF_8),
                        ((DateUtil.offsetDay(new Date(), 30).getTime()) / 1000) - DateUtil.currentSeconds()));

                return redisFutures;
            });

        } catch (Exception e) {
            log.error("AustinSink#invoke error: {}", Throwables.getStackTraceAsString(e));
        }
    }
}
