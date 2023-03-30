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

import com.platform.modules.chat.vo.FriendVo06;
import com.platform.modules.chat.vo.FriendVo07;
import com.platform.modules.push.vo.PushParamVo;
import java.util.List;

/** 系统聊天 服务层 q3z3 */
public interface ChatTalkService {

    /** 查询好友列表 */
    List<FriendVo06> queryFriendList();

    /** 查询好友详情 */
    FriendVo07 queryFriendInfo(Long userId);

    /** 发送聊天 */
    PushParamVo talk(Long userId, String content);
}
