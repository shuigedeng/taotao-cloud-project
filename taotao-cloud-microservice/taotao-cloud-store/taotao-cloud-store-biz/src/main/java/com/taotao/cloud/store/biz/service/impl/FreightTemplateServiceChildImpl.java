package com.taotao.cloud.store.biz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.store.biz.entity.FreightTemplateChild;
import com.taotao.cloud.store.biz.mapper.FreightTemplateChildMapper;
import com.taotao.cloud.store.biz.service.FreightTemplateChildService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 配送子模板业务层实现
 *
 * 
 * @since 2020-03-07 09:24:33
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class FreightTemplateServiceChildImpl extends ServiceImpl<FreightTemplateChildMapper, FreightTemplateChild> implements
	FreightTemplateChildService {

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
    public boolean removeFreightTemplate(String freightTemplateId) {
        LambdaQueryWrapper<FreightTemplateChild> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(FreightTemplateChild::getFreightTemplateId, freightTemplateId);
        return this.remove(lambdaQueryWrapper);
    }


}
