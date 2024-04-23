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

package com.taotao.cloud.goods.application.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.utils.lang.StringUtils;
import com.taotao.cloud.goods.application.command.specification.dto.SpecificationPageQuery;
import com.taotao.cloud.goods.application.service.ICategoryService;
import com.taotao.cloud.goods.application.service.ICategorySpecificationService;
import com.taotao.cloud.goods.application.service.ISpecificationService;
import com.taotao.cloud.goods.infrastructure.persistent.mapper.ISpecificationMapper;
import com.taotao.cloud.goods.infrastructure.persistent.po.CategorySpecification;
import com.taotao.cloud.goods.infrastructure.persistent.po.Specification;
import com.taotao.cloud.goods.infrastructure.persistent.repository.cls.SpecificationRepository;
import com.taotao.cloud.goods.infrastructure.persistent.repository.inf.ISpecificationRepository;
import com.taotao.cloud.web.base.service.impl.BaseSuperServiceImpl;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 商品规格业务层实现
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:02:55
 */
@AllArgsConstructor
@Service
public class SpecificationServiceImpl
        extends BaseSuperServiceImpl<
	Specification, Long, ISpecificationMapper, SpecificationRepository, ISpecificationRepository>
        implements ISpecificationService {

    /** 分类-规格绑定服务 */
    private final ICategorySpecificationService categorySpecificationService;
    /** 分类服务 */
    private final ICategoryService categoryService;

    @Override
    public boolean deleteSpecification(List<Long> ids) {
        boolean result = false;
        for (Long id : ids) {
            // 如果此规格绑定分类则不允许删除
            List<CategorySpecification> list = categorySpecificationService.list(
                    new QueryWrapper<CategorySpecification>().eq("specification_id", id));

            if (!list.isEmpty()) {
                List<Long> categoryIds = new ArrayList<>();
                list.forEach(item -> categoryIds.add(item.getCategoryId()));
                throw new BusinessException(
                        ResultEnum.SPEC_DELETE_ERROR.getCode(),
                        JSONUtil.toJsonStr(categoryService.getCategoryNameByIds(categoryIds)));
            }
            // 删除规格
            result = this.removeById(id);
        }
        return result;
    }

    @Override
    public IPage<Specification> getPage(SpecificationPageQuery specificationPageQuery) {
        LambdaQueryWrapper<Specification> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(
                StringUtils.isNotEmpty(specificationPageQuery.getSpecName()),
                Specification::getSpecName,
                specificationPageQuery.getSpecName());
        return this.page(specificationPageQuery.buildMpPage(), lambdaQueryWrapper);
    }

    @Override
    @Transactional
    public boolean saveCategoryBrand(Long categoryId, String[] categorySpecs) {
        QueryWrapper<CategorySpecification> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_id", categoryId);
        // 删除分类规格绑定信息
        this.categorySpecificationService.remove(queryWrapper);
        // 绑定规格信息
        if (categorySpecs != null && categorySpecs.length > 0) {
            List<CategorySpecification> categorySpecifications = new ArrayList<>();
            for (String categorySpec : categorySpecs) {
                categorySpecifications.add(new CategorySpecification(categoryId, Long.valueOf(categorySpec)));
            }
            categorySpecificationService.saveBatch(categorySpecifications);
        }
        return true;
    }
}
