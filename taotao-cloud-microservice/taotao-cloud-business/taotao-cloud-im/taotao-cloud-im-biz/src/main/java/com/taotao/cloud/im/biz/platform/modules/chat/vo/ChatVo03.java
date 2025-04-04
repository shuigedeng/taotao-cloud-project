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

package com.taotao.cloud.im.biz.platform.modules.chat.vo;

import com.platform.modules.chat.enums.MsgStatusEnum;
import lombok.Data;
import lombok.experimental.*;
import lombok.experimental.*;
import lombok.experimental.*;

@Data
@Accessors(chain = true) // 链式调用
public class ChatVo03 {

    /** 发送状态 */
    private MsgStatusEnum status;

    /** 好友详情 */
    private ChatVo04 userInfo;

    public String getStatusLabel() {
        if (status == null) {
            return null;
        }
        return status.getInfo();
    }
}
