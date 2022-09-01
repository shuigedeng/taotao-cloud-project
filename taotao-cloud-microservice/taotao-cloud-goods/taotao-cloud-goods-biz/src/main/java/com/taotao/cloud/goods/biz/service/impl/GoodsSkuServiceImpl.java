package com.taotao.cloud.goods.biz.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.common.enums.CachePrefix;
import com.taotao.cloud.common.enums.PromotionTypeEnum;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.goods.api.model.dto.GoodsSkuStockDTO;
import com.taotao.cloud.goods.api.enums.GoodsAuthEnum;
import com.taotao.cloud.goods.api.enums.GoodsStatusEnum;
import com.taotao.cloud.goods.biz.listener.GeneratorEsGoodsIndexEvent;
import com.taotao.cloud.goods.api.model.query.GoodsPageQuery;
import com.taotao.cloud.goods.api.model.vo.GoodsSkuSpecVO;
import com.taotao.cloud.goods.api.model.vo.GoodsSkuSpecGalleryVO;
import com.taotao.cloud.goods.api.model.vo.GoodsSkuParamsVO;
import com.taotao.cloud.goods.api.model.vo.SpecValueVO;
import com.taotao.cloud.goods.biz.elasticsearch.EsGoodsAttribute;
import com.taotao.cloud.goods.biz.elasticsearch.EsGoodsIndex;
import com.taotao.cloud.goods.biz.model.entity.Goods;
import com.taotao.cloud.goods.biz.model.entity.GoodsSku;
import com.taotao.cloud.goods.biz.mapper.IGoodsSkuMapper;
import com.taotao.cloud.goods.biz.mapstruct.IGoodsSkuMapStruct;
import com.taotao.cloud.goods.biz.service.ICategoryService;
import com.taotao.cloud.goods.biz.service.IEsGoodsIndexService;
import com.taotao.cloud.goods.biz.service.IGoodsGalleryService;
import com.taotao.cloud.goods.biz.service.IGoodsService;
import com.taotao.cloud.goods.biz.service.IGoodsSkuService;
import com.taotao.cloud.goods.biz.util.EsIndexUtil;
import com.taotao.cloud.member.api.enums.EvaluationGradeEnum;
import com.taotao.cloud.member.api.feign.IFeignMemberEvaluationService;
import com.taotao.cloud.member.api.model.query.EvaluationPageQuery;
import com.taotao.cloud.promotion.api.enums.CouponGetEnum;
import com.taotao.cloud.promotion.api.feign.IFeignPromotionGoodsService;
import com.taotao.cloud.promotion.api.query.PromotionGoodsSearchParams;
import com.taotao.cloud.promotion.api.web.vo.PromotionGoodsVO;
import com.taotao.cloud.redis.repository.RedisRepository;
import com.taotao.cloud.stream.framework.rocketmq.RocketmqSendCallbackBuilder;
import com.taotao.cloud.stream.framework.rocketmq.tags.GoodsTagsEnum;
import com.taotao.cloud.stream.properties.RocketmqCustomProperties;
import lombok.AllArgsConstructor;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 商品sku业务层实现
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:02:44
 */
@AllArgsConstructor
@Service
public class GoodsSkuServiceImpl extends ServiceImpl<IGoodsSkuMapper, GoodsSku> implements
	IGoodsSkuService {

	/**
	 * 缓存服务
	 */
	private final RedisRepository redisRepository;
	/**
	 * 分类服务
	 */
	private final ICategoryService categoryService;
	/**
	 * 商品相册服务
	 */
	private final IGoodsGalleryService goodsGalleryService;
	/**
	 * rocketMq服务
	 */
	private final RocketMQTemplate rocketMQTemplate;
	/**
	 * rocketMq配置
	 */
	private final RocketmqCustomProperties rocketmqCustomProperties;
	/**
	 * 会员评价服务
	 */
	private final IFeignMemberEvaluationService memberEvaluationService;
	/**
	 * 商品服务
	 */
	private final IGoodsService goodsService;
	/**
	 * 商品索引服务
	 */
	private final IEsGoodsIndexService goodsIndexService;
	/**
	 * 促销活动商品服务
	 */
	private final IFeignPromotionGoodsService promotionGoodsService;
	/**
	 * ApplicationEventPublisher
	 */
	private final ApplicationEventPublisher applicationEventPublisher;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean add(List<Map<String, Object>> skuList, Goods goods) {
		// 检查是否需要生成索引
		List<GoodsSku> newSkuList;
		//如果有规格
		if (skuList != null && !skuList.isEmpty()) {
			// 添加商品sku
			newSkuList = this.addGoodsSku(skuList, goods);
		} else {
			throw new BusinessException(ResultEnum.MUST_HAVE_GOODS_SKU);
		}

		this.updateStock(newSkuList);
		if (!newSkuList.isEmpty()) {
			generateEs(goods);
		}
		return true;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean update(List<Map<String, Object>> skuList, Goods goods,
						  Boolean regeneratorSkuFlag) {
		// 是否存在规格
		if (skuList == null || skuList.isEmpty()) {
			throw new BusinessException(ResultEnum.MUST_HAVE_GOODS_SKU);
		}
		List<GoodsSku> newSkuList;
		//删除旧的sku信息
		if (Boolean.TRUE.equals(regeneratorSkuFlag)) {
			List<GoodsSkuSpecGalleryVO> goodsListByGoodsId = getGoodsListByGoodsId(goods.getId());
			List<Long> oldSkuIds = new ArrayList<>();
			//删除旧索引
			for (GoodsSkuSpecGalleryVO goodsSkuSpecGalleryVO : goodsListByGoodsId) {
				oldSkuIds.add(goodsSkuSpecGalleryVO.getId());
				redisRepository.del(IGoodsSkuService.getCacheKeys(goodsSkuSpecGalleryVO.getId()));
			}
			goodsIndexService.deleteIndexByIds(oldSkuIds);
			this.removeByIds(oldSkuIds);
			//删除sku相册
			goodsGalleryService.removeByIds(oldSkuIds);
			// 添加商品sku
			newSkuList = this.addGoodsSku(skuList, goods);

			//发送mq消息
			String destination =
				rocketmqCustomProperties.getGoodsTopic() + ":" + GoodsTagsEnum.SKU_DELETE.name();
			rocketMQTemplate.asyncSend(destination, JSONUtil.toJsonStr(oldSkuIds),
				RocketmqSendCallbackBuilder.commonCallback());
		} else {
			newSkuList = new ArrayList<>();
			for (Map<String, Object> map : skuList) {
				GoodsSku sku = new GoodsSku();
				//设置商品信息
				goodsInfo(sku, goods);
				//设置商品规格信息
				skuInfo(sku, goods, map, null);
				newSkuList.add(sku);
				//如果商品状态值不对，则es索引移除
				if (goods.getIsAuth().equals(GoodsAuthEnum.PASS.name()) && goods.getMarketEnable()
					.equals(GoodsStatusEnum.UPPER.name())) {
					goodsIndexService.deleteIndexById(sku.getId());
					this.clearCache(sku.getId());
				}
			}
			this.updateBatchById(newSkuList);
		}
		this.updateStock(newSkuList);
		if (GoodsAuthEnum.PASS.name().equals(goods.getIsAuth()) && !newSkuList.isEmpty()) {
			generateEs(goods);
		}

		return true;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean update(GoodsSku goodsSku) {
		this.updateById(goodsSku);
		redisRepository.del(IGoodsSkuService.getCacheKeys(goodsSku.getId()));
		redisRepository.set(IGoodsSkuService.getCacheKeys(goodsSku.getId()), goodsSku);
		return true;
	}

	@Override
	public Boolean clearCache(Long skuId) {
		redisRepository.del(IGoodsSkuService.getCacheKeys(skuId));
		return true;
	}

	@Override
	public GoodsSku getGoodsSkuByIdFromCache(Long skuId) {
		//获取缓存中的sku
		GoodsSku goodsSku = (GoodsSku) redisRepository.get(IGoodsSkuService.getCacheKeys(skuId));
		//如果缓存中没有信息，则查询数据库，然后写入缓存
		if (goodsSku == null) {
			goodsSku = this.getById(skuId);
			if (goodsSku == null) {
				return null;
			}
			redisRepository.set(IGoodsSkuService.getCacheKeys(skuId), goodsSku);
		}

		//获取商品库存
		Integer stock = (Integer) redisRepository.get(IGoodsSkuService.getStockCacheKey(skuId));

		//库存不为空,库存与缓存中不一致
		if (stock != null && !goodsSku.getQuantity().equals(stock)) {
			//写入最新的库存信息
			goodsSku.setQuantity(stock);
			redisRepository.set(IGoodsSkuService.getCacheKeys(goodsSku.getId()), goodsSku);
		}
		return goodsSku;
	}

	@Override
	public Map<String, Object> getGoodsSkuDetail(Long goodsId, Long skuId) {
		Map<String, Object> map = new HashMap<>(16);
		//获取商品VO
		GoodsSkuParamsVO goodsSkuParamsVO = goodsService.getGoodsVO(goodsId);
		//如果skuid为空，则使用商品VO中sku信息获取
		if (Objects.nonNull(skuId)) {
			skuId = goodsSkuParamsVO.getSkuList().get(0).getId();
		}

		//从缓存拿商品Sku
		GoodsSku goodsSku = this.getGoodsSkuByIdFromCache(skuId);
		//如果使用商品ID无法查询SKU则返回错误
		if (goodsSkuParamsVO == null || goodsSku == null) {
			throw new BusinessException(ResultEnum.GOODS_NOT_EXIST);
		}

		//商品下架||商品未审核通过||商品删除，则提示：商品已下架
		if (GoodsStatusEnum.DOWN.name().equals(goodsSkuParamsVO.getMarketEnable())
			|| !GoodsAuthEnum.PASS.name().equals(goodsSkuParamsVO.getIsAuth())
			|| Boolean.TRUE.equals(goodsSkuParamsVO.getDelFlag())) {
			throw new BusinessException(ResultEnum.GOODS_NOT_EXIST);
		}

		//获取当前商品的索引信息
		EsGoodsIndex goodsIndex = goodsIndexService.findById(skuId);
		if (goodsIndex == null) {
			goodsIndex = goodsIndexService.getResetEsGoodsIndex(goodsSku,
				goodsSkuParamsVO.getGoodsParamsDTOList());
		}

		//商品规格
		GoodsSkuSpecGalleryVO goodsSkuDetail = this.getGoodsSkuVO(goodsSku);

		Map<String, Object> promotionMap = goodsIndex.getPromotionMap();
		//设置当前商品的促销价格
		if (promotionMap != null && !promotionMap.isEmpty()) {
			promotionMap = promotionMap.entrySet().stream().parallel().filter(i -> {
				JSONObject jsonObject = JSONUtil.parseObj(i.getValue());
				// 过滤活动赠送优惠券和无效时间的活动
				return (jsonObject.get("getType") == null || jsonObject.get("getType", String.class)
					.equals(CouponGetEnum.FREE.name())) &&
					(jsonObject.get("startTime") != null
						&& jsonObject.get("startTime", Date.class).getTime()
						<= System.currentTimeMillis()) &&
					(jsonObject.get("endTime") == null
						|| jsonObject.get("endTime", Date.class).getTime()
						>= System.currentTimeMillis());
			}).collect(Collectors.toMap(Entry::getKey, Entry::getValue));

			Optional<Entry<String, Object>> containsPromotion = promotionMap.entrySet().stream()
				.filter(i ->
					i.getKey().contains(PromotionTypeEnum.SECKILL.name()) || i.getKey()
						.contains(PromotionTypeEnum.PINTUAN.name())).findFirst();
			if (containsPromotion.isPresent()) {
				JSONObject jsonObject = JSONUtil.parseObj(containsPromotion.get().getValue());
				PromotionGoodsSearchParams searchParams = new PromotionGoodsSearchParams();
				searchParams.setSkuId(String.valueOf(skuId));
				searchParams.setPromotionId(jsonObject.get("id").toString());
				PromotionGoodsVO promotionsGoods = promotionGoodsService.getPromotionsGoods(
					searchParams).data();
				if (promotionsGoods != null && promotionsGoods.getPrice() != null) {
					goodsSkuDetail.setPromotionFlag(true);
					goodsSkuDetail.setPromotionPrice(promotionsGoods.getPrice());
				}
			} else {
				goodsSkuDetail.setPromotionFlag(false);
				goodsSkuDetail.setPromotionPrice(null);
			}
		}
		map.put("data", goodsSkuDetail);

		//获取分类
		String[] split = goodsSkuDetail.getCategoryPath().split(",");
		//map.put("categoryName", categoryService.getCategoryNameByIds(Arrays.asList(split)));

		//获取规格信息
		map.put("specs", this.groupBySkuAndSpec(goodsSkuParamsVO.getSkuList()));
		map.put("promotionMap", promotionMap);

		//获取参数信息
		if (goodsSkuParamsVO.getGoodsParamsDTOList() != null && !goodsSkuParamsVO.getGoodsParamsDTOList().isEmpty()) {
			map.put("goodsParamsDTOList", goodsSkuParamsVO.getGoodsParamsDTOList());
		}

		//记录用户足迹
		//if (UserContext.getCurrentUser() != null) {
		//	FootPrint footPrint = new FootPrint(UserContext.getCurrentUser().getId(), goodsId,
		//		skuId);
		//	String destination =
		//		rocketmqCustomProperties.getGoodsTopic() + ":" + GoodsTagsEnum.VIEW_GOODS.name();
		//	rocketMQTemplate.asyncSend(destination, footPrint,
		//		RocketmqSendCallbackBuilder.commonCallback());
		//}
		return map;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean updateGoodsSkuStatus(Goods goods) {
		LambdaUpdateWrapper<GoodsSku> updateWrapper = new LambdaUpdateWrapper<>();
		updateWrapper.eq(GoodsSku::getGoodsId, goods.getId());
		updateWrapper.set(GoodsSku::getMarketEnable, goods.getMarketEnable());
		updateWrapper.set(GoodsSku::getIsAuth, goods.getIsAuth());
		updateWrapper.set(GoodsSku::getDelFlag, goods.getDelFlag());
		boolean update = this.update(updateWrapper);
		if (Boolean.TRUE.equals(update)) {
			List<GoodsSku> goodsSkus = this.getGoodsSkuListByGoodsId(goods.getId());
			for (GoodsSku sku : goodsSkus) {
				redisRepository.del(IGoodsSkuService.getCacheKeys(sku.getId()));
				redisRepository.set(IGoodsSkuService.getCacheKeys(sku.getId()), sku);
			}
			if (!goodsSkus.isEmpty()) {
				generateEs(goods);
			}
		}
		return true;
	}

	@Override
	public List<GoodsSku> getGoodsSkuByIdFromCache(List<Long> ids) {
		List<String> keys = new ArrayList<>();
		for (Long id : ids) {
			keys.add(IGoodsSkuService.getCacheKeys(id));
		}
		List<GoodsSku> list = redisRepository.mGet(keys);
		if (list == null || list.isEmpty()) {
			list = new ArrayList<>();
			List<GoodsSku> goodsSkus = listByIds(ids);
			for (GoodsSku skus : goodsSkus) {
				redisRepository.set(IGoodsSkuService.getCacheKeys(skus.getId()), skus);
				list.add(skus);
			}
		}
		return list;
	}

	@Override
	public List<GoodsSkuSpecGalleryVO> getGoodsListByGoodsId(Long goodsId) {
		LambdaQueryWrapper<GoodsSku> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.eq(GoodsSku::getGoodsId, goodsId);
		List<GoodsSku> list = this.list(queryWrapper);
		return this.getGoodsSkuVOList(list);
	}

	@Override
	public List<GoodsSku> getGoodsSkuListByGoodsId(Long goodsId) {
		return this.list(new LambdaQueryWrapper<GoodsSku>().eq(GoodsSku::getGoodsId, goodsId));
	}

	@Override
	public List<GoodsSkuSpecGalleryVO> getGoodsSkuVOList(List<GoodsSku> list) {
		List<GoodsSkuSpecGalleryVO> goodsSkuSpecGalleryVOS = new ArrayList<>();
		for (GoodsSku goodsSku : list) {
			GoodsSkuSpecGalleryVO goodsSkuSpecGalleryVO = this.getGoodsSkuVO(goodsSku);
			goodsSkuSpecGalleryVOS.add(goodsSkuSpecGalleryVO);
		}
		return goodsSkuSpecGalleryVOS;
	}

	@Override
	public GoodsSkuSpecGalleryVO getGoodsSkuVO(GoodsSku goodsSku) {
		//初始化商品
		GoodsSkuSpecGalleryVO goodsSkuSpecGalleryVO = IGoodsSkuMapStruct.INSTANCE.goodsSkuToGoodsSkuVO(goodsSku);
		//获取规格信息
		JSONObject jsonObject = JSONUtil.parseObj(goodsSku.getSpecs());
		//规格值信息
		List<SpecValueVO> specValueVOS = new ArrayList<>();
		//sku相册信息
		List<String> goodsGalleryList = new ArrayList<>();

		//循环提交的sku表单
		for (Entry<String, Object> entry : jsonObject.entrySet()) {
			SpecValueVO specValueVO = new SpecValueVO();
			if ("images".equals(entry.getKey())) {
				specValueVO.setSpecName(entry.getKey());
				if (entry.getValue().toString().contains("url")) {
					List<SpecValueVO.SpecImages> specImages = JSONUtil.toList(
						JSONUtil.parseArray(entry.getValue()), SpecValueVO.SpecImages.class);
					specValueVO.setSpecImage(specImages);
					goodsGalleryList = specImages.stream()
						.map(SpecValueVO.SpecImages::getUrl).toList();
				}
			} else {
				specValueVO.setSpecName(entry.getKey());
				specValueVO.setSpecValue(entry.getValue().toString());
			}
			specValueVOS.add(specValueVO);
		}
		goodsSkuSpecGalleryVO.setGoodsGalleryList(goodsGalleryList);
		goodsSkuSpecGalleryVO.setSpecList(specValueVOS);
		return goodsSkuSpecGalleryVO;
	}

	@Override
	public IPage<GoodsSku> getGoodsSkuByPage(GoodsPageQuery searchParams) {
		return this.page(searchParams.buildMpPage(),
			searchParams.queryWrapper());
	}

	@Override
	public List<GoodsSku> getGoodsSkuByList(GoodsPageQuery searchParams) {
		return this.list(searchParams.queryWrapper());
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean updateStocks(List<GoodsSkuStockDTO> goodsSkuStockDTOS) {
		for (GoodsSkuStockDTO goodsSkuStockDTO : goodsSkuStockDTOS) {
			this.updateStock(goodsSkuStockDTO.getSkuId(), goodsSkuStockDTO.getQuantity());
		}
		return true;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean updateStock(Long skuId, Integer quantity) {
		GoodsSku goodsSku = getGoodsSkuByIdFromCache(skuId);
		if (goodsSku != null) {
			if (quantity <= 0) {
				goodsIndexService.deleteIndexById(goodsSku.getId());
			}
			goodsSku.setQuantity(quantity);
			boolean update = this.update(
				new LambdaUpdateWrapper<GoodsSku>().eq(GoodsSku::getId, skuId)
					.set(GoodsSku::getQuantity, quantity));
			if (update) {
				redisRepository.del(CachePrefix.GOODS.getPrefix() + goodsSku.getGoodsId());
			}
			redisRepository.set(IGoodsSkuService.getCacheKeys(skuId), goodsSku);
			redisRepository.set(IGoodsSkuService.getStockCacheKey(skuId), quantity);

			//更新商品库存
			List<GoodsSku> goodsSkus = new ArrayList<>();
			goodsSkus.add(goodsSku);
			this.updateGoodsStuck(goodsSkus);
		}
		return true;
	}

	@Override
	public Integer getStock(Long skuId) {
		String cacheKeys = IGoodsSkuService.getStockCacheKey(skuId);
		Integer stock = (Integer) redisRepository.get(cacheKeys);
		if (stock != null) {
			return stock;
		} else {
			GoodsSku goodsSku = getGoodsSkuByIdFromCache(skuId);
			redisRepository.set(cacheKeys, goodsSku.getQuantity());
			return goodsSku.getQuantity();
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean updateGoodsStuck(List<GoodsSku> goodsSkus) {
		//商品id集合 hashset 去重复
		Set<Long> goodsIds = new HashSet<>();
		for (GoodsSku sku : goodsSkus) {
			goodsIds.add(sku.getGoodsId());
		}
		//获取相关的sku集合
		LambdaQueryWrapper<GoodsSku> lambdaQueryWrapper = new LambdaQueryWrapper<>();
		lambdaQueryWrapper.in(GoodsSku::getGoodsId, goodsIds);
		List<GoodsSku> goodsSkuList = this.list(lambdaQueryWrapper);

		//统计每个商品的库存
		for (Long goodsId : goodsIds) {
			//库存
			Integer quantity = 0;
			for (GoodsSku goodsSku : goodsSkuList) {
				if (goodsId.equals(goodsSku.getGoodsId())) {
					quantity += goodsSku.getQuantity();
				}
			}
			//保存商品库存结果
			goodsService.updateStock(goodsId, quantity);
		}
		return true;

	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean updateGoodsSkuCommentNum(Long skuId) {
		//获取商品信息
		GoodsSku goodsSku = this.getGoodsSkuByIdFromCache(skuId);

		EvaluationPageQuery queryParams = new EvaluationPageQuery();
		queryParams.setGrade(EvaluationGradeEnum.GOOD.name());
		queryParams.setSkuId(goodsSku.getId());
		//好评数量
		long highPraiseNum = memberEvaluationService.getEvaluationCount(queryParams).data();

		//更新商品评价数量
		goodsSku.setCommentNum(goodsSku.getCommentNum() != null ? goodsSku.getCommentNum() + 1 : 1);

		//todo 此处需要修改 好评率
		BigDecimal grade = BigDecimal.valueOf(NumberUtil.mul(
			NumberUtil.div(highPraiseNum, goodsSku.getCommentNum().doubleValue(), 2), 100));
		goodsSku.setGrade(grade);
		//修改规格
		this.update(goodsSku);

		//修改规格索引,发送mq消息
		Map<String, Object> updateIndexFieldsMap = EsIndexUtil.getUpdateIndexFieldsMap(
			MapUtil.builder(new HashMap<String, Object>()).put("id", goodsSku.getId()).build(),
			MapUtil.builder(new HashMap<String, Object>())
				.put("commentNum", goodsSku.getCommentNum()).put("highPraiseNum", highPraiseNum)
				.put("grade", grade).build());

		String destination = rocketmqCustomProperties.getGoodsTopic() + ":"
			+ GoodsTagsEnum.UPDATE_GOODS_INDEX_FIELD.name();
		rocketMQTemplate.asyncSend(destination, JSONUtil.toJsonStr(updateIndexFieldsMap),
			RocketmqSendCallbackBuilder.commonCallback());

		//修改商品的评价数量
		goodsService.updateGoodsCommentNum(goodsSku.getGoodsId());
		return true;
	}

	@Override
	public List<String> getSkuIdsByGoodsId(Long goodsId) {
		return this.baseMapper.getGoodsSkuIdByGoodsId(goodsId);
	}

	@Override
	public void generateEs(Goods goods) {
		//不生成没有审核通过且没有上架的商品
		if (!GoodsStatusEnum.UPPER.name().equals(goods.getMarketEnable())
			|| !GoodsAuthEnum.PASS.name().equals(goods.getIsAuth())) {
			return;
		}
		applicationEventPublisher.publishEvent(
			new GeneratorEsGoodsIndexEvent("生成商品索引事件", goods.getId()));
	}

	/**
	 * 修改库存
	 *
	 * @param goodsSkus 商品SKU
	 */
	private void updateStock(List<GoodsSku> goodsSkus) {
		//总库存数量
		Integer quantity = 0;
		for (GoodsSku sku : goodsSkus) {
			this.updateStock(sku.getId(), sku.getQuantity());
			quantity += sku.getQuantity();
		}

		//修改商品库存
		goodsService.updateStock(goodsSkus.get(0).getGoodsId(), quantity);
	}

	/**
	 * 增加sku集合
	 *
	 * @param skuList sku列表
	 * @param goods   商品信息
	 */
	List<GoodsSku> addGoodsSku(List<Map<String, Object>> skuList, Goods goods) {
		List<GoodsSku> skus = new ArrayList<>();
		for (Map<String, Object> skuVO : skuList) {
			Map<String, Object> resultMap = this.add(skuVO, goods);
			GoodsSku goodsSku = (GoodsSku) resultMap.get("goodsSku");
			if (goods.getSelfOperated() != null) {
				goodsSku.setSelfOperated(goods.getSelfOperated());
			}
			goodsSku.setGoodsType(goods.getGoodsType());
			skus.add(goodsSku);
			redisRepository.set(IGoodsSkuService.getStockCacheKey(goodsSku.getId()),
				goodsSku.getQuantity());
		}
		this.saveBatch(skus);
		return skus;
	}

	/**
	 * 添加商品规格
	 *
	 * @param map   规格属性
	 * @param goods 商品
	 * @return 规格商品
	 */
	private Map<String, Object> add(Map<String, Object> map, Goods goods) {
		Map<String, Object> resultMap = new HashMap<>(2);
		GoodsSku sku = new GoodsSku();

		//商品索引
		EsGoodsIndex esGoodsIndex = new EsGoodsIndex();

		//设置商品信息
		goodsInfo(sku, goods);
		//设置商品规格信息
		skuInfo(sku, goods, map, esGoodsIndex);

		//esGoodsIndex.setGoodsSku(sku);
		resultMap.put("goodsSku", sku);
		resultMap.put("goodsIndex", esGoodsIndex);
		return resultMap;
	}

	/**
	 * 设置规格商品的商品信息
	 *
	 * @param sku   规格
	 * @param goods 商品
	 */
	private void goodsInfo(GoodsSku sku, Goods goods) {
		//商品基本信息
		sku.setGoodsId(goods.getId());

		sku.setSellingPoint(goods.getSellingPoint());
		sku.setCategoryPath(goods.getCategoryPath());
		sku.setBrandId(goods.getBrandId());
		sku.setMarketEnable(goods.getMarketEnable());
		sku.setIntro(goods.getIntro());
		sku.setMobileIntro(goods.getMobileIntro());
		sku.setGoodsUnit(goods.getGoodsUnit());
		sku.setGrade(BigDecimal.valueOf(100));
		//商品状态
		sku.setIsAuth(goods.getIsAuth());
		sku.setSalesModel(goods.getSalesModel());
		//卖家信息
		sku.setStoreId(goods.getStoreId());
		sku.setStoreName(goods.getStoreName());
		sku.setStoreCategoryPath(goods.getStoreCategoryPath());
		sku.setFreightTemplateId(goods.getTemplateId());
		sku.setRecommend(goods.getRecommend());
	}

	/**
	 * 设置商品规格信息
	 *
	 * @param sku          规格商品
	 * @param goods        商品
	 * @param map          规格信息
	 * @param esGoodsIndex 商品索引
	 */
	private void skuInfo(GoodsSku sku, Goods goods, Map<String, Object> map,
						 EsGoodsIndex esGoodsIndex) {
		//规格简短信息
		StringBuilder simpleSpecs = new StringBuilder();
		//商品名称
		StringBuilder goodsName = new StringBuilder(goods.getGoodsName());
		//规格商品缩略图
		String thumbnail = "";
		String small = "";
		//规格值
		Map<String, Object> specMap = new HashMap<>(16);
		//商品属性
		List<EsGoodsAttribute> attributes = new ArrayList<>();

		//获取规格信息
		for (Entry<String, Object> spec : map.entrySet()) {
			//保存规格信息
			if (("id").equals(spec.getKey()) || ("sn").equals(spec.getKey()) || ("cost").equals(
				spec.getKey())
				|| ("price").equals(spec.getKey()) || ("quantity").equals(spec.getKey())
				|| ("weight").equals(spec.getKey())) {
			} else {
				specMap.put(spec.getKey(), spec.getValue());
				if (("images").equals(spec.getKey())) {
					//设置规格商品缩略图
					List<Map<String, String>> images = (List<Map<String, String>>) spec.getValue();
					if (images == null || images.isEmpty()) {
						continue;
					}
					//设置规格商品缩略图
					//如果规格没有图片，则用商品图片复盖。有则增加规格图片，放在商品图片集合之前
					if (CharSequenceUtil.isNotEmpty(spec.getValue().toString())) {
						thumbnail = goodsGalleryService.getGoodsGallery(images.get(0).get("url"))
							.getThumbnail();
						small = goodsGalleryService.getGoodsGallery(images.get(0).get("url"))
							.getSmall();
					}
				} else {
					if (spec.getValue() != null) {
						//设置商品名称
						goodsName.append(" ").append(spec.getValue());
						//规格简短信息
						simpleSpecs.append(" ").append(spec.getValue());
					}
				}
			}
		}
		//设置规格信息
		sku.setGoodsName(goodsName.toString());
		sku.setThumbnail(thumbnail);
		sku.setSmall(small);

		//规格信息
		sku.setId(Convert.toLong(map.get("id"), null));
		sku.setSn(Convert.toStr(map.get("sn")));
		sku.setWeight(Convert.toBigDecimal(map.get("weight"), BigDecimal.ZERO));
		sku.setPrice(Convert.toBigDecimal(map.get("price"), BigDecimal.ZERO));
		sku.setCost(Convert.toBigDecimal(map.get("cost"), BigDecimal.ZERO));
		sku.setQuantity(Convert.toInt(map.get("quantity"), 0));
		sku.setSpecs(JSONUtil.toJsonStr(specMap));
		sku.setSimpleSpecs(simpleSpecs.toString());

		if (esGoodsIndex != null) {
			//商品索引
			esGoodsIndex.setAttrList(attributes);
		}
	}

	/**
	 * 根据商品分组商品sku及其规格信息
	 *
	 * @param goodsSkuSpecGalleryVOList 商品VO列表
	 * @return 分组后的商品sku及其规格信息
	 */
	private List<GoodsSkuSpecVO> groupBySkuAndSpec(List<GoodsSkuSpecGalleryVO> goodsSkuSpecGalleryVOList) {
		List<GoodsSkuSpecVO> skuSpecVOList = new ArrayList<>();
		for (GoodsSkuSpecGalleryVO goodsSkuSpecGalleryVO : goodsSkuSpecGalleryVOList) {
			GoodsSkuSpecVO specVO = new GoodsSkuSpecVO();
			specVO.setSkuId(goodsSkuSpecGalleryVO.getId());
			specVO.setSpecValues(goodsSkuSpecGalleryVO.getSpecList());
			specVO.setQuantity(goodsSkuSpecGalleryVO.getQuantity());
			skuSpecVOList.add(specVO);
		}
		return skuSpecVOList;
	}

}
