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

package com.taotao.cloud.im.biz.platform.modules.collect.service.impl;

import com.github.pagehelper.PageInfo;
import com.platform.common.exception.BaseException;
import com.platform.common.shiro.ShiroUtils;
import com.platform.common.web.service.impl.BaseServiceImpl;
import com.platform.modules.collect.dao.ChatCollectDao;
import com.platform.modules.collect.domain.ChatCollect;
import com.platform.modules.collect.service.ChatCollectService;
import com.platform.modules.collect.vo.CollectVo01;
import com.platform.modules.collect.vo.CollectVo02;
import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** 收藏表 服务层实现 q3z3 */
@Service("chatCollectService")
public class ChatCollectServiceImpl extends BaseServiceImpl<ChatCollect> implements ChatCollectService {

    @Resource
    private ChatCollectDao chatCollectDao;

    @Autowired
    public void setBaseDao() {
        super.setBaseDao(chatCollectDao);
    }

    @Override
    public List<ChatCollect> queryList(ChatCollect t) {
        List<ChatCollect> dataList = chatCollectDao.queryList(t);
        return dataList;
    }

    @Override
    public void addCollect(CollectVo01 collectVo) {
        ChatCollect collect = new ChatCollect()
                .setUserId(ShiroUtils.getUserId())
                .setCollectType(collectVo.getCollectType())
                .setContent(collectVo.getContent())
                .setCreateTime(DateUtil.date());
        this.add(collect);
    }

    @Override
    public void deleteCollect(Long collectId) {
        ChatCollect collect = this.getById(collectId);
        if (collect == null) {
            return;
        }
        if (!ShiroUtils.getUserId().equals(collect.getUserId())) {
            throw new BaseException("删除失败，不能删除别人的收藏");
        }
        this.deleteById(collectId);
    }

    @Override
    public PageInfo collectList(ChatCollect collect) {
        collect.setUserId(ShiroUtils.getUserId());
        List<ChatCollect> collectList = queryList(collect);
        List<CollectVo02> dataList = new ArrayList<>();
        collectList.forEach(e -> {
            dataList.add(BeanUtil.toBean(e, CollectVo02.class).setCollectId(e.getId()));
        });
        return getPageInfo(dataList, collectList);
    }
}
