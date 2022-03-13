package com.taotao.cloud.goods.biz.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.goods.biz.entity.CategorySpecification;
import com.taotao.cloud.goods.biz.entity.Specification;
import com.taotao.cloud.goods.biz.mapper.SpecificationMapper;
import com.taotao.cloud.goods.biz.service.CategorySpecificationService;
import com.taotao.cloud.goods.biz.service.SpecificationService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 商品规格业务层实现
 */
@Service
public class SpecificationServiceImpl extends
	ServiceImpl<SpecificationMapper, Specification> implements
	SpecificationService {

	/**
	 * 分类-规格绑定
	 */
	@Autowired
	private CategorySpecificationService categorySpecificationService;
	/**
	 * 分类
	 */
	@Autowired
	private CategoryServiceImpl categoryService;


	@Override
	public boolean deleteSpecification(List<String> ids) {
		boolean result = false;
		for (String id : ids) {
			//如果此规格绑定分类则不允许删除
			List<CategorySpecification> list = categorySpecificationService.list(
				new QueryWrapper<CategorySpecification>().eq("specification_id", id));
			if (!list.isEmpty()) {
				List<String> categoryIds = new ArrayList<>();
				list.forEach(item -> categoryIds.add(item.getCategoryId()));
				throw new ServiceException(ResultCode.SPEC_DELETE_ERROR,
					JSONUtil.toJsonStr(categoryService.getCategoryNameByIds(categoryIds)));
			}
			//删除规格
			result = this.removeById(id);
		}
		return result;
	}

}
