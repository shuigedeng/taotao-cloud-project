package com.taotao.cloud.store.biz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.store.biz.model.entity.FreightTemplateChild;
import com.taotao.cloud.store.biz.mapper.FreightTemplateChildMapper;
import com.taotao.cloud.store.biz.service.IFreightTemplateChildService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 配送子模板业务层实现
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-06-01 15:05:08
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class FreightTemplateServiceChildImpl extends ServiceImpl<FreightTemplateChildMapper, FreightTemplateChild> implements
        IFreightTemplateChildService {

    @Override
    public List<FreightTemplateChild> getFreightTemplateChild(Long freightTemplateId) {
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
