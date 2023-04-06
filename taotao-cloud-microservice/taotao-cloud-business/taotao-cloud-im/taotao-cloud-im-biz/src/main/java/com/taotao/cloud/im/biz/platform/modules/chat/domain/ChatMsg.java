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

package com.taotao.cloud.im.biz.platform.modules.chat.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.common.web.domain.BaseEntity;
import com.platform.modules.push.enums.PushMsgTypeEnum;
import com.platform.modules.push.enums.PushTalkEnum;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/** 聊天消息实体类 q3z3 */
@Data
@TableName("chat_msg")
@Accessors(chain = true) // 链式调用
public class ChatMsg extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 消息主键 */
    @TableId
    private Long id;
    /** 发送人 */
    private Long fromId;
    /** 接收人 */
    private Long toId;
    /** 消息类型 */
    private PushMsgTypeEnum msgType;
    /** 消息类型 */
    private PushTalkEnum talkType;
    /** 消息内容 */
    private String content;
    /** 创建时间 */
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Date createTime;
}
