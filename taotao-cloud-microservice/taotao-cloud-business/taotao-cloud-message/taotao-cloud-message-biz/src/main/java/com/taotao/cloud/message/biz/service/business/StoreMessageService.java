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
import com.taotao.cloud.message.api.model.vo.StoreMessageQueryVO;
import com.taotao.cloud.message.biz.model.entity.StoreMessage;
import java.util.List;

/** 店铺接收消息业务层 */
public interface StoreMessageService extends IService<StoreMessage> {

    /**
     * 通过消息id删除
     *
     * @param messageId 消息ID
     * @return 操作结果
     */
    Boolean deleteByMessageId(String messageId);

    /**
     * 多条件分页获取
     *
     * @param storeMessageQueryVO 店铺消息查询VO
     * @param PageQuery 分页
     * @return 店铺消息分页
     */
    IPage<StoreMessage> getPage(StoreMessageQueryVO storeMessageQueryVO, PageQuery PageQuery);

    /**
     * 保存店铺消息信息
     *
     * @param messages 消息
     * @return
     */
    Boolean save(List<StoreMessage> messages);

    /**
     * 修改店铺消息状态
     *
     * @param status 状态
     * @param id id
     * @return
     */
    Boolean editStatus(String status, String id);
}
