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

package com.taotao.cloud.im.biz.platform.modules.topic.vo;

import com.platform.common.enums.YesOrNoEnum;
import com.platform.modules.topic.enums.TopicReplyTypeEnum;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true) // 链式调用
public class TopicVo06 {

    /** 回复id */
    private Long replyId;
    /** 回复类型1帖子2用户 */
    private TopicReplyTypeEnum replyType;
    /** 评论内容 */
    private String content;
    /** 回复时间 */
    private Date createTime;
    /** 用户id */
    private Long userId;
    /** 昵称 */
    private String nickName;
    /** 头像 */
    private String portrait;
    /** 是否可以删除 */
    private YesOrNoEnum canDeleted;
    /** 用户id */
    private Long toUserId;
    /** 昵称 */
    private String toNickName;
    /** 头像 */
    private String toPortrait;
}
