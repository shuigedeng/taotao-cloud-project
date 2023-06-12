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
import com.platform.modules.chat.domain.ChatGroup;
import com.platform.modules.chat.vo.GroupVo02;
import com.platform.modules.chat.vo.GroupVo03;
import com.platform.modules.chat.vo.GroupVo07;
import com.platform.modules.chat.vo.GroupVo08;
import com.platform.modules.push.vo.PushParamVo;
import java.util.List;

/** 群组 服务层 q3z3 */
public interface ChatGroupService extends BaseService<ChatGroup> {

    /** 建群 */
    void createGroup(List<Long> list);

    /** 群详情 */
    Dict getInfo(Long groupId);

    /** 邀请群组 */
    void invitationGroup(Long groupId, List<Long> list);

    /** 踢出群组 */
    void kickedGroup(Long groupId, List<Long> list);

    /** 获取群二维码 */
    String getGroupQrCode(Long groupId);

    /** 修改置顶 */
    void editTop(Long groupId, YesOrNoEnum top);

    /** 修改免打扰 */
    void editDisturb(Long groupId, YesOrNoEnum disturb);

    /** 修改保存群组 */
    void editKeepGroup(Long groupId, YesOrNoEnum keepGroup);

    /** 退出群组 */
    void logoutGroup(Long groupId);

    /** 解散群组 */
    void removeGroup(Long groupId);

    /** 扫码查询 */
    GroupVo07 scanCode(String param);

    /** 加入群组 */
    void joinGroup(Long groupId);

    /** 查询群列表 */
    List<GroupVo08> groupList();

    /** 查询用户id */
    List<PushParamVo> queryFriendPushFrom(Long groupId, String content);

    /** 查询用户id */
    List<PushParamVo> queryGroupPushFrom(Long groupId, List<Long> list, String content);

    /** 修改群名 */
    void editGroupName(GroupVo02 groupVo);

    /** 修改群公告 */
    void editGroupNotice(GroupVo03 groupVo);
}
