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

package com.taotao.cloud.im.biz.platform.modules.chat.service;

import com.github.pagehelper.PageInfo;
import com.platform.common.web.service.BaseService;
import com.platform.modules.chat.domain.ChatApply;
import com.platform.modules.chat.enums.ApplySourceEnum;
import com.platform.modules.chat.vo.ApplyVo03;

/** 好友申请表 服务层 q3z3 */
public interface ChatApplyService extends BaseService<ChatApply> {

    /** 申请好友 */
    void applyFriend(Long acceptId, ApplySourceEnum source, String reason);

    /** 申请加群 */
    void applyGroup(Long acceptId);

    /** 申请记录 */
    PageInfo list();

    /** 查询详情 */
    ApplyVo03 getInfo(Long applyId);
}
