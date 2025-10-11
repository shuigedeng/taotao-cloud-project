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

package com.taotao.cloud.message.biz.service.business;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.boot.common.model.request.PageQuery;
import com.taotao.cloud.message.api.model.vo.MemberMessageQueryVO;
import com.taotao.cloud.message.biz.model.entity.MemberMessage;
import java.util.List;

/** 会员消息发送业务层 */
public interface MemberMessageService extends IService<MemberMessage> {

    /**
     * 会员消息查询接口
     *
     * @param memberMessageQueryVO 会员查询条件
     * @param PageQuery 分页条件
     * @return 会员消息分页
     */
    IPage<MemberMessage> getPage(MemberMessageQueryVO memberMessageQueryVO, PageQuery PageQuery);

    /**
     * 修改会员消息状态
     *
     * @param status 状态
     * @param messageId 消息id
     * @return 操作状态
     */
    Boolean editStatus(String status, String messageId);

    /**
     * 删除消息
     *
     * @param messageId 消息id
     * @return 操作状态
     */
    Boolean deleteMessage(String messageId);

    /**
     * 保存消息信息
     *
     * @param messages 消息
     * @return
     */
    Boolean save(List<MemberMessage> messages);
}
