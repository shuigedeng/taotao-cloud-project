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
package com.taotao.cloud.sys.biz.service;

import com.taotao.cloud.sys.api.vo.region.RegionParentVO;
import com.taotao.cloud.sys.api.vo.region.RegionTreeVO;
import com.taotao.cloud.sys.api.vo.region.RegionVO;
import com.taotao.cloud.sys.biz.entity.region.Region;
import com.taotao.cloud.web.base.service.BaseSuperService;
import java.util.List;
import java.util.Map;

/**
 * SysRegionService
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 20:37:26
 */
public interface IRegionService extends BaseSuperService<Region, Long> {

	/**
	 * queryRegionByParentId
	 *
	 * @param parentId parentId
	 * @return {@link List&lt;com.taotao.cloud.sys.api.vo.QueryRegionByParentIdVO&gt; }
	 * @since 2021-10-09 20:37:32
	 */
	List<RegionParentVO> queryRegionByParentId(Long parentId);

	/**
	 * tree
	 *
	 * @return {@link List&lt;com.taotao.cloud.sys.api.vo.QueryRegionByParentIdVO&gt; }
	 * @since 2021-10-09 20:37:36
	 */
	List<RegionParentVO> tree();

	List<RegionVO> getAllCity();

	void synchronizationData(String url);

	List<RegionTreeVO> treeOther();

	List<Region> getItem(Long parentId);

	Map<String, Object> getRegion(String cityCode, String townName);
}
