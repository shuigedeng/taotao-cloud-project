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
package com.taotao.cloud.uc.biz.service;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.taotao.cloud.uc.api.service.ISysRegionService;
import com.taotao.cloud.uc.api.vo.region.RegionParentVO;
import com.taotao.cloud.uc.biz.entity.SysRegion;
import com.taotao.cloud.uc.biz.mapper.SysRegionMapper;
import com.taotao.cloud.uc.biz.repository.ISysRegionRepository;
import com.taotao.cloud.uc.biz.repository.impl.SysRegionRepository;
import com.taotao.cloud.web.base.service.BaseSuperServiceImpl;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * SysRegionServiceImpl
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 20:37:52
 */
@Service
public class SysRegionServiceImpl extends
	BaseSuperServiceImpl<SysRegionMapper, SysRegion, SysRegionRepository, ISysRegionRepository, Long>
	implements ISysRegionService<SysRegion, Long> {

	@Override
	public List<RegionParentVO> queryRegionByParentId(Long parentId) {
		LambdaQueryWrapper<SysRegion> query = new LambdaQueryWrapper<>();
		query.eq(SysRegion::getParentId, parentId);
		List<SysRegion> sysRegions = getBaseMapper().selectList(query);
		List<RegionParentVO> result = new ArrayList<>();
		if (CollectionUtil.isNotEmpty(sysRegions)) {
			sysRegions.forEach(sysRegion -> {
				RegionParentVO vo = new RegionParentVO();
				vo.setId(sysRegion.getId());
				vo.setLabel(sysRegion.getName());
				vo.setValue(sysRegion.getCode());
				vo.setChildren(new ArrayList<>());
				result.add(vo);
			});
		}
		return result;
	}

	@Override
	public List<RegionParentVO> tree() {
		LambdaQueryWrapper<SysRegion> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(SysRegion::getParentId, 1);

		// 得到一级节点资源列表
		List<SysRegion> sysRegions = getBaseMapper().selectList(wrapper);
		List<RegionParentVO> vos = new ArrayList<>();
		if (CollectionUtil.isNotEmpty(sysRegions)) {
			sysRegions.forEach(sysRegion -> {
				RegionParentVO vo = new RegionParentVO();
				vo.setId(sysRegion.getId());
				vo.setLabel(sysRegion.getName());
				vo.setValue(sysRegion.getCode());
				vo.setChildren(new ArrayList<>());
				vos.add(vo);
			});
		}

		if (vos.size() > 0) {
			vos.forEach(this::findAllChild);
		}
		return vos;
	}

	public void findAllChild(RegionParentVO vo) {
		LambdaQueryWrapper<SysRegion> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(SysRegion::getParentId, vo.getId());
		List<SysRegion> sysRegions = getBaseMapper().selectList(wrapper);
		List<RegionParentVO> regions = new ArrayList<>();
		if (CollectionUtil.isNotEmpty(sysRegions)) {
			sysRegions.forEach(sysRegion -> {
				RegionParentVO region = new RegionParentVO();
				region.setId(sysRegion.getId());
				region.setLabel(sysRegion.getName());
				region.setValue(sysRegion.getCode());
				region.setChildren(new ArrayList<>());
				regions.add(region);
			});
		}

		vo.setChildren(regions);
		if (regions.size() > 0) {
			regions.forEach(this::findAllChild);
		}
	}
}
