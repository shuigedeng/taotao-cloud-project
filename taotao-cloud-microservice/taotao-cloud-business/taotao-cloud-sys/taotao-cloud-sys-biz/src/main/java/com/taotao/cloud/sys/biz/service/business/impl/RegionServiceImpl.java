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

package com.taotao.cloud.sys.biz.service.business.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taotao.boot.cache.redis.repository.RedisRepository;
import com.taotao.boot.common.constant.RedisConstant;
import com.taotao.boot.common.http.HttpRequest;
import com.taotao.boot.common.utils.id.IdGeneratorUtils;
import com.taotao.boot.common.utils.common.OrikaUtils;
import com.taotao.boot.common.utils.log.LogUtils;
import com.taotao.boot.common.utils.secure.SignUtils;
import com.taotao.boot.core.autoconfigure.OkhttpAutoConfiguration.OkHttpService;
import com.taotao.cloud.sys.biz.model.vo.region.RegionParentVO;
import com.taotao.cloud.sys.biz.model.vo.region.RegionTreeVO;
import com.taotao.cloud.sys.biz.model.vo.region.RegionVO;
import com.taotao.cloud.sys.biz.mapper.IRegionMapper;
import com.taotao.cloud.sys.biz.model.convert.RegionConvert;
import com.taotao.cloud.sys.biz.model.entity.region.Region;
import com.taotao.cloud.sys.biz.repository.RegionRepository;
import com.taotao.cloud.sys.biz.repository.IRegionRepository;
import com.taotao.cloud.sys.biz.service.business.IRegionService;
import com.taotao.boot.webagg.service.impl.BaseSuperServiceImpl;
import lombok.*;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * RegionServiceImpl
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 20:37:52
 */
@Service
@AllArgsConstructor
public class RegionServiceImpl
	extends BaseSuperServiceImpl< Region, Long, IRegionMapper,RegionRepository, IRegionRepository>
	implements IRegionService {

	private final OkHttpService okHttpService;
	private final RedisRepository redisRepository;

	private static final String AMAP_KEY = System.getenv("AMAP_KEY");
	private static final String AMAP_SECURITY_KEY = System.getenv("AMAP_SECURITY_KEY");

	/**
	 * 同步请求地址
	 */
	private static final String syncUrl = "https://restapi.amap.com/v3/config/district?subdistrict=4&key=" + AMAP_KEY;

	@Override
	public List<RegionParentVO> queryRegionByParentId(Long parentId) {
		LambdaQueryWrapper<Region> query = new LambdaQueryWrapper<>();
		query.eq(Region::getParentId, parentId);
		List<Region> sysRegions = getBaseMapper().selectList(query);
		List<RegionParentVO> result = new ArrayList<>();
		if (CollUtil.isNotEmpty(sysRegions)) {
			sysRegions.forEach(sysRegion -> {
				RegionParentVO vo = new RegionParentVO(
					sysRegion.getId(), sysRegion.getName(), sysRegion.getCode(), new ArrayList<>());
				result.add(vo);
			});
		}
		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Region> getItem(Long parentId) {
		Object o = redisRepository.get(RedisConstant.REGIONS_PARENT_ID_KEY + parentId);
		if (Objects.nonNull(o)) {
			return (List<Region>) o;
		}

		LambdaQueryWrapper<Region> lambdaQueryWrapper = new LambdaQueryWrapper<>();
		lambdaQueryWrapper.eq(Region::getParentId, parentId);
		List<Region> regions = this.list(lambdaQueryWrapper);
		regions.sort(Comparator.comparing(Region::getDepth));

		redisRepository.setEx(RedisConstant.REGIONS_PARENT_ID_KEY + parentId, regions, 5 * 60);

		return regions;
	}

	@Override
	public Map<String, Object> getRegion(String cityCode, String townName) {
		// 获取地址信息
		Region region = this.baseMapper.selectOne(
			new QueryWrapper<Region>().eq("city_code", cityCode).eq("name", townName));
		if (region != null) {
			// 获取它的层级关系
			// String path = region.getPath();
			// String[] result = path.split(",");
			//// 因为有无用数据 所以先删除前两个
			// result = ArrayUtils.remove(result, 0);
			// result = ArrayUtils.remove(result, 0);
			//// 地址id
			// StringBuilder regionIds = new StringBuilder();
			//// 地址名称
			// StringBuilder regionNames = new StringBuilder();
			//// 循环构建新的数据
			// for (String regionId : result) {
			//	Region reg = this.baseMapper.selectById(regionId);
			//	if (reg != null) {
			//		regionIds.append(regionId).append(",");
			//		regionNames.append(reg.getName()).append(",");
			//	}
			// }
			// regionIds.append(region.getId());
			// regionNames.append(region.getName());
			//// 构建返回数据
			// Map<String, Object> obj = new HashMap<>(2);
			// obj.put("id", regionIds.toString());
			// obj.put("name", regionNames.toString());
			//
			// return obj;
		}
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<RegionVO> getAllCity() {
		Object o = redisRepository.get(RedisConstant.REGIONS_ALL_CITY_KEY);
		if (Objects.nonNull(o)) {
			return (List<RegionVO>) o;
		}

		LambdaQueryWrapper<Region> lambdaQueryWrapper = new LambdaQueryWrapper<>();
		// 查询所有省市
		lambdaQueryWrapper.in(Region::getLevel, "city", "province");
		List<RegionVO> regionVOS = regionTree(this.list(lambdaQueryWrapper));

		redisRepository.setEx(RedisConstant.REGIONS_ALL_CITY_KEY, regionVOS, 5 * 60);

		return regionVOS;
	}

	private List<RegionVO> regionTree(List<Region> regions) {
		List<RegionVO> regionVOS = new ArrayList<>();
		regions
			.stream()
			.filter(region -> ("province").equals(region.getLevel()))
			.forEach(item -> {
				RegionVO vo = new RegionVO();
				OrikaUtils.copy(item, vo);
				regionVOS.add(vo);
			});

		regions
			.stream()
			.filter(region -> ("city").equals(region.getLevel()))
			.forEach(item -> {
				for (RegionVO region : regionVOS) {
					if (region.getId().equals(item.getParentId())) {
						RegionVO vo = new RegionVO();
						OrikaUtils.copy(item, vo);
						region.getChildren().add(vo);
					}
				}
			});
		return regionVOS;
	}

	@Override
	public List<RegionParentVO> tree(Long parentId, Integer depth) {
		LambdaQueryWrapper<Region> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(Region::getParentId, parentId);

		// 得到一级节点菜单列表
		List<Region> sysRegions = getBaseMapper().selectList(wrapper);
		List<RegionParentVO> vos = new ArrayList<>();
		if (CollUtil.isNotEmpty(sysRegions)) {
			sysRegions.forEach(sysRegion -> {
				RegionParentVO vo = new RegionParentVO(
					sysRegion.getId(), sysRegion.getName(), sysRegion.getCode(), new ArrayList<>());
				vos.add(vo);
			});
		}

		if (!vos.isEmpty()) {
			vos.forEach(e -> findAllChild(e, 1, depth));
		}
		return vos;
	}

	@Override
	public List<RegionTreeVO> treeOther() {
		LambdaQueryWrapper<Region> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.orderByDesc(Region::getCreateTime);
		List<Region> list = list(queryWrapper);

		return RegionConvert.INSTANCE.convertTree(list)
			.stream()
			.filter(Objects::nonNull)
			.peek(e -> {
				e.setKey(e.getId());
				e.setValue(e.getId());
				e.setTitle(e.getName());
			})
			.toList();
	}

	public void findAllChild(RegionParentVO vo, int depth, int maxDepth) {
		if (depth >= maxDepth) {
			return;
		}
		LambdaQueryWrapper<Region> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(Region::getParentId, vo.getId());
		List<Region> sysRegions = getBaseMapper().selectList(wrapper);
		List<RegionParentVO> regions = new ArrayList<>();
		if (CollUtil.isNotEmpty(sysRegions)) {
			sysRegions.forEach(sysRegion -> {
				RegionParentVO region = new RegionParentVO(
					sysRegion.getId(), sysRegion.getName(), sysRegion.getCode(), new ArrayList<>());
				regions.add(region);
			});
		}

		vo.setChildren(regions);
		if (regions.size() > 0) {
			regions.forEach(e -> findAllChild(e, depth + 1, maxDepth));
		}
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	@SuppressWarnings("unchecked")
	public void synchronizationData(String url) {
		try {
			// 读取数据
			String jsonString;
			String signUrl = SignUtils.sign(AMAP_SECURITY_KEY, syncUrl);
			if (Objects.nonNull(okHttpService)) {
				jsonString = okHttpService
					.url(StrUtil.isBlank(url) ? signUrl : url)
					.get()
					.sync();
			} else {
				jsonString = HttpRequest.get(StrUtil.isBlank(url) ? signUrl : url)
					.useConsoleLog()
					.executeAsync()
					.join()
					.asString();
			}

			if (StrUtil.isNotBlank(jsonString)) {
				// 清空数据
				QueryWrapper<Region> queryWrapper = new QueryWrapper<>();
				queryWrapper.eq("version", "1");
				this.remove(queryWrapper);

				// 清空缓存的地区数据
				Set<String> keys = redisRepository.keys(RedisConstant.REGIONS_PATTERN);
				redisRepository.del(keys.toArray(new String[keys.size()]));

				// 构造存储数据库的对象集合
				// List<Region> regions = this.initData(jsonString);
				// for (int i = 0; i < (regions.size() / 100 + (regions.size() % 100 == 0 ? 0 : 1));
				//	i++) {
				//	int endPoint = Math.min((100 + (i * 100)), regions.size());
				//	this.saveOrUpdateBatch(regions.subList(i * 100, endPoint));
				// }
				//
				// MpUtils.batchUpdateOrInsert(regions, getBaseMapper().getClass(),
				//	(t, m) -> m.insert(t));

				// 重新设置缓存
				redisRepository.setEx(RedisConstant.REGIONS_KEY, jsonString, 30 * 24 * 60 * 60);
			}
		} catch (Exception e) {
			LogUtils.error("同步行政数据错误", e);
		}
	}

	// public static void main(String[] args) throws IllegalAccessException {
	//	String signUrl = SignUtils.sign(AMAP_SECURITY_KEY, syncUrl);
	//	String request = HttpUtils.request(signUrl, "GET");
	//	LogUtils.info("slfdjsldf");
	//	List<Region> regions = initData(request);
	//	LogUtils.info("lsdfsldf");
	// }

	/**
	 * 构造数据模型
	 *
	 * @param jsonString jsonString
	 */
	private static List<Region> initData(String jsonString) {
		// 最终数据承载对象
		List<Region> regions = new ArrayList<>();
		JSONObject jsonObject = JSONObject.parseObject(jsonString);
		// 获取到国家及下面所有的信息 开始循环插入，这里可以写成递归调用，但是不如这样方便查看、理解
		JSONArray countryAll = jsonObject.getJSONArray("districts");
		for (int i = 0; i < countryAll.size(); i++) {
			JSONObject contry = countryAll.getJSONObject(i);
			String countryCode = contry.getString("citycode");
			String countryAdCode = contry.getString("adcode");
			String countryName = contry.getString("name");
			String countryCenter = contry.getString("center");
			String countryLevel = contry.getString("level");

			List<Long> idTree = new ArrayList<>();
			List<String> codeTree = new ArrayList<>();

			// 插入国家
			Long countryId = insert(
				regions,
				null,
				countryCode,
				countryAdCode,
				countryName,
				countryCenter,
				countryLevel,
				idTree,
				codeTree,
				1,
				i + 1);

			JSONArray provinceAll = contry.getJSONArray("districts");
			for (int j = 0; j < provinceAll.size(); j++) {
				JSONObject province = provinceAll.getJSONObject(j);
				String provinceCode = province.getString("citycode");
				String provinceAdcode = province.getString("adcode");
				String provinceName = province.getString("name");
				String provinceCenter = province.getString("center");
				String provinceLevel = province.getString("level");

				List<Long> countryIdTree = List.of(countryId);
				List<String> countryCodeTree = List.of(countryAdCode);

				// 1.插入省
				Long provinceId = insert(
					regions,
					countryId,
					provinceCode,
					provinceAdcode,
					provinceName,
					provinceCenter,
					provinceLevel,
					countryIdTree,
					countryCodeTree,
					2,
					j + 1);

				JSONArray cityAll = province.getJSONArray("districts");
				for (int z = 0; z < cityAll.size(); z++) {
					JSONObject city = cityAll.getJSONObject(z);
					String cityCode = city.getString("citycode");
					String cityAdcode = city.getString("adcode");
					String cityName = city.getString("name");
					String cityCenter = city.getString("center");
					String cityLevel = city.getString("level");

					List<Long> provinceIdTree = List.of(countryId, provinceId);
					List<String> provinceCodeTree = List.of(countryAdCode, provinceAdcode);

					// 2.插入市
					Long cityId = insert(
						regions,
						provinceId,
						cityCode,
						cityAdcode,
						cityName,
						cityCenter,
						cityLevel,
						provinceIdTree,
						provinceCodeTree,
						3,
						z + 1);

					JSONArray districtAll = city.getJSONArray("districts");
					for (int w = 0; w < districtAll.size(); w++) {
						JSONObject district = districtAll.getJSONObject(w);
						String districtCode = district.getString("citycode");
						String districtAdcode = district.getString("adcode");
						String districtName = district.getString("name");
						String districtCenter = district.getString("center");
						String districtLevel = district.getString("level");

						List<Long> cityIdTree = List.of(countryId, provinceId, cityId);
						List<String> cityCodeTree = List.of(countryAdCode, provinceAdcode, cityAdcode);

						// 3.插入区县
						Long districtId = insert(
							regions,
							cityId,
							districtCode,
							districtAdcode,
							districtName,
							districtCenter,
							districtLevel,
							cityIdTree,
							cityCodeTree,
							4,
							w + 1);

						// 有需要可以继续向下遍历
						JSONArray streetAll = district.getJSONArray("districts");
						for (int r = 0; r < streetAll.size(); r++) {
							JSONObject street = streetAll.getJSONObject(r);
							String streetCode = street.getString("citycode");
							String streetAdcode = street.getString("adcode");
							String streetName = street.getString("name");
							String streetCenter = street.getString("center");
							String streetLevel = street.getString("level");

							List<Long> districtIdTree = List.of(countryId, provinceId, cityId, districtId);
							List<String> districtCodeTree =
								List.of(countryAdCode, provinceAdcode, cityAdcode, districtAdcode);
							;

							// 4.插入街道
							insert(
								regions,
								districtId,
								streetCode,
								streetAdcode,
								streetName,
								streetCenter,
								streetLevel,
								districtIdTree,
								districtCodeTree,
								5,
								r + 1);
						}
					}
				}
			}
		}
		return regions;
	}

	/**
	 * 公共的插入方法
	 *
	 * @param parentId 父id
	 * @param cityCode 城市编码
	 * @param code     区域编码 街道没有独有的code，均继承父类（区县）的code
	 * @param name     城市名称 （行政区名称）
	 * @param center   地理坐标
	 * @param level    country:国家 province:省份（直辖市会在province和city显示） city:市（直辖市会在province和city显示）
	 *                 district:区县 street:街道
	 */
	public static Long insert(
		List<Region> regions,
		Long parentId,
		String cityCode,
		String code,
		String name,
		String center,
		String level,
		List<Long> idTree,
		List<String> codeTree,
		Integer depth,
		Integer orderNum) {
		//  \"citycode\": [],\n" +
		//         "        \"adcode\": \"100000\",\n" +
		//         "        \"name\": \"中华人民共和国\",\n" +
		//         "        \"center\": \"116.3683244,39.915085\",\n" +
		//         "        \"level\": \"country\",\n" +
		Region record = new Region();
		if (!("[]").equals(code)) {
			record.setCode(code);
		}
		if (!("[]").equals(cityCode)) {
			record.setCityCode(cityCode);
		}
		String[] split = center.split(",");
		record.setLng(split[0]);
		record.setLat(split[1]);
		record.setLevel(level);
		record.setName(name);
		record.setParentId(parentId);
		if ("100000".equals(code) && "country".equals(level)) {
			record.setId(1L);
		} else {
			record.setId(IdGeneratorUtils.getId());
		}

		// StringBuilder megName = new StringBuilder();
		// for (int i = 0; i < ids.length; i++) {
		//	megName.append(ids[i]);
		//	if (i < ids.length - 1) {
		//		megName.append(",");
		//	}
		// }
		record.setOrderNum(orderNum);

		ArrayList<Long> idTrees = new ArrayList<>(idTree);
		idTrees.add(record.getId());
		record.setIdTree(idTrees);

		ArrayList<String> codeTrees = new ArrayList<>(codeTree);
		codeTrees.add(code);
		record.setCodeTree(codeTrees);

		record.setDepth(depth);

		regions.add(record);
		return record.getId();
	}
}
