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

package com.taotao.cloud.im.biz.platform.modules.chat.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.platform.common.constant.ApiConstant;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.exception.BaseException;
import com.platform.common.utils.redis.RedisUtils;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.dao.ChatGroupInfoDao;
import com.platform.modules.chat.domain.ChatGroupInfo;
import com.platform.modules.chat.service.ChatGroupInfoService;
import jakarta.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/** 服务层实现 q3z3 */
@Service("chatGroupInfoService")
public class ChatGroupInfoServiceImpl extends BaseServiceImpl<ChatGroupInfo> implements ChatGroupInfoService {

    @Resource
    private ChatGroupInfoDao chatGroupInfoDao;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatGroupInfoDao);
    }

    @Override
    public List<ChatGroupInfo> queryList(ChatGroupInfo t) {
        List<ChatGroupInfo> dataList = chatGroupInfoDao.queryList(t);
        return dataList;
    }

    @Override
    public ChatGroupInfo getGroupInfo(Long groupId, Long userId, YesOrNoEnum verify) {
        String key = StrUtil.format(ApiConstant.REDIS_GROUP_INFO, groupId, userId);
        ChatGroupInfo info;
        // 缓存存在
        if (redisUtils.hasKey(key)) {
            info = JSONUtil.toBean(redisUtils.get(key), ChatGroupInfo.class);
        }
        // 缓存不存在
        else if ((info = this.queryOne(new ChatGroupInfo().setUserId(userId).setGroupId(groupId))) != null) {
            redisUtils.set(key, JSONUtil.toJsonStr(info), ApiConstant.REDIS_GROUP_TIME, TimeUnit.DAYS);
        }
        if (YesOrNoEnum.NO.equals(verify)) {
            return info;
        }
        if (info == null) {
            throw new BaseException("你不在当前群中");
        }
        if (YesOrNoEnum.YES.equals(info.getKicked())) {
            throw new BaseException("你已被踢出");
        }
        return info;
    }

    @Override
    public void delGroupInfoCache(Long groupId, List<Long> userList) {
        userList.forEach(e -> {
            redisUtils.delete(StrUtil.format(ApiConstant.REDIS_GROUP_INFO, groupId, e));
        });
    }

    @Override
    public Long countByGroup(Long groupId) {
        return queryCount(new ChatGroupInfo().setGroupId(groupId).setKicked(YesOrNoEnum.NO));
    }

    @Override
    public List<Long> queryUserList(Long groupId) {
        // 查询所有成员
        List<ChatGroupInfo> infoList =
                this.queryList(new ChatGroupInfo().setGroupId(groupId).setKicked(YesOrNoEnum.NO));
        return infoList.stream().map(ChatGroupInfo::getUserId).toList();
    }

    @Override
    public List<ChatGroupInfo> queryUserList(Long groupId, List<Long> userList) {
        List<ChatGroupInfo> dataList =
                this.queryList(new ChatGroupInfo().setGroupId(groupId).setKicked(YesOrNoEnum.NO));
        if (!CollectionUtils.isEmpty(userList)) {
            dataList = dataList.stream()
                    .filter(data -> userList.contains(data.getUserId()))
                    .toList();
        }
        return dataList;
    }

    @Override
    public Map<Long, ChatGroupInfo> queryUserMap(Long groupId) {
        // 查询所有成员
        List<ChatGroupInfo> dataList = this.queryList(new ChatGroupInfo().setGroupId(groupId));
        return dataList.stream().collect(Collectors.toMap(ChatGroupInfo::getUserId, a -> a, (k1, k2) -> k1));
    }

    @Override
    public void delByGroup(Long groupId) {
        chatGroupInfoDao.delete(new QueryWrapper<>(new ChatGroupInfo().setGroupId(groupId)));
        // 删除群二维码
        redisUtils.delete(ApiConstant.REDIS_QR_CODE + groupId);
        // 删除群成员
        String key = StrUtil.format(ApiConstant.REDIS_GROUP_INFO, groupId, "*");
        redisUtils.delete(key);
    }
}
