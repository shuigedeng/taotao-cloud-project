package com.taotao.cloud.goods.biz.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.taotao.cloud.common.model.PageModel;
import com.taotao.cloud.common.model.PageParam;
import com.taotao.cloud.goods.api.vo.SpecificationVO;
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
	Boolean deleteSpecification(List<String> ids);

	PageModel<SpecificationVO> getPage(String specName, PageParam page);
}
