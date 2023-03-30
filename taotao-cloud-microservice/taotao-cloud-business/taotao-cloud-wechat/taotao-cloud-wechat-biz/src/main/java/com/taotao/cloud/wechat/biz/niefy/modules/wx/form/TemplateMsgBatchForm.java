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

package com.taotao.cloud.wechat.biz.niefy.modules.wx.form;

import com.github.niefy.common.utils.Json;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import lombok.Data;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;

/** 批量发送模板消息表单 通过用户筛选条件（一般使用标签筛选），将消息发送给数据库中所有符合筛选条件的用户 若所有筛选条件都为空，则表示发送给所有用户 */
@Data
public class TemplateMsgBatchForm {
    @NotNull(message = "需用户筛选条件参数")
    Map<String, Object> wxUserFilterParams;

    @NotEmpty(message = "模板ID不得为空")
    private String templateId;

    private String url;
    private WxMpTemplateMessage.MiniProgram miniprogram;

    @NotEmpty(message = "消息模板数据不得为空")
    private List<WxMpTemplateData> data;

    @Override
    public String toString() {
        return Json.toJsonString(this);
    }
}
