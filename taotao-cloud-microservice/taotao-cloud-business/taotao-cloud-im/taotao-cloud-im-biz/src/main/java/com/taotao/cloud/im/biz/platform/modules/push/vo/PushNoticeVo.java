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

import lombok.Data;
import lombok.experimental.*;
import lombok.experimental.*;

/** 推送对象 */
@Data
@Accessors(chain = true) // 链式调用
public class PushNoticeVo {

    /** 帖子_小红点 */
    private Dict topicRed = Dict.create();
    /** 帖子_回复 */
    private Dict topicReply = Dict.create();
    /** 好友_申请 */
    private Dict friendApply = Dict.create();
}
