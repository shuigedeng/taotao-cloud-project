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

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.niefy.common.utils.Json;
import jakarta.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.sql.Time;
import java.util.Date;
import lombok.Data;

/**
 * 自动回复规则
 *
 * @author Nifury
 * @since 2017-11-1
 */
@Data
@TableName("wx_msg_reply_rule")
public class MsgReplyRule implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long ruleId;

    private String appid;

    @NotEmpty(message = "规则名称不得为空")
    private String ruleName;

    @NotEmpty(message = "匹配关键词不得为空")
    private String matchValue;

    private boolean exactMatch;
    private String replyType;

    @NotEmpty(message = "回复内容不得为空")
    private String replyContent;

    @TableField(value = "`status`")
    private boolean status;

    @TableField(value = "`desc`")
    private String desc;

    private Time effectTimeStart;
    private Time effectTimeEnd;
    private int priority;
    private Date updateTime;

    @Override
    public String toString() {
        return Json.toJsonString(this);
    }
}
