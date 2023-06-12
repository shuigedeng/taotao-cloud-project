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

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.platform.common.enums.YesOrNoEnum;
import com.platform.common.web.domain.BaseEntity;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/** 实体类 q3z3 */
@Data
@TableName("chat_group_info")
@Accessors(chain = true) // 链式调用
@NoArgsConstructor
public class ChatGroupInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId
    private Long infoId;
    /** 用户id */
    private Long userId;
    /** 群组id */
    private Long groupId;
    /** 是否置顶 */
    private YesOrNoEnum top;
    /** 是否免打扰 */
    private YesOrNoEnum disturb;
    /** 是否保存群组 */
    private YesOrNoEnum keepGroup;
    /** 是否被踢 */
    private YesOrNoEnum kicked;
    /** 加入时间 */
    private Date createTime;

    public ChatGroupInfo(Long userId, Long groupId) {
        this.userId = userId;
        this.groupId = groupId;
        this.createTime = DateUtil.date();
        this.top = YesOrNoEnum.NO;
        this.disturb = YesOrNoEnum.NO;
        this.keepGroup = YesOrNoEnum.NO;
        this.kicked = YesOrNoEnum.NO;
    }
}
