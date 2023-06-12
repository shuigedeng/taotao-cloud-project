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

package com.taotao.cloud.im.biz.platform.modules.shake.service.impl;

import com.platform.common.constant.ApiConstant;
import com.platform.common.shiro.ShiroUtils;
import com.platform.common.utils.redis.GeoHashUtils;
import com.platform.common.utils.redis.GeoVo;
import com.platform.modules.chat.domain.ChatUser;
import com.platform.modules.chat.service.ChatUserService;
import com.platform.modules.shake.service.NearService;
import com.platform.modules.shake.vo.NearVo01;
import com.platform.modules.shake.vo.NearVo02;
import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.GeoResult;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/** 附近的人 服务层 */
@Service("nearService")
public class NearServiceImpl implements NearService {

    @Autowired
    private GeoHashUtils geoHashUtils;

    @Resource
    private ChatUserService chatUserService;

    @Override
    public List<NearVo02> doNear(NearVo01 nearVo) {
        sendNear(nearVo);
        return getNear();
    }

    @Override
    public void closeNear() {
        String userId = NumberUtil.toStr(ShiroUtils.getUserId());
        geoHashUtils.remove(ApiConstant.REDIS_NEAR, userId);
    }

    private void sendNear(NearVo01 nearVo) {
        // 当前用户ID
        String userId = NumberUtil.toStr(ShiroUtils.getUserId());
        // 保存坐标
        geoHashUtils.add(ApiConstant.REDIS_NEAR, nearVo.getLongitude(), nearVo.getLatitude(), userId);
    }

    private List<NearVo02> getNear() {
        // 当前用户
        String userId = NumberUtil.toStr(ShiroUtils.getUserId());
        // 100公里内的9999个用户
        List<GeoResult<GeoVo>> geoResults = geoHashUtils.radius(ApiConstant.REDIS_NEAR, userId, 100, 9999);
        // 过滤
        List<String> userList = new ArrayList<>();
        List<NearVo02> dataList = geoResults.stream()
                .collect(
                        ArrayList::new,
                        (x, y) -> {
                            String name = JSONUtil.parseObj(y.getContent()).getStr("name");
                            if (!userId.equals(name)) {
                                userList.add(name);
                                NearVo02 nearVo = new NearVo02()
                                        .setUserId(NumberUtil.parseLong(name))
                                        .setDistance(y.getDistance().getValue())
                                        .setDistanceUnit(y.getDistance().getUnit());
                                x.add(nearVo);
                            }
                        },
                        ArrayList::addAll);
        if (CollectionUtils.isEmpty(userList)) {
            return dataList;
        }
        HashMap<Long, ChatUser> mapList = chatUserService.getByIds(userList).stream()
                .collect(
                        HashMap::new,
                        (x, y) -> {
                            x.put(y.getUserId(), y);
                        },
                        HashMap::putAll);
        // 转换
        dataList.forEach(e -> {
            ChatUser chatUser = ChatUser.initUser(mapList.get(e.getUserId()));
            e.setPortrait(chatUser.getPortrait())
                    .setIntro(chatUser.getIntro())
                    .setNickName(chatUser.getNickName())
                    .setGender(chatUser.getGender());
        });
        return dataList;
    }
}
