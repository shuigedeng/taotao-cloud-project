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
import lombok.Data;
import lombok.experimental.Accessors;

/** 普通消息 */
@Data
@Accessors(chain = true) // 链式调用
public class PushMsgVo {

    /** 是否置顶 */
    private String top = YesOrNoEnum.NO.getCode();

    /** 免打扰 */
    private String disturb = YesOrNoEnum.NO.getCode();

    /** 消息类型 */
    private String msgType;

    /** 消息内容 */
    private String content;
}
