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

import com.platform.common.web.service.BaseService;
import com.platform.modules.chat.domain.ChatFriend;
import com.platform.modules.chat.vo.*;
import java.util.List;

/** 好友表 服务层 q3z3 */
public interface ChatFriendService extends BaseService<ChatFriend> {

    /** 搜索好友 */
    FriendVo07 findFriend(String param);

    /** 添加好友 */
    void applyFriend(FriendVo02 friendVo);

    /** 同意申请 */
    void agree(Long applyId);

    /** 拒绝申请 */
    void refused(Long applyId);

    /** 忽略申请 */
    void ignore(Long applyId);

    /** 设置黑名单 */
    void setBlack(FriendVo03 friendVo);

    /** 删除好友 */
    void delFriend(Long friendId);

    /** 设置备注 */
    void setRemark(FriendVo05 friendVo);

    /** 是否置顶 */
    void setTop(FriendVo09 friendVo);

    /** 好友列表 */
    List<FriendVo06> friendList(String param);

    /** 好友详情 */
    FriendVo07 getInfo(Long friendId);

    /** 获取好友信息 */
    ChatFriend getFriend(Long userId, Long friendId);

    /** 查询好友id */
    List<Long> queryFriendId(Long userId);
}
