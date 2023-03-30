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

package com.taotao.cloud.im.biz.platform.modules.push.service;

import com.platform.modules.push.enums.PushMsgTypeEnum;
import com.platform.modules.push.enums.PushNoticeTypeEnum;
import com.platform.modules.push.vo.PushParamVo;
import java.util.List;

/** 用户推送 服务层 q3z3 */
public interface ChatPushService {

    /** 注册别名 */
    void setAlias(Long userId, String cid);

    /** 解除别名 */
    void delAlias(Long userId, String cid);

    /** 发送消息 */
    void pushMsg(PushParamVo from, PushMsgTypeEnum msgType);

    /** 发送消息 */
    void pushMsg(List<PushParamVo> userList, PushMsgTypeEnum msgType);

    /** 发送消息 */
    void pushMsg(List<PushParamVo> userList, PushParamVo group, PushMsgTypeEnum msgType);

    /** 拉取离线消息 */
    void pullOffLine(Long userId);

    /** 发送通知 */
    void pushNotice(PushParamVo paramVo, PushNoticeTypeEnum pushNoticeType);

    /** 发送通知 */
    void pushNotice(List<PushParamVo> userList, PushNoticeTypeEnum pushNoticeType);
}
