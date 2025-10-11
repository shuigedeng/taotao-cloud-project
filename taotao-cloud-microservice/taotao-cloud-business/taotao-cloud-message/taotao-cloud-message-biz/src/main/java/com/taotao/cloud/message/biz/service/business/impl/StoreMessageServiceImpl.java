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

package com.taotao.cloud.message.biz.service.business.impl;

import org.dromara.hutoolcore.text.CharSequenceUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.boot.common.model.request.PageQuery;
import com.taotao.cloud.message.api.model.vo.StoreMessageQueryVO;
import com.taotao.cloud.message.biz.mapper.StoreMessageMapper;
import com.taotao.cloud.message.biz.model.entity.StoreMessage;
import com.taotao.cloud.message.biz.service.business.StoreMessageService;
import java.util.List;
import org.springframework.stereotype.Service;

/** 消息发送业务层实现 */
@Service
public class StoreMessageServiceImpl extends ServiceImpl<StoreMessageMapper, StoreMessage>
        implements StoreMessageService {

    @Override
    public Boolean deleteByMessageId(String messageId) {
        StoreMessage storeMessage = this.getById(messageId);
        if (storeMessage != null) {
            return this.removeById(messageId);
        }
        return false;
    }

    @Override
    public IPage<StoreMessage> getPage(StoreMessageQueryVO storeMessageQueryVO, PageQuery PageQuery) {

        QueryWrapper<StoreMessage> queryWrapper = new QueryWrapper<>();
        // 消息id查询
        if (CharSequenceUtil.isNotEmpty(storeMessageQueryVO.getMessageId())) {
            queryWrapper.eq("message_id", storeMessageQueryVO.getMessageId());
        }
        // 商家id
        if (CharSequenceUtil.isNotEmpty(storeMessageQueryVO.getStoreId())) {
            queryWrapper.eq("store_id", storeMessageQueryVO.getStoreId());
        }
        // 状态查询
        if (storeMessageQueryVO.getStatus() != null) {
            queryWrapper.eq("status", storeMessageQueryVO.getStatus());
        }
        queryWrapper.orderByDesc("status");
        // return this.baseMapper.queryByParams(PageUtil.initPage(PageQuery), queryWrapper);
        return null;
    }

    @Override
    public Boolean save(List<StoreMessage> messages) {
        return saveBatch(messages);
    }

    @Override
    public Boolean editStatus(String status, String id) {
        StoreMessage storeMessage = this.getById(id);
        if (storeMessage != null) {
            // 校验权限
            // if (!storeMessage.getStoreId().equals(UserContext.getCurrentUser().getStoreId())) {
            //     throw new ResourceNotFoundException(ResultEnum.USER_AUTHORITY_ERROR.message());
            // }
            storeMessage.setStatus(status);
            return this.updateById(storeMessage);
        }
        return false;
    }
}
