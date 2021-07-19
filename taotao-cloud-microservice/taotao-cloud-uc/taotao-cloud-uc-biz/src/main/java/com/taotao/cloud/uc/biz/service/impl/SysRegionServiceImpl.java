/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.uc.biz.service.impl;

import com.taotao.cloud.uc.api.vo.QueryRegionByParentIdVO;
import com.taotao.cloud.uc.biz.service.SysRegionService;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * SysRegionServiceImpl
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/03/16 09:34
 */
@Service
public class SysRegionServiceImpl implements SysRegionService {

//	@Autowired
//	private SysRegionMapper sysRegionMapper;

	@Override
	public List<QueryRegionByParentIdVO> queryRegionByParentId(Long parentId) {
//		LambdaQueryWrapper<SysRegion> query = new LambdaQueryWrapper<>();
//		query.eq(SysRegion::getParentId, parentId);
//		List<SysRegion> sysRegions = sysRegionMapper.selectList(query);
//		List<QueryRegionByParentIdVO> result = new ArrayList<>();
//		if (CollectionUtil.isNotEmpty(sysRegions)) {
//			sysRegions.forEach(sysRegion -> {
//				QueryRegionByParentIdVO vo = new QueryRegionByParentIdVO();
//				vo.setId(sysRegion.getId());
//				vo.setLabel(sysRegion.getName());
//				vo.setValue(sysRegion.getCode());
//				vo.setChildren(new ArrayList<>());
//				result.add(vo);
//			});
//		}
//		return result;
		return null;
	}

	@Override
	public List<QueryRegionByParentIdVO> tree() {
//		LambdaQueryWrapper<SysRegion> wrapper = new LambdaQueryWrapper<>();
//		wrapper.eq(SysRegion::getParentId, 1);
//
//		// 得到一级节点资源列表
//		List<SysRegion> sysRegions = sysRegionMapper.selectList(wrapper);
//		List<QueryRegionByParentIdVO> vos = new ArrayList<>();
//		if (CollectionUtil.isNotEmpty(sysRegions)) {
//			sysRegions.forEach(sysRegion -> {
//				QueryRegionByParentIdVO vo = new QueryRegionByParentIdVO();
//				vo.setId(sysRegion.getId());
//				vo.setLabel(sysRegion.getName());
//				vo.setValue(sysRegion.getCode());
//				vo.setChildren(new ArrayList<>());
//				vos.add(vo);
//			});
//		}
//
//		if (vos.size() > 0) {
//			vos.forEach(this::findAllChild);
//		}
//		return vos;
		return null;
	}

	public void findAllChild(QueryRegionByParentIdVO vo) {
//		LambdaQueryWrapper<SysRegion> wrapper = new LambdaQueryWrapper<>();
//		wrapper.eq(SysRegion::getParentId, vo.getId());
//		List<SysRegion> sysRegions = sysRegionMapper.selectList(wrapper);
//		List<QueryRegionByParentIdVO> regions = new ArrayList<>();
//		if (CollectionUtil.isNotEmpty(sysRegions)) {
//			sysRegions.forEach(sysRegion -> {
//				QueryRegionByParentIdVO region = new QueryRegionByParentIdVO();
//				region.setId(sysRegion.getId());
//				region.setLabel(sysRegion.getName());
//				region.setValue(sysRegion.getCode());
//				region.setChildren(new ArrayList<>());
//				regions.add(region);
//			});
//		}
//
//		vo.setChildren(regions);
//		if (regions.size() > 0) {
//			regions.forEach(this::findAllChild);
//		}
	}
}
