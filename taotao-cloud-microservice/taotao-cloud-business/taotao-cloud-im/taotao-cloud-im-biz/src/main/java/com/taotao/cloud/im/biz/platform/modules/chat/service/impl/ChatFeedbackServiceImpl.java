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

package com.taotao.cloud.im.biz.platform.modules.chat.service.impl;

import com.platform.common.constant.HeadConstant;
import com.platform.common.shiro.ShiroUtils;
import com.platform.common.utils.ServletUtils;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.chat.dao.ChatFeedbackDao;
import com.platform.modules.chat.domain.ChatFeedback;
import com.platform.modules.chat.service.ChatFeedbackService;
import com.platform.modules.chat.vo.MyVo04;
import jakarta.annotation.Resource;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** 建议反馈 服务层实现 q3z3 */
@Service("chatFeedbackService")
public class ChatFeedbackServiceImpl extends BaseServiceImpl<ChatFeedback> implements ChatFeedbackService {

    @Resource
    private ChatFeedbackDao chatFeedbackDao;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatFeedbackDao);
    }

    @Override
    public List<ChatFeedback> queryList(ChatFeedback t) {
        List<ChatFeedback> dataList = chatFeedbackDao.queryList(t);
        return dataList;
    }

    @Override
    public void addFeedback(MyVo04 myVo) {
        String version = ServletUtils.getRequest().getHeader(HeadConstant.VERSION);
        ChatFeedback feedback = BeanUtil.toBean(myVo, ChatFeedback.class)
                .setUserId(ShiroUtils.getUserId())
                .setVersion(version)
                .setCreateTime(DateUtil.date());
        this.add(feedback);
    }
}
