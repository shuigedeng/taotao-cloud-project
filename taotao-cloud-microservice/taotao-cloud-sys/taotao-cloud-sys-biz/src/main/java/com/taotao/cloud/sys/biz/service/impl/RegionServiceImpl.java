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
package com.taotao.cloud.sys.biz.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.taotao.cloud.order.api.dubbo.IDubboOrderService;
import com.taotao.cloud.sys.api.dubbo.IDubboRegionService;
import com.taotao.cloud.sys.api.vo.region.RegionParentVO;
import com.taotao.cloud.sys.biz.entity.Region;
import com.taotao.cloud.sys.biz.mapper.IRegionMapper;
import com.taotao.cloud.sys.biz.repository.cls.RegionRepository;
import com.taotao.cloud.sys.biz.repository.inf.IRegionRepository;
import com.taotao.cloud.sys.biz.service.IRegionService;
import com.taotao.cloud.web.base.service.BaseSuperServiceImpl;
import java.util.ArrayList;
import java.util.List;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

/**
 * RegionServiceImpl
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 20:37:52
 */
@Service
@DubboService(interfaceClass = IDubboRegionService.class)
public class RegionServiceImpl extends
	BaseSuperServiceImpl<IRegionMapper, Region, RegionRepository, IRegionRepository, Long>
	implements IDubboRegionService, IRegionService {

	@DubboReference
	private IDubboOrderService dubboOrderService;

	@Override
	public List<RegionParentVO> queryRegionByParentId(Long parentId) {
		LambdaQueryWrapper<Region> query = new LambdaQueryWrapper<>();
		query.eq(Region::getParentId, parentId);
		List<Region> sysRegions = getBaseMapper().selectList(query);
		List<RegionParentVO> result = new ArrayList<>();
		if (CollectionUtil.isNotEmpty(sysRegions)) {
			sysRegions.forEach(sysRegion -> {
				RegionParentVO vo = new RegionParentVO(sysRegion.getId(),
					sysRegion.getName(),
					sysRegion.getCode(),
					new ArrayList<>());
				result.add(vo);
			});
		}
		return result;
	}

	@Override
	public List<RegionParentVO> tree() {
		LambdaQueryWrapper<Region> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(Region::getParentId, 1);

		// 得到一级节点菜单列表
		List<Region> sysRegions = getBaseMapper().selectList(wrapper);
		List<RegionParentVO> vos = new ArrayList<>();
		if (CollectionUtil.isNotEmpty(sysRegions)) {
			sysRegions.forEach(sysRegion -> {
				RegionParentVO vo = new RegionParentVO(sysRegion.getId(),
					sysRegion.getName(),
					sysRegion.getCode(),
					new ArrayList<>());
				vos.add(vo);
			});
		}

		if (vos.size() > 0) {
			vos.forEach(this::findAllChild);
		}
		return vos;
	}

	public void findAllChild(RegionParentVO vo) {
		LambdaQueryWrapper<Region> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(Region::getParentId, vo.id());
		List<Region> sysRegions = getBaseMapper().selectList(wrapper);
		List<RegionParentVO> regions = new ArrayList<>();
		if (CollectionUtil.isNotEmpty(sysRegions)) {
			sysRegions.forEach(sysRegion -> {
				RegionParentVO region = new RegionParentVO(sysRegion.getId(),
					sysRegion.getName(),
					sysRegion.getCode(),
					new ArrayList<>());
				regions.add(region);
			});
		}

		//vo.children(regions);
		if (regions.size() > 0) {
			regions.forEach(this::findAllChild);
		}
	}
}
