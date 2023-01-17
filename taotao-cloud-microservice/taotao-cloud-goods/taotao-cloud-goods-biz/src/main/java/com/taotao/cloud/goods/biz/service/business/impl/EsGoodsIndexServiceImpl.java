package com.taotao.cloud.goods.biz.service.business.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.extra.pinyin.PinyinUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.taotao.cloud.cache.redis.repository.RedisRepository;
import com.taotao.cloud.common.enums.CachePrefix;
import com.taotao.cloud.common.enums.PromotionTypeEnum;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.goods.api.enums.GoodsAuthEnum;
import com.taotao.cloud.goods.api.enums.GoodsStatusEnum;
import com.taotao.cloud.goods.api.enums.GoodsWordsTypeEnum;
import com.taotao.cloud.goods.api.model.dto.GoodsParamsDTO;
import com.taotao.cloud.goods.api.model.query.EsGoodsSearchQuery;
import com.taotao.cloud.goods.biz.elasticsearch.BaseElasticsearchService;
import com.taotao.cloud.goods.biz.elasticsearch.ElasticsearchProperties;
import com.taotao.cloud.goods.biz.elasticsearch.EsGoodsAttribute;
import com.taotao.cloud.goods.biz.elasticsearch.EsGoodsIndex;
import com.taotao.cloud.goods.biz.elasticsearch.EsGoodsIndexRepository;
import com.taotao.cloud.goods.biz.elasticsearch.EsSuffix;
import com.taotao.cloud.goods.biz.model.entity.Brand;
import com.taotao.cloud.goods.biz.model.entity.Category;
import com.taotao.cloud.goods.biz.model.entity.Goods;
import com.taotao.cloud.goods.biz.model.entity.GoodsSku;
import com.taotao.cloud.goods.biz.model.entity.GoodsWords;
import com.taotao.cloud.goods.biz.model.entity.StoreGoodsLabel;
import com.taotao.cloud.goods.biz.service.business.IBrandService;
import com.taotao.cloud.goods.biz.service.business.ICategoryService;
import com.taotao.cloud.goods.biz.service.business.IEsGoodsIndexService;
import com.taotao.cloud.goods.biz.service.business.IEsGoodsSearchService;
import com.taotao.cloud.goods.biz.service.business.IGoodsService;
import com.taotao.cloud.goods.biz.service.business.IGoodsSkuService;
import com.taotao.cloud.goods.biz.service.business.IGoodsWordsService;
import com.taotao.cloud.goods.biz.service.business.IStoreGoodsLabelService;
import com.taotao.cloud.mq.stream.framework.rocketmq.RocketmqSendCallbackBuilder;
import com.taotao.cloud.mq.stream.framework.rocketmq.tags.GoodsTagsEnum;
import com.taotao.cloud.mq.stream.properties.RocketmqCustomProperties;
import com.taotao.cloud.promotion.api.enums.PromotionsStatusEnum;
import com.taotao.cloud.promotion.api.feign.IFeignPromotionApi;
import com.taotao.cloud.promotion.api.model.vo.BasePromotionsVO;
import com.taotao.cloud.promotion.api.model.vo.PromotionGoodsVO;
import com.taotao.cloud.promotion.api.tools.PromotionTools;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.assertj.core.util.IterableUtil;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.indices.AnalyzeRequest;
import org.elasticsearch.client.indices.AnalyzeResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.client.erhlc.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 商品索引业务层实现
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:02:27
 */
@Service
public class EsGoodsIndexServiceImpl extends BaseElasticsearchService implements
	IEsGoodsIndexService {

	private static final String IGNORE_FIELD = "serialVersionUID,promotionMap,id,goodsId";

	private final Map<String, Field> fieldMap = ReflectUtil.getFieldMap(EsGoodsIndex.class);

	@Autowired
	private ElasticsearchProperties elasticsearchProperties;
	@Autowired
	private EsGoodsIndexRepository esGoodsIndexRepository;
	@Autowired
	private IEsGoodsSearchService goodsSearchService;
	@Autowired
	private IGoodsWordsService goodsWordsService;
	@Autowired
	private IGoodsSkuService goodsSkuService;
	@Autowired
	private IGoodsService goodsService;
	@Autowired
	private IBrandService brandService;
	@Autowired
	private ICategoryService categoryService;
	@Autowired
	private IStoreGoodsLabelService storeGoodsLabelService;
	@Autowired
	private RedisRepository redisRepository;
	@Autowired
	private RocketMQTemplate rocketMQTemplate;
	@Autowired
	private RocketmqCustomProperties rocketmqCustomProperties;

	@Autowired
	@Qualifier("elasticsearchRestTemplate")
	private ElasticsearchRestTemplate restTemplate;

	@Autowired
	private IFeignPromotionApi feignPromotionApi;

	@Override
	public void init() {
		//获取索引任务标识
		Boolean flag = (Boolean) redisRepository.get(CachePrefix.INIT_INDEX_FLAG.getPrefix());
		//为空则默认写入没有任务
		if (flag == null) {
			redisRepository.set(CachePrefix.INIT_INDEX_FLAG.getPrefix(), false);
		}
		//有正在初始化的任务，则提示异常
		if (Boolean.TRUE.equals(flag)) {
			throw new BusinessException(ResultEnum.INDEX_BUILDING);
		}

		//初始化标识
		redisRepository.set(CachePrefix.INIT_INDEX_PROCESS.getPrefix(), null);
		redisRepository.set(CachePrefix.INIT_INDEX_FLAG.getPrefix(), true);

		ThreadUtil.execAsync(() -> {
			try {
				List<EsGoodsIndex> esGoodsIndices = new ArrayList<>();

				LambdaQueryWrapper<Goods> goodsQueryWrapper = new LambdaQueryWrapper<>();
				goodsQueryWrapper.eq(Goods::getIsAuth, GoodsAuthEnum.PASS.name());
				goodsQueryWrapper.eq(Goods::getMarketEnable, GoodsStatusEnum.UPPER.name());
				goodsQueryWrapper.eq(Goods::getDelFlag, false);

				for (Goods goods : goodsService.list(goodsQueryWrapper)) {
					LambdaQueryWrapper<GoodsSku> skuQueryWrapper = new LambdaQueryWrapper<>();
					skuQueryWrapper.eq(GoodsSku::getGoodsId, goods.getId());
					skuQueryWrapper.eq(GoodsSku::getIsAuth, GoodsAuthEnum.PASS.name());
					skuQueryWrapper.eq(GoodsSku::getMarketEnable, GoodsStatusEnum.UPPER.name());
					skuQueryWrapper.eq(GoodsSku::getDelFlag, false);

					List<GoodsSku> goodsSkuList = goodsSkuService.list(skuQueryWrapper);
					int skuSource = 100;
					for (GoodsSku goodsSku : goodsSkuList) {
						EsGoodsIndex esGoodsIndex = wrapperEsGoodsIndex(goodsSku, goods);
						esGoodsIndex.setSkuSource(skuSource--);
						esGoodsIndices.add(esGoodsIndex);
						//库存锁是在redis做的，所以生成索引，同时更新一下redis中的库存数量
						redisRepository.set(IGoodsSkuService.getStockCacheKey(goodsSku.getId()),
							goodsSku.getQuantity());
					}
				}
				//初始化商品索引
				this.initIndex(esGoodsIndices);
			} catch (Exception e) {
				LogUtils.error("商品索引生成异常：", e);
				//如果出现异常，则将进行中的任务标识取消掉，打印日志
				redisRepository.set(CachePrefix.INIT_INDEX_PROCESS.getPrefix(), null);
				redisRepository.set(CachePrefix.INIT_INDEX_FLAG.getPrefix(), false);
			}
		});
	}

	@Override
	public Map<String, Integer> getProgress() {
		Map<String, Integer> map = (Map<String, Integer>) redisRepository.get(
			CachePrefix.INIT_INDEX_PROCESS.getPrefix());
		if (map == null) {
			return Collections.emptyMap();
		}
		Boolean flag = (Boolean) redisRepository.get(CachePrefix.INIT_INDEX_FLAG.getPrefix());
		map.put("flag", Boolean.TRUE.equals(flag) ? 1 : 0);
		return map;
	}

	@Override
	public Boolean addIndex(EsGoodsIndex goods) {
		try {
			//分词器分词
			AnalyzeRequest analyzeRequest = AnalyzeRequest.withIndexAnalyzer(getIndexName(),
				"ik_max_word", goods.getGoodsName());
			AnalyzeResponse analyze = client.indices()
				.analyze(analyzeRequest, RequestOptions.DEFAULT);
			List<AnalyzeResponse.AnalyzeToken> tokens = analyze.getTokens();

			if (goods.getAttrList() != null && !goods.getAttrList().isEmpty()) {
				//保存分词
				for (EsGoodsAttribute esGoodsAttribute : goods.getAttrList()) {
					wordsToDb(esGoodsAttribute.getValue());
				}
			}
			//分析词条
			for (AnalyzeResponse.AnalyzeToken token : tokens) {
				//保存词条进入数据库
				wordsToDb(token.getTerm());
			}
			//生成索引
			esGoodsIndexRepository.save(goods);
		} catch (Exception e) {
			LogUtils.error("为商品[" + goods.getGoodsName() + "]生成索引异常", e);
		}
		return true;
	}

	@Override
	public Boolean updateIndex(EsGoodsIndex goods) {
		esGoodsIndexRepository.save(goods);
		return true;
	}

	/**
	 * 更新商品索引的的部分属性（只填写更新的字段，不需要更新的字段不要填写）
	 *
	 * @param id    商品索引id
	 * @param goods 更新后的购买数量
	 */
	@Override
	public Boolean updateIndex(Long id, EsGoodsIndex goods) {
		EsGoodsIndex goodsIndex = this.findById(id);
		// 通过反射获取全部字段，在根据参数字段是否为空，设置要更新的字段
		for (Map.Entry<String, Field> entry : fieldMap.entrySet()) {
			Object fieldValue = ReflectUtil.getFieldValue(goods, entry.getValue());
			if (fieldValue != null && !IGNORE_FIELD.contains(entry.getKey())) {
				ReflectUtil.setFieldValue(goodsIndex, entry.getValue(), fieldValue);
			}
		}
		esGoodsIndexRepository.save(goodsIndex);
		return true;
	}

	/**
	 * 更新商品索引的的部分属性（只填写更新的字段，不需要更新的字段不要填写）
	 *
	 * @param queryFields  查询字段
	 * @param updateFields 更新字段
	 */
	@Override
	public Boolean updateIndex(Map<String, Object> queryFields, Map<String, Object> updateFields) {
		UpdateByQueryRequest update = new UpdateByQueryRequest(getIndexName());
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
		for (Map.Entry<String, Object> entry : queryFields.entrySet()) {
			TermQueryBuilder termQueryBuilder = new TermQueryBuilder(entry.getKey(),
				entry.getValue());
			queryBuilder.filter(termQueryBuilder);
		}
		update.setQuery(queryBuilder);
		StringBuilder script = new StringBuilder();
		for (Map.Entry<String, Object> entry : updateFields.entrySet()) {
			script.append("ctx._source.").append(entry.getKey()).append("=").append("'")
				.append(entry.getValue()).append("'").append(";");
		}
		update.setScript(new Script(script.toString()));
		client.updateByQueryAsync(update, RequestOptions.DEFAULT, this.actionListener());
		return true;
	}

	/**
	 * 批量商品索引的的属性（ID 必填, 其他字段只填写更新的字段，不需要更新的字段不要填写。）
	 *
	 * @param goodsIndices 商品索引列表
	 */
	@Override
	public Boolean updateBulkIndex(List<EsGoodsIndex> goodsIndices) {
		try {
			//索引名称拼接
			String indexName = getIndexName();

			BulkRequest request = new BulkRequest();

			for (EsGoodsIndex goodsIndex : goodsIndices) {
				UpdateRequest updateRequest = new UpdateRequest(indexName,
					String.valueOf(goodsIndex.getId()));

				JSONObject jsonObject = JSONUtil.parseObj(goodsIndex);
				jsonObject.set("releaseTime", goodsIndex.getReleaseTime());
				updateRequest.doc(jsonObject);
				request.add(updateRequest);
			}
			client.bulk(request, RequestOptions.DEFAULT);
			throw new IOException();
		} catch (IOException e) {
			LogUtils.error("批量更新商品索引异常", e);
		}
		return true;
	}

	@Override
	public Boolean deleteIndex(EsGoodsIndex goods) {
		if (ObjectUtil.isEmpty(goods)) {
			//如果对象为空，则删除全量
			esGoodsIndexRepository.deleteAll();
		}
		esGoodsIndexRepository.delete(goods);
		return true;
	}

	@Override
	public Boolean deleteIndexById(Long id) {
		esGoodsIndexRepository.deleteById(id);
		return true;
	}

	@Override
	public Boolean deleteIndexByIds(List<Long> ids) {
		NativeQueryBuilder queryBuilder = new NativeQueryBuilder();
		queryBuilder.withQuery(QueryBuilders.termsQuery("id", ids.toArray()).toQuery());

		this.restTemplate.delete(queryBuilder.build(), EsGoodsIndex.class);
		return true;
	}

	@Override
	public Boolean initIndex(List<EsGoodsIndex> goodsIndexList) {
		if (goodsIndexList == null || goodsIndexList.isEmpty()) {
			//初始化标识
			redisRepository.set(CachePrefix.INIT_INDEX_PROCESS.getPrefix(), null);
			redisRepository.set(CachePrefix.INIT_INDEX_FLAG.getPrefix(), false);
			return true;
		}
		//索引名称拼接
		String indexName = this.getIndexName();

		//索引初始化，因为mapping结构问题：
		//但是如果索引已经自动生成过，这里就不会创建索引，设置mapping，所以这里决定在初始化索引的同时，将已有索引删除，重新创建

		//如果索引存在，则删除，重新生成。 这里应该有更优解。
		if (this.indexExist(indexName)) {
			deleteIndexRequest(indexName);
		}

		//如果索引不存在，则创建索引
		createIndexRequest(indexName);
		Map<String, Integer> resultMap = new HashMap<>(16);
		final String KEY_SUCCESS = "success";
		final String KEY_FAIL = "fail";
		final String KEY_PROCESSED = "processed";
		resultMap.put("total", goodsIndexList.size());
		resultMap.put(KEY_SUCCESS, 0);
		resultMap.put(KEY_FAIL, 0);
		resultMap.put(KEY_PROCESSED, 0);
		redisRepository.set(CachePrefix.INIT_INDEX_PROCESS.getPrefix(), resultMap);
		if (!goodsIndexList.isEmpty()) {
			esGoodsIndexRepository.deleteAll();
			for (EsGoodsIndex goodsIndex : goodsIndexList) {
				try {
					addIndex(goodsIndex);
					resultMap.put(KEY_SUCCESS, resultMap.get(KEY_SUCCESS) + 1);
				} catch (Exception e) {
					LogUtils.error("商品{}生成索引错误！", goodsIndex);
					resultMap.put(KEY_FAIL, resultMap.get(KEY_FAIL) + 1);
				}
				resultMap.put(KEY_PROCESSED, resultMap.get(KEY_PROCESSED) + 1);
				redisRepository.set(CachePrefix.INIT_INDEX_PROCESS.getPrefix(), resultMap);
			}
		}
		redisRepository.set(CachePrefix.INIT_INDEX_PROCESS.getPrefix(), resultMap);
		redisRepository.set(CachePrefix.INIT_INDEX_FLAG.getPrefix(), false);
		return true;
	}

	@Override
	public UpdateRequest updateEsGoodsIndexPromotions(Long id, BasePromotionsVO promotion,
													  String key) {
		EsGoodsIndex goodsIndex = findById(id);
		if (goodsIndex != null) {
			//更新索引
			return this.updateGoodsIndexPromotion(goodsIndex, key, promotion);
		} else {
			LogUtils.error("更新索引商品促销信息失败！skuId 为 {} 的索引不存在！", id);
			return null;
		}
	}

	@Override
	public Boolean updateEsGoodsIndexPromotions(List<Long> ids, BasePromotionsVO promotion,
												String key) {
		BulkRequest bulkRequest = new BulkRequest();
		LogUtils.info("更新商品索引的促销信息----------");
		LogUtils.info("商品ids: {}", ids);
		LogUtils.info("活动: {}", promotion);
		for (Long id : ids) {
			UpdateRequest updateRequest = this.updateEsGoodsIndexPromotions(id, promotion, key);
			if (updateRequest != null) {
				bulkRequest.add(updateRequest);
			}
		}
		this.executeBulkUpdateRequest(bulkRequest);
		return true;
	}

	@Override
	public Boolean updateEsGoodsIndexByList(List<PromotionGoodsVO> promotionGoodsList,
											BasePromotionsVO promotion, String key) {
		BulkRequest bulkRequest = new BulkRequest();
		LogUtils.info("修改商品活动索引");
		LogUtils.info("促销商品信息: {}", promotionGoodsList);
		LogUtils.info("活动关键字: {}", key);
		LogUtils.info("活动: {}", promotion);
		if (promotionGoodsList != null) {
			//循环更新 促销商品索引
			for (PromotionGoodsVO promotionGoods : promotionGoodsList) {
				promotion.setStartTime(promotionGoods.getStartTime());
				promotion.setEndTime(promotionGoods.getEndTime());
				UpdateRequest updateRequest = this.updateEsGoodsIndexPromotions(
					promotionGoods.getSkuId(), promotion, key);
				if (updateRequest != null) {
					bulkRequest.add(updateRequest);
				}
			}
		}
		this.executeBulkUpdateRequest(bulkRequest);
		return true;
	}

	@Override
	public Boolean updateEsGoodsIndexAllByList(BasePromotionsVO promotion, String key) {
		List<EsGoodsIndex> goodsIndices = new ArrayList<>();
		//如果storeId不为空，则表示是店铺活动
		if (promotion.getStoreId() != null && !promotion.getStoreId()
			.equals(PromotionTools.PLATFORM_ID)) {
			EsGoodsSearchQuery searchDTO = new EsGoodsSearchQuery();
			searchDTO.setStoreId(promotion.getStoreId());
			//查询出店铺商品
			SearchPage<EsGoodsIndex> esGoodsIndices = goodsSearchService.searchGoods(searchDTO
			);
			for (SearchHit<EsGoodsIndex> searchHit : esGoodsIndices.getContent()) {
				goodsIndices.add(searchHit.getContent());
			}
		} else {
			//否则是平台活动
			Iterable<EsGoodsIndex> all = esGoodsIndexRepository.findAll();
			//查询出全部商品
			goodsIndices = new ArrayList<>(IterableUtil.toCollection(all));
		}
		List<Long> skuIds = goodsIndices.stream().map(EsGoodsIndex::getId)
			.collect(Collectors.toList());
		this.updateEsGoodsIndexPromotions(skuIds, promotion, key);
		return true;
	}

	@Override
	public Boolean deleteEsGoodsPromotionIndexByList(List<Long> skuIds,
													 PromotionTypeEnum promotionType) {
		//批量删除活动索引
		for (Long skuId : skuIds) {
			EsGoodsIndex goodsIndex = findById(skuId);
			//商品索引不为空
			if (goodsIndex != null) {
				Map<String, Object> promotionMap = goodsIndex.getPromotionMap();
				if (promotionMap != null && !promotionMap.isEmpty()) {
					//如果存在同类型促销活动删除
					List<String> collect = promotionMap.keySet().stream()
						.filter(i -> i.contains(promotionType.name())).toList();
					collect.forEach(promotionMap::remove);
					goodsIndex.setPromotionMapJson(JSONUtil.toJsonStr(promotionMap));
					updateIndex(goodsIndex);
				}
			} else {
				LogUtils.error("更新索引商品促销信息失败！skuId 为 【{}】的索引不存在！", skuId);
			}
		}
		return true;
	}

	@Override
	public Boolean deleteEsGoodsPromotionByPromotionKey(List<Long> skuIds, String promotionsKey) {
		BulkRequest bulkRequest = new BulkRequest();
		LogUtils.info("删除商品活动索引");
		LogUtils.info("商品skuIds: {}", skuIds);
		LogUtils.info("活动Key: {}", promotionsKey);
		if (skuIds == null || skuIds.isEmpty()) {
			return true;
		}
		for (Long skuId : skuIds) {
			EsGoodsIndex goodsIndex = findById(skuId);
			//商品索引不为空
			if (goodsIndex != null) {
				UpdateRequest updateRequest = this.removePromotionByPromotionKey(goodsIndex,
					promotionsKey);
				if (updateRequest != null) {
					bulkRequest.add(updateRequest);
				}
			} else {
				LogUtils.error("更新索引商品促销信息失败！skuId 为 【{}】的索引不存在！", skuId);
			}
		}
		this.executeBulkUpdateRequest(bulkRequest);
		return true;
	}

	/**
	 * 删除索引中指定的促销活动id的促销活动
	 *
	 * @param promotionsKey 促销活动Key
	 */
	@Override
	public Boolean deleteEsGoodsPromotionByPromotionKey(String promotionsKey) {
		BulkRequest bulkRequest = new BulkRequest();
		for (EsGoodsIndex goodsIndex : this.esGoodsIndexRepository.findAll()) {
			UpdateRequest updateRequest = this.removePromotionByPromotionKey(goodsIndex,
				promotionsKey);
			if (updateRequest != null) {
				bulkRequest.add(updateRequest);
			}
		}
		this.executeBulkUpdateRequest(bulkRequest);
		return true;
	}

	/**
	 * 从索引中删除指定促销活动id的促销活动
	 *
	 * @param goodsIndex    索引
	 * @param promotionsKey 促销活动key
	 */
	private UpdateRequest removePromotionByPromotionKey(EsGoodsIndex goodsIndex,
														String promotionsKey) {
		Map<String, Object> promotionMap = goodsIndex.getPromotionMap();
		if (promotionMap != null && !promotionMap.isEmpty()) {
			//如果存在同促销ID的活动删除
			List<String> collect = promotionMap.keySet().stream()
				.filter(i -> i.equals(promotionsKey)).toList();
			collect.forEach(promotionMap::remove);
			goodsIndex.setPromotionMapJson(JSONUtil.toJsonStr(promotionMap));
			return this.getGoodsIndexPromotionUpdateRequest(goodsIndex.getId(), promotionMap);
		}
		return null;
	}

	/**
	 * 清除所有商品索引的无效促销活动
	 */
	@Override
	public Boolean cleanInvalidPromotion() {
		Iterable<EsGoodsIndex> all = esGoodsIndexRepository.findAll();
		for (EsGoodsIndex goodsIndex : all) {
			Map<String, Object> promotionMap = goodsIndex.getPromotionMap();
			//获取商品索引
			if (promotionMap != null && !promotionMap.isEmpty()) {
				//促销不为空则进行清洗
				promotionMap.entrySet().removeIf(i -> {
					JSONObject promotionJson = JSONUtil.parseObj(i.getValue());
					BasePromotionsVO promotion = promotionJson.toBean(BasePromotionsVO.class);
					// todo 此处时间条件判断需要修改
					return promotion.getEndTime() != null
						&& promotion.getEndTime().toEpochSecond(ZoneOffset.of("+8"))
						< DateUtil.date().getTime();
				});
			}
		}
		esGoodsIndexRepository.saveAll(all);
		return true;
	}

	@Override
	public EsGoodsIndex findById(Long id) {
		Optional<EsGoodsIndex> goodsIndex = esGoodsIndexRepository.findById(id);
		if (goodsIndex.isEmpty()) {
			LogUtils.error("商品skuId为" + id + "的es索引不存在！");
			return null;
		}
		return goodsIndex.get();
	}

	/**
	 * 根据id获取商品索引信息的促销信息
	 *
	 * @param id skuId
	 * @return 促销信息map
	 */
	@Override
	public Map<String, Object> getPromotionMap(Long id) {
		EsGoodsIndex goodsIndex = this.findById(id);

		//如果商品索引不为空，返回促销信息，否则返回空
		if (goodsIndex != null) {
			Map<String, Object> promotionMap = goodsIndex.getPromotionMap();
			if (promotionMap == null || promotionMap.isEmpty()) {
				return new HashMap<>(16);
			}
			return promotionMap;
		}
		return new HashMap<>();
	}

	/**
	 * 根据id获取商品索引信息的指定促销活动的id
	 *
	 * @param id                skuId
	 * @param promotionTypeEnum 促销活动类型
	 * @return 当前商品参与的促销活动id集合
	 */
	@Override
	public List<Long> getPromotionIdByPromotionType(Long id,
													PromotionTypeEnum promotionTypeEnum) {
		Map<String, Object> promotionMap = this.getPromotionMap(id);
		//如果没有促销信息，则返回新的
		if (promotionMap == null || promotionMap.isEmpty()) {
			return new ArrayList<>();
		}
		//对促销进行过滤
		List<String> keyCollect = promotionMap.keySet().stream()
			.filter(i -> i.contains(promotionTypeEnum.name())).toList();
		List<Long> promotionIds = new ArrayList<>();
		//写入促销id
		for (String key : keyCollect) {
			BasePromotionsVO promotion = (BasePromotionsVO) promotionMap.get(key);
			promotionIds.add(promotion.getId());
		}
		return promotionIds;
	}

	/**
	 * 获取重置的商品索引
	 *
	 * @param goodsSku       商品sku信息
	 * @param goodsParamDTOS 商品参数
	 * @return 商品索引
	 */
	@Override
	public EsGoodsIndex getResetEsGoodsIndex(GoodsSku goodsSku,
											 List<GoodsParamsDTO> goodsParamDTOS) {
		EsGoodsIndex index = new EsGoodsIndex(goodsSku, goodsParamDTOS);

		//获取活动信息
		Map<String, Object> goodsCurrentPromotionMap = feignPromotionApi.getGoodsSkuPromotionMap(
			index.getStoreId(), index.getId());

		//写入促销信息
		index.setPromotionMapJson(JSONUtil.toJsonStr(goodsCurrentPromotionMap));

		//发送mq消息
		String destination =
			rocketmqCustomProperties.getGoodsTopic() + ":" + GoodsTagsEnum.RESET_GOODS_INDEX.name();
		rocketMQTemplate.asyncSend(destination,
			JSONUtil.toJsonStr(Collections.singletonList(index)),
			RocketmqSendCallbackBuilder.commonCallback());
		return index;
	}

	/**
	 * 修改商品活动索引
	 *
	 * @param goodsIndex 商品索引
	 * @param key        关键字
	 * @param promotion  活动
	 */
	private UpdateRequest updateGoodsIndexPromotion(EsGoodsIndex goodsIndex, String key,
													BasePromotionsVO promotion) {
		Map<String, Object> promotionMap;
		//数据非空处理，如果空给一个新的信息
		if (goodsIndex.getPromotionMap() == null || goodsIndex.getPromotionMap().isEmpty()) {
			promotionMap = new HashMap<>(1);
		} else {
			promotionMap = goodsIndex.getPromotionMap();
		}

		LogUtils.info("ES修改商品活动索引-活动信息:{}", promotion);
		LogUtils.info("ES修改商品活动索引-活动信息状态:{}", promotion.getPromotionStatus());
		LogUtils.info("ES修改商品活动索引-原商品索引信息:{}", goodsIndex);
		LogUtils.info("ES修改商品活动索引-原商品索引活动信息:{}", promotionMap);
		//如果活动已结束
		if (promotion.getPromotionStatus().equals(PromotionsStatusEnum.END.name())
			|| promotion.getPromotionStatus().equals(PromotionsStatusEnum.CLOSE.name())) {//如果存在活动
			//删除活动
			promotionMap.remove(key);
		} else {
			promotionMap.put(key, promotion);
		}
		LogUtils.info("ES修改商品活动索引-过滤后商品索引活动信息:{}", promotionMap);
		return this.getGoodsIndexPromotionUpdateRequest(goodsIndex.getId(), promotionMap);
	}

	/**
	 * 以更新部分字段的方式更新索引促销信息
	 *
	 * @param id           索引id
	 * @param promotionMap 促销信息
	 */
	private UpdateRequest getGoodsIndexPromotionUpdateRequest(Long id,
															  Map<String, Object> promotionMap) {
		UpdateRequest updateRequest = new UpdateRequest();
		updateRequest.index(getIndexName());
		updateRequest.id(String.valueOf(id));
		updateRequest.retryOnConflict(5);
		updateRequest.version(promotionMap.size());
		Map<String, Object> params = new HashMap<>();
		params.put("promotionMap", JSONUtil.toJsonStr(promotionMap));
		Script script = new Script(ScriptType.INLINE, "painless",
			"ctx._source.promotionMapJson=params.promotionMap;", params);
		updateRequest.script(script);
		return updateRequest;
	}

	/**
	 * 执行批量更新商品索引
	 *
	 * @param bulkRequest 批量请求
	 */
	private void executeBulkUpdateRequest(BulkRequest bulkRequest) {
		if (bulkRequest.requests().isEmpty()) {
			return;
		}
		try {
			BulkResponse responses = this.client.bulk(bulkRequest, RequestOptions.DEFAULT);
			if (responses.hasFailures()) {
				LogUtils.info("批量更新商品索引的促销信息中出现部分异常：{}",
					responses.buildFailureMessage());
			} else {
				LogUtils.info("批量更新商品索引的促销信息结果：{}", responses.status());
			}
			throw new IOException();
		} catch (IOException e) {
			LogUtils.error("批量更新商品索引的促销信息出现异常！", e);
		}
	}

	/**
	 * 移除需要移除的促销活动
	 *
	 * @param currentKey     当前操作的促销活动key
	 * @param promotionMap   促销活动
	 * @param needRemoveKeys 需移除的促销活动
	 */
	private void removePromotionKey(String currentKey, Map<String, Object> promotionMap,
									String... needRemoveKeys) {
		//判定是否需要移除
		if (CharSequenceUtil.containsAny(currentKey, needRemoveKeys)) {
			List<String> removeKeys = new ArrayList<>();
			//促销循环
			for (String entry : promotionMap.keySet()) {
				//需要移除则进行移除处理
				for (String needRemoveKey : needRemoveKeys) {
					if (entry.contains(needRemoveKey) && currentKey.contains(needRemoveKey)) {
						removeKeys.add(entry);
						break;
					}
				}
			}
			//移除促销信息
			removeKeys.forEach(promotionMap.keySet()::remove);
		}
	}

	/**
	 * 将商品关键字入库
	 *
	 * @param words 商品关键字
	 */
	private void wordsToDb(String words) {
		if (CharSequenceUtil.isEmpty(words)) {
			return;
		}
		try {
			//是否有重复
			GoodsWords entity = goodsWordsService.getOne(
				new LambdaQueryWrapper<GoodsWords>().eq(GoodsWords::getWords, words));
			if (entity == null) {
				GoodsWords goodsWords = new GoodsWords();
				goodsWords.setWords(words);
				goodsWords.setWholeSpell(PinyinUtil.getPinyin(words, ""));
				goodsWords.setAbbreviate(PinyinUtil.getFirstLetter(words, ""));
				goodsWords.setType(GoodsWordsTypeEnum.SYSTEM.name());
				goodsWords.setSort(0);
				goodsWordsService.save(goodsWords);
			}
		} catch (MyBatisSystemException me) {
			LogUtils.error(words + "关键字已存在！");
		} catch (Exception e) {
			LogUtils.error("关键字入库异常！", e);
		}
	}

	private String getIndexName() {
		//索引名称拼接
		return elasticsearchProperties.getIndexPrefix() + "_" + EsSuffix.GOODS_INDEX_NAME;
	}

	private EsGoodsIndex wrapperEsGoodsIndex(GoodsSku goodsSku, Goods goods) {
		EsGoodsIndex index = new EsGoodsIndex(goodsSku);

		//商品参数索引
		if (goods.getParams() != null && !goods.getParams().isEmpty()) {
			List<GoodsParamsDTO> goodsParamDTOS = JSONUtil.toList(goods.getParams(),
				GoodsParamsDTO.class);
			index = new EsGoodsIndex(goodsSku, goodsParamDTOS);
		}
		//商品分类索引
		if (goods.getCategoryPath() != null) {
			List<String> ids = Arrays.asList(goods.getCategoryPath().split(","));
			List<Category> categories = categoryService.listByIdsOrderByLevel(
				ids.stream().map(Long::valueOf).toList());
			if (!categories.isEmpty()) {
				index.setCategoryNamePath(
					ArrayUtil.join(categories.stream().map(Category::getName).toArray(), ","));
			}
		}
		//商品品牌索引
		Brand brand = brandService.getById(goods.getBrandId());
		if (brand != null) {
			index.setBrandName(brand.getName());
			index.setBrandUrl(brand.getLogo());
		}
		//店铺分类索引
		if (goods.getStoreCategoryPath() != null && CharSequenceUtil.isNotEmpty(
			goods.getStoreCategoryPath())) {
			List<String> ids = Arrays.asList(goods.getStoreCategoryPath().split(","));
			List<StoreGoodsLabel> storeGoodsLabels = storeGoodsLabelService.listByStoreIds(
				ids.stream().map(Long::valueOf).toList());
			if (!storeGoodsLabels.isEmpty()) {
				index.setStoreCategoryNamePath(ArrayUtil.join(
					storeGoodsLabels.stream().map(StoreGoodsLabel::getLabelName).toArray(), ","));
			}
		}

		//促销索引
		Map<String, Object> goodsCurrentPromotionMap = feignPromotionApi.getGoodsSkuPromotionMap(
			index.getStoreId(), index.getId());
		index.setPromotionMapJson(JSONUtil.toJsonStr(goodsCurrentPromotionMap));
		return index;
	}

	private ActionListener<BulkByScrollResponse> actionListener() {
		return new ActionListener<BulkByScrollResponse>() {
			@Override
			public void onResponse(BulkByScrollResponse bulkByScrollResponse) {
				LogUtils.info("UpdateByQueryResponse: {}", bulkByScrollResponse);
			}

			@Override
			public void onFailure(Exception e) {
				LogUtils.error("UpdateByQueryRequestFailure: ", e);
			}
		};
	}
}
