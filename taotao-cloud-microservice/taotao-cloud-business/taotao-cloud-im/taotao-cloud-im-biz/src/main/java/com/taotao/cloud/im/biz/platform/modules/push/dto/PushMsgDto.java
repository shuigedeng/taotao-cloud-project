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

package com.taotao.cloud.im.biz.platform.modules.push.dto;

import com.platform.modules.push.enums.PushClickTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;

/** 推送消息对象 */
@Data
@Accessors(chain = true) // 链式调用
public class PushMsgDto {

    /** [必填]通知标题，长度50 */
    private String title;
    /** [必填]通知内容，长度256 */
    private String body;
    /** 通知的图标名称，需内置，长度64 */
    private String logo;
    /** 通知图标URL地址，长度256 */
    private String logo_url;
    /** [必填]点击类型，点击通知后续动作 */
    private PushClickTypeEnum clickType;
    /** 转换字段 */
    private String click_type;
    /**
     * [click_type为intent时必填]，点击类型，长度4096 示例：intent:#Intent;component=你的包名/你要打开的 activity
     * 全路径;S.parm1=value1;S.parm2=value2;end
     */
    private String intent;
    /** [click_type为url时必填]，点击通知打开链接，长度1024 */
    private String url;
    /** [click_type为payload/payload_custom时必填] 点击通知时，附加自定义透传消息，长度3072 */
    private String payload;
    /** [独立字段，必填]透传消息，长度3072 */
    private Dict transmission;
}
