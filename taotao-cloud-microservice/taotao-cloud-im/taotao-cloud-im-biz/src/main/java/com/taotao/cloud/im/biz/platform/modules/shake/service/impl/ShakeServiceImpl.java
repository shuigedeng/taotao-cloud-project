/**
 * Licensed to the Apache Software Foundation （ASF） under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * （the "License"）； you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p>
 * https://www.q3z3.com
 * QQ : 939313737
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.im.biz.platform.modules.shake.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.NumberUtil;
import com.platform.common.constant.ApiConstant;
import com.platform.common.exception.BaseException;
import com.platform.common.shiro.ShiroUtils;
import com.platform.common.utils.redis.GeoHashUtils;
import com.platform.common.utils.redis.RedisUtils;
import com.platform.modules.chat.domain.ChatUser;
import com.platform.modules.chat.service.ChatUserService;
import com.platform.modules.shake.service.ShakeService;
import com.platform.modules.shake.vo.ShakeVo01;
import com.platform.modules.shake.vo.ShakeVo02;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 摇一摇 服务层
 */
@Service("shakeService")
public class ShakeServiceImpl implements ShakeService {

    @Autowired
    private GeoHashUtils geoHashUtils;

    @Autowired
    private RedisUtils redisUtils;

    @Resource
    private ChatUserService chatUserService;

    private final static String ERR_MSG = "暂无匹配到的结果";

    @Override
    public ShakeVo02 doShake(ShakeVo01 shakeVo) {
        sendShake(shakeVo);
        return getShake();
    }

    private void sendShake(ShakeVo01 shakeVo) {
        // 当前用户ID
        String userId = NumberUtil.toStr(ShiroUtils.getUserId());
        // 保存集合
        redisUtils.lRightPush(ApiConstant.REDIS_SHAKE, userId);
        // 保存经纬度
        geoHashUtils.add(ApiConstant.REDIS_GEO, shakeVo.getLongitude(), shakeVo.getLatitude(), userId);
    }

    private ShakeVo02 getShake() {
        if (!redisUtils.hasKey(ApiConstant.REDIS_SHAKE)) {
            throw new BaseException(ERR_MSG);
        }
        String userId = redisUtils.lLeftPop(ApiConstant.REDIS_SHAKE);
        String current = NumberUtil.toStr(ShiroUtils.getUserId());
        if (current.equals(userId)) {
            Long length = redisUtils.lLen(ApiConstant.REDIS_SHAKE);
            if (length < 10) {
                // 保存集合
                redisUtils.lRightPush(ApiConstant.REDIS_SHAKE, current);
            }
            throw new BaseException(ERR_MSG);
        }
        ChatUser chatUser = ChatUser.initUser(chatUserService.getById(NumberUtil.parseLong(userId)));
        Distance distance = geoHashUtils.dist(ApiConstant.REDIS_GEO, userId, current);
        return BeanUtil.toBean(chatUser, ShakeVo02.class)
                .setDistance(distance.getValue())
                .setDistanceUnit(distance.getUnit());
    }

}
