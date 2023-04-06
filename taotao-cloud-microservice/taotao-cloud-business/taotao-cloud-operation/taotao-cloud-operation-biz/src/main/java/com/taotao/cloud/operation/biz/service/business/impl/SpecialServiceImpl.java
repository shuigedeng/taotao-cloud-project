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

package com.taotao.cloud.operation.biz.service.business.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.operation.biz.mapper.SpecialMapper;
import com.taotao.cloud.operation.biz.model.entity.PageData;
import com.taotao.cloud.operation.biz.model.entity.Special;
import com.taotao.cloud.operation.biz.service.business.PageDataService;
import com.taotao.cloud.operation.biz.service.business.SpecialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** 专题活动业务层实现 */
@Service
public class SpecialServiceImpl extends ServiceImpl<SpecialMapper, Special> implements SpecialService {

    /** 页面数据 */
    @Autowired
    private PageDataService pageDataService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Special addSpecial(Special special) {
        // 新建页面
        PageData pageData = new PageData();
        pageDataService.save(pageData);

        // 设置专题页面
        special.setPageDataId(pageData.getId());
        this.save(special);
        return special;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeSpecial(String id) {

        // 删除页面内容
        Special special = this.getById(id);
        pageDataService.removeById(special.getPageDataId());

        // 删除专题
        return this.removeById(id);
    }
}
