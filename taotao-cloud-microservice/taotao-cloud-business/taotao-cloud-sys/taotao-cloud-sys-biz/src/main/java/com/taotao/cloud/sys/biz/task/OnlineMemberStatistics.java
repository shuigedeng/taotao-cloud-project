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

package com.taotao.cloud.sys.biz.task;

import com.taotao.cloud.cache.redis.repository.RedisRepository;
import com.taotao.cloud.common.enums.UserEnum;
import com.taotao.cloud.job.xxl.timetask.EveryHourExecute;
import com.taotao.cloud.report.api.model.vo.OnlineMemberVO;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** 实时在线人数统计 */
@Component
public class OnlineMemberStatistics implements EveryHourExecute {

    /** 缓存 */
    @Autowired
    private RedisRepository redisRepository;
    /** 统计小时 */
    @Autowired
    private StatisticsProperties statisticsProperties;

    @Override
    public void execute() {

        Calendar calendar = Calendar.getInstance();

        List<OnlineMemberVO> onlineMemberVOS =
                (List<OnlineMemberVO>) redisRepository.get(CachePrefix.ONLINE_MEMBER.getPrefix());

        if (onlineMemberVOS == null) {
            onlineMemberVOS = new ArrayList<>();
        }

        // 过滤 有效统计时间
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - statisticsProperties.getOnlineMember());
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Calendar finalCalendar = calendar;
        onlineMemberVOS = onlineMemberVOS.stream()
                .filter(onlineMemberVO -> onlineMemberVO.getDate().after(finalCalendar.getTime()))
                .toList();

        // 计入新数据
        calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        onlineMemberVOS.add(new OnlineMemberVO(
                calendar.getTime(),
                redisRepository
                        .keys(CachePrefix.ACCESS_TOKEN.getPrefix(UserEnum.MEMBER) + "*")
                        .size()));

        // 写入缓存
        redisRepository.set(CachePrefix.ONLINE_MEMBER.getPrefix(), onlineMemberVOS);
    }

    /**
     * 手动设置某一时间，活跃人数
     *
     * @param time 时间
     * @param num 人数
     */
    public void execute(Date time, Integer num) {

        List<OnlineMemberVO> onlineMemberVOS =
                (List<OnlineMemberVO>) redisRepository.get(CachePrefix.ONLINE_MEMBER.getPrefix());

        if (onlineMemberVOS == null) {
            onlineMemberVOS = new ArrayList<>();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        // 过滤 有效统计时间
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - 48);

        onlineMemberVOS = onlineMemberVOS.stream()
                .filter(onlineMemberVO -> onlineMemberVO.getDate().after(calendar.getTime()))
                .toList();
        onlineMemberVOS.add(new OnlineMemberVO(time, num));

        // 写入缓存
        redisRepository.set(CachePrefix.ONLINE_MEMBER.getPrefix(), onlineMemberVOS);
    }
}
