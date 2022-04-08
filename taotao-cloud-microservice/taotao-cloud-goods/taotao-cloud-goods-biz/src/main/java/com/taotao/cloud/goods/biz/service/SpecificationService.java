package com.taotao.cloud.goods.biz.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.common.model.PageParam;
import com.taotao.cloud.goods.biz.entity.Specification;
import java.util.List;

/**
 * 规格业务层
 */
public interface SpecificationService extends IService<Specification> {

	/**
	 * 删除规格
	 *
	 * @param ids 规格ID
	 * @return 是否删除成功
	 */
	Boolean deleteSpecification(List<Long> ids);

	/**
	 * 分页查询
	 *
	 * @param specName 名称
	 * @param page     分页参数
	 * @return 数据
	 * @since 2022-04-06 16:10:19
	 */
	IPage<Specification> getPage(String specName, PageParam page);
}
