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

package com.taotao.cloud.im.biz.platform.modules.collect.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.common.web.domain.BaseEntity;
import com.platform.modules.collect.enums.CollectTypeEnum;
import java.util.Date;
import lombok.Data;
import lombok.experimental.*;
import lombok.experimental.*;

/** 收藏表实体类 q3z3 */
@Data
@TableName("chat_collect")
@Accessors(chain = true) // 链式调用
public class ChatCollect extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId
    private Long id;
    /** 用户id */
    private Long userId;
    /** 收藏类型 */
    private CollectTypeEnum collectType;
    /** 内容 */
    private String content;
    /** 创建时间 */
    private Date createTime;
}
