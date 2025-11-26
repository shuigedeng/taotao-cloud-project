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

package com.taotao.cloud.wechat.biz.niefy.modules.wx.entity;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.niefy.common.utils.Json;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.experimental.*;
import me.chanjar.weixin.mp.bean.template.WxMpTemplate;

/**
 * 模板消息模板
 *
 * @author Nifury
 * @since 2017-9-27
 */
@Data
@TableName("wx_msg_template")
public class MsgTemplate implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private String appid;
    private String templateId;

    @TableField(value = "`name`")
    private String name;

    private String title;
    private String content;
    private JSONArray data;
    private String url;
    private JSONObject miniprogram;

    @TableField(value = "`status`")
    private boolean status;

    private Date updateTime;

    public MsgTemplate() {}

    public MsgTemplate(WxMpTemplate mpTemplate, String appid) {
        this.appid = appid;
        this.templateId = mpTemplate.getTemplateId();
        this.title = mpTemplate.getTitle();
        this.name = mpTemplate.getTemplateId();
        this.content = mpTemplate.getContent();
        this.status = true;
    }

    @Override
    public String toString() {
        return Json.toJsonString(this);
    }
}
