package com.taotao.cloud.goods.biz.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.goods.api.query.SpecificationPageQuery;
import com.taotao.cloud.goods.biz.entity.Specification;

import java.util.List;

/**
 * 规格业务层
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:01:01
 */
public interface ISpecificationService extends IService<Specification> {

	/**
	 * 删除规格
	 *
	 * @param ids 规格ID
	 * @return {@link Boolean }
	 * @since 2022-04-27 17:01:01
	 */
	Boolean deleteSpecification(List<Long> ids);

	/**
	 * 分页查询
	 *
	 * @param specificationPageQuery 查询条件
	 * @return {@link IPage }<{@link Specification }>
	 * @since 2022-04-27 17:01:01
	 */
	IPage<Specification> getPage(SpecificationPageQuery specificationPageQuery);
}
