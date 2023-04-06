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

package com.taotao.cloud.im.biz.platform.modules.topic.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

/** 帖子点赞实体类 q3z3 */
@Data
@TableName("chat_topic_like")
@Accessors(chain = true) // 链式调用
public class ChatTopicLike extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId
    private Long id;
    /** 帖子id */
    private Long topicId;
    /** 用户id */
    private Long userId;
    /** 是否点赞 */
    private YesOrNoEnum hasLike;
}
