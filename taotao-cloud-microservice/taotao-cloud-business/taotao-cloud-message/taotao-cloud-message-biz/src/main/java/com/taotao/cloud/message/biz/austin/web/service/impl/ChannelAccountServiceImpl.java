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

package com.taotao.cloud.message.biz.austin.web.service.impl;

import org.dromara.hutoolcore.date.DateUtil;
import org.dromara.hutoolcore.util.StrUtil;
import com.taotao.cloud.message.biz.austin.common.constant.AustinConstant;
import com.taotao.cloud.message.biz.austin.common.constant.CommonConstant;
import com.taotao.cloud.message.biz.austin.support.dao.ChannelAccountDao;
import com.taotao.cloud.message.biz.austin.support.domain.ChannelAccount;
import com.taotao.cloud.message.biz.austin.web.service.ChannelAccountService;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 3y
 */
@Service
public class ChannelAccountServiceImpl implements ChannelAccountService {

    @Autowired
    private ChannelAccountDao channelAccountDao;

    @Override
    public ChannelAccount save(ChannelAccount channelAccount) {
        if (Objects.isNull(channelAccount.getId())) {
            channelAccount.setCreated(Math.toIntExact(DateUtil.currentSeconds()));
            channelAccount.setIsDeleted(CommonConstant.FALSE);
        }
        channelAccount.setCreator(
                StrUtil.isBlank(channelAccount.getCreator())
                        ? AustinConstant.DEFAULT_CREATOR
                        : channelAccount.getCreator());
        channelAccount.setUpdated(Math.toIntExact(DateUtil.currentSeconds()));
        return channelAccountDao.save(channelAccount);
    }

    @Override
    public List<ChannelAccount> queryByChannelType(Integer channelType, String creator) {
        return channelAccountDao.findAllByIsDeletedEqualsAndCreatorEqualsAndSendChannelEquals(
                CommonConstant.FALSE, creator, channelType);
    }

    @Override
    public List<ChannelAccount> list(String creator) {
        return channelAccountDao.findAllByCreatorEquals(creator);
    }

    @Override
    public void deleteByIds(List<Long> ids) {
        channelAccountDao.deleteAllById(ids);
    }
}
