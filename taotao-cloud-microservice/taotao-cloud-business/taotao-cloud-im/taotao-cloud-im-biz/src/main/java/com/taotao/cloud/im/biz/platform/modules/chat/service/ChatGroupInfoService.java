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

package com.taotao.cloud.im.biz.platform.modules.chat.service;

import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.service.BaseService;
import com.platform.modules.chat.domain.ChatGroupInfo;
import java.util.List;
import java.util.Map;

/** 服务层 q3z3 */
public interface ChatGroupInfoService extends BaseService<ChatGroupInfo> {

    /** 查询详情 */
    ChatGroupInfo getGroupInfo(Long groupId, Long userId, YesOrNoEnum verify);

    /** 删除缓存 */
    void delGroupInfoCache(Long groupId, List<Long> userList);

    /** 查询数量 */
    Long countByGroup(Long groupId);

    /** 查询用户id */
    List<Long> queryUserList(Long groupId);

    /** 查询用户id */
    List<ChatGroupInfo> queryUserList(Long groupId, List<Long> userList);

    /** 查询用户id */
    Map<Long, ChatGroupInfo> queryUserMap(Long groupId);

    /** 通过群组删除 */
    void delByGroup(Long groupId);
}
