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

package com.taotao.cloud.im.biz.platform.modules.push.vo;

import com.platform.common.enums.YesOrNoEnum;
import com.platform.modules.chat.enums.FriendTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

/** 消息发送人 */
@Data
@Accessors(chain = true) // 链式调用
public class PushParamVo {

    /** 用户id */
    private Long userId;

    /** 用户头像 */
    private String portrait;

    /** 用户昵称 */
    private String nickName;

    /** 发送内容 */
    private String content;

    /** 是否静默 */
    private YesOrNoEnum disturb = YesOrNoEnum.NO;

    /** 是否置顶 */
    private YesOrNoEnum top = YesOrNoEnum.NO;

    /** 好友类型 */
    private FriendTypeEnum userType = FriendTypeEnum.NORMAL;

    /** 临时参数 */
    private Long toId;
}
