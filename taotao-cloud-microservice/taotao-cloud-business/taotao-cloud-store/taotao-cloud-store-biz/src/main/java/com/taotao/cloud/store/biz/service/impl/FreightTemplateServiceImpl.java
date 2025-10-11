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
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.boot.cache.redis.repository.RedisRepository;

import com.taotao.boot.common.enums.ResultEnum;
import com.taotao.boot.common.exception.BusinessException;
import com.taotao.boot.common.model.request.PageQuery;
import com.taotao.boot.security.spring.model.SecurityUser;
import com.taotao.boot.common.utils.bean.BeanUtils;
import com.taotao.boot.security.spring.utils.SecurityUtils;
import com.taotao.cloud.store.api.model.vo.FreightTemplateChildVO;
import com.taotao.cloud.store.api.model.vo.FreightTemplateInfoVO;
import com.taotao.cloud.store.biz.mapper.FreightTemplateMapper;
import com.taotao.cloud.store.biz.mapstruct.IFreightTemplateChildMapStruct;
import com.taotao.cloud.store.biz.model.entity.FreightTemplate;
import com.taotao.cloud.store.biz.model.entity.FreightTemplateChild;
import com.taotao.cloud.store.biz.service.IFreightTemplateChildService;
import com.taotao.cloud.store.biz.service.IFreightTemplateService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 店铺运费模板业务层实现
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-06-01 15:05:19
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class FreightTemplateServiceImpl extends ServiceImpl<FreightTemplateMapper, FreightTemplate>
        implements IFreightTemplateService {
    /** 配送子模板 */
    @Autowired
    private IFreightTemplateChildService freightTemplateChildService;
    /** 缓存 */
    @Autowired
    private RedisRepository redisRepository;

    @Override
    public List<FreightTemplateInfoVO> getFreightTemplateList(String storeId) {
        // 先从缓存中获取运费模板，如果有则直接返回，如果没有则查询数据后再返回
        List<FreightTemplateInfoVO> list =
                (List<FreightTemplateInfoVO>) redisRepository.get(CachePrefix.SHIP_TEMPLATE.getPrefix() + storeId);
        if (list != null) {
            return list;
        }
        list = new ArrayList<>();
        // 查询运费模板
        LambdaQueryWrapper<FreightTemplate> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(FreightTemplate::getStoreId, storeId);
        List<FreightTemplate> freightTemplates = this.baseMapper.selectList(lambdaQueryWrapper);
        if (!freightTemplates.isEmpty()) {
            // 如果模板不为空则查询子模板信息
            for (FreightTemplate freightTemplate : freightTemplates) {
                FreightTemplateInfoVO freightTemplateInfoVO = new FreightTemplateInfoVO();
                BeanUtils.copyProperties(freightTemplate, freightTemplateInfoVO);
                List<FreightTemplateChild> freightTemplateChildren =
                        freightTemplateChildService.getFreightTemplateChild(freightTemplate.getId());
                if (!freightTemplateChildren.isEmpty()) {

                    freightTemplateInfoVO.setFreightTemplateChildList(
                            IFreightTemplateChildMapStruct.INSTANCE
                                    .convertToList(freightTemplateChildren));
                }
                list.add(freightTemplateInfoVO);
            }
        }
        redisRepository.set(CachePrefix.SHIP_TEMPLATE.getPrefix() + storeId, list);
        return list;
    }

    @Override
    public IPage<FreightTemplate> getFreightTemplate(PageQuery PageQuery) {
        LambdaQueryWrapper<FreightTemplate> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(
                FreightTemplate::getStoreId, SecurityUtils.getCurrentUser().getStoreId());
        return this.baseMapper.selectPage(PageQuery.buildMpPage(), lambdaQueryWrapper);
    }

    @Override
    public FreightTemplateInfoVO getFreightTemplate(String id) {
        FreightTemplateInfoVO freightTemplateInfoVO = new FreightTemplateInfoVO();
        // 获取运费模板
        FreightTemplate freightTemplate = this.getById(id);
        if (freightTemplate != null) {
            // 复制属性
            org.springframework.beans.BeanUtils.copyProperties(freightTemplate, freightTemplateInfoVO);
            // 填写运费模板子内容
            List<FreightTemplateChild> freightTemplateChildList =
                    freightTemplateChildService.getFreightTemplateChild(id);
            freightTemplateInfoVO.setFreightTemplateChildList(
                    IFreightTemplateChildMapStruct.INSTANCE.convertToList(
                            freightTemplateChildList));
        }
        return freightTemplateInfoVO;
    }

    @Override
    public FreightTemplateInfoVO addFreightTemplate(FreightTemplateInfoVO freightTemplateInfoVO) {
        // 获取当前登录商家账号
        SecurityUser tokenUser = SecurityUtils.getCurrentUser();
        FreightTemplate freightTemplate = new FreightTemplate();
        // 设置店铺ID
        freightTemplateInfoVO.setStoreId(tokenUser.getStoreId());
        // 复制属性
        org.springframework.beans.BeanUtils.copyProperties(freightTemplateInfoVO, freightTemplate);
        // 添加运费模板
        this.save(freightTemplate);
        // 给子模板赋父模板的id
        List<FreightTemplateChildVO> list = new ArrayList<>();
        // 如果子运费模板不为空则进行新增
        if (freightTemplateInfoVO.getFreightTemplateChildList() != null) {
            for (FreightTemplateChildVO freightTemplateChild : freightTemplateInfoVO.getFreightTemplateChildList()) {
                freightTemplateChild.setFreightTemplateId(freightTemplate.getId());
                list.add(freightTemplateChild);
            }
            List<FreightTemplateChild> freightTemplateChildren =
                    IFreightTemplateChildMapStruct.INSTANCE.convertToVoList(list);
            // 添加运费模板子内容
            freightTemplateChildService.addFreightTemplateChild(freightTemplateChildren);
        }

        // 更新缓存
        redisRepository.del(CachePrefix.SHIP_TEMPLATE.getPrefix() + tokenUser.getStoreId());
        return freightTemplateInfoVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FreightTemplateInfoVO editFreightTemplate(FreightTemplateInfoVO freightTemplateInfoVO) {
        // 获取当前登录商家账号
        SecurityUser tokenUser = SecurityUtils.getCurrentUser();
        if (freightTemplateInfoVO.getId().equals(tokenUser.getStoreId())) {
            throw new BusinessException(ResultEnum.USER_AUTHORITY_ERROR);
        }

        FreightTemplate freightTemplate = new FreightTemplate();
        // 复制属性
        org.springframework.beans.BeanUtils.copyProperties(freightTemplateInfoVO, freightTemplate);
        // 修改运费模板
        this.updateById(freightTemplate);
        // 删除模板子内容
        freightTemplateChildService.removeFreightTemplate(freightTemplateInfoVO.getId());
        // 给子模板赋父模板的id
        List<FreightTemplateChildVO> list = new ArrayList<>();
        for (FreightTemplateChildVO freightTemplateChild : freightTemplateInfoVO.getFreightTemplateChildList()) {
            freightTemplateChild.setFreightTemplateId(freightTemplate.getId());
            list.add(freightTemplateChild);
        }
        List<FreightTemplateChild> freightTemplateChildren =
                IFreightTemplateChildMapStruct.INSTANCE.convertToVoList(list);
        // 添加模板子内容
        freightTemplateChildService.addFreightTemplateChild(freightTemplateChildren);
        // 更新缓存
        redisRepository.del(CachePrefix.SHIP_TEMPLATE.getPrefix() + tokenUser.getStoreId());
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeFreightTemplate(Long id) {
        // 获取当前登录商家账号
        SecurityUser tokenUser = SecurityUtils.getCurrentUser();
        LambdaQueryWrapper<FreightTemplate> lambdaQueryWrapper = Wrappers.lambdaQuery();
        lambdaQueryWrapper.eq(FreightTemplate::getStoreId, tokenUser.getStoreId());
        lambdaQueryWrapper.eq(FreightTemplate::getId, id);
        // 如果删除成功则删除运费模板子项
        if (this.remove(lambdaQueryWrapper)) {
            redisRepository.del(CachePrefix.SHIP_TEMPLATE.getPrefix() + tokenUser.getStoreId());
            return freightTemplateChildService.removeFreightTemplate(id);
        }
        return false;
    }
}
