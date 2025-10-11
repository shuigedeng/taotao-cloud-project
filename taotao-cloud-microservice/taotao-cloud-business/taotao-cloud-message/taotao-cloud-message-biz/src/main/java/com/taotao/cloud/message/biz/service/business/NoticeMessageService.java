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
import com.taotao.cloud.message.api.model.dto.NoticeMessageDTO;
import com.taotao.cloud.message.biz.model.entity.NoticeMessage;

/** 通知类消息模板业务层 */
public interface NoticeMessageService extends IService<NoticeMessage> {

    /**
     * 多条件分页获取
     *
     * @param PageQuery 分页数据
     * @param type 类型
     * @return
     */
    IPage<NoticeMessage> getMessageTemplate(PageQuery PageQuery, String type);

    /**
     * 根据模板编码获取消息模板
     *
     * @param noticeMessageDTO 站内信消息
     */
    void noticeMessage(NoticeMessageDTO noticeMessageDTO);
}
