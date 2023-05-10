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

package com.taotao.cloud.store.biz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.store.biz.mapper.FreightTemplateChildMapper;
import com.taotao.cloud.store.biz.model.entity.FreightTemplateChild;
import com.taotao.cloud.store.biz.service.IFreightTemplateChildService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 配送子模板业务层实现
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-06-01 15:05:08
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class FreightTemplateServiceChildImpl extends ServiceImpl<FreightTemplateChildMapper, FreightTemplateChild>
        implements IFreightTemplateChildService {

    @Override
    public List<FreightTemplateChild> getFreightTemplateChild(String freightTemplateId) {
        LambdaQueryWrapper<FreightTemplateChild> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(FreightTemplateChild::getFreightTemplateId, freightTemplateId);
        return this.baseMapper.selectList(lambdaQueryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addFreightTemplateChild(List<FreightTemplateChild> freightTemplateChildren) {
        return this.saveBatch(freightTemplateChildren);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeFreightTemplate(Long freightTemplateId) {
        LambdaQueryWrapper<FreightTemplateChild> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(FreightTemplateChild::getFreightTemplateId, freightTemplateId);
        return this.remove(lambdaQueryWrapper);
    }
}
