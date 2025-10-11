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
import com.taotao.cloud.message.api.model.vo.MessageVO;
import com.taotao.cloud.message.biz.model.entity.Message;

/** 管理端发送消息内容业务层 */
public interface MessageService extends IService<Message> {

    /**
     * 多条件分页获取
     *
     * @param messageVO
     * @param PageQuery
     * @return
     */
    IPage<Message> getPage(MessageVO messageVO, PageQuery PageQuery);

    /**
     * 发送站内信
     *
     * @param message 站内信
     * @return
     */
    Boolean sendMessage(Message message);

    /**
     * 删除站内信
     *
     * @param id 站内信id
     * @return
     */
    Boolean deleteMessage(String id);
}
