package com.taotao.cloud.uc.biz.service;

import com.taotao.cloud.uc.api.vo.QueryRegionByParentIdVO;
import java.util.List;

/**
 * SysApplicationService
 *
 * @author shuigedeng
 * @version v1.0
 * @since 2021/03/12 16:28
 */
public interface SysRegionService {

	List<QueryRegionByParentIdVO> queryRegionByParentId(Long parentId);

	List<QueryRegionByParentIdVO> tree();

}
