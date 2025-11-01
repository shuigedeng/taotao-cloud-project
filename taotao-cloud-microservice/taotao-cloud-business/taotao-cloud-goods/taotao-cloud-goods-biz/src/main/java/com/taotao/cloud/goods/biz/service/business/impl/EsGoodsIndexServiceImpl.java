/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://bLogUtilstaotaocloud.top/).
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

package com.taotao.cloud.goods.biz.service.business.impl;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.taotao.boot.cache.redis.repository.RedisRepository;
import com.taotao.boot.common.enums.PromotionTypeEnum;
import com.taotao.boot.common.utils.log.LogUtils;
import com.taotao.cloud.goods.api.enums.GoodsAuthEnum;
import com.taotao.cloud.goods.api.enums.GoodsSalesModeEnum;
import com.taotao.cloud.goods.api.enums.GoodsStatusEnum;
import com.taotao.cloud.goods.biz.model.dto.GoodsParamsDTO;
import com.taotao.cloud.goods.biz.model.dto.GoodsSkuDTO;
import com.taotao.cloud.goods.biz.elasticsearch.entity.EsGoodsIndex;
import com.taotao.cloud.goods.biz.elasticsearch.pojo.EsGoodsAttribute;
import com.taotao.cloud.goods.biz.elasticsearch.pojo.EsGoodsSearchDTO;
import com.taotao.cloud.goods.biz.elasticsearch.pojo.EsSuffix;
import com.taotao.cloud.goods.biz.elasticsearch.repository.EsGoodsIndexRepository;
import com.taotao.cloud.goods.biz.elasticsearch.service.BaseElasticsearchService;
import com.taotao.cloud.goods.biz.model.entity.CustomWords;
import com.taotao.cloud.goods.biz.model.entity.GoodsSku;
import com.taotao.cloud.goods.biz.service.business.*;
import com.taotao.cloud.stream.framework.rocketmq.RocketmqSendCallbackBuilder;
import com.taotao.cloud.stream.framework.rocketmq.tags.GoodsTagsEnum;
import com.taotao.cloud.stream.properties.RocketmqCustomProperties;
import com.taotao.cloud.promotion.api.enums.PromotionsScopeTypeEnum;
import com.taotao.cloud.promotion.api.enums.PromotionsStatusEnum;
import com.taotao.cloud.promotion.api.feign.IFeignPromotionApi;
import com.taotao.cloud.promotion.api.feign.IFeignPromotionGoodsApi;
import com.taotao.cloud.promotion.api.model.vo.BasePromotionsVO;
import com.taotao.cloud.promotion.api.model.vo.PromotionGoodsVO;
import com.taotao.cloud.promotion.api.tools.PromotionTools;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.retry.RetryException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.taotao.boot.common.enums.CachePrefixEnum.INIT_INDEX_FLAG;
import static com.taotao.boot.common.enums.CachePrefixEnum.INIT_INDEX_PROCESS;

/**
 * 商品索引业务层实现
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:02:27
 */
@Service
public class EsGoodsIndexServiceImpl extends BaseElasticsearchService implements IEsGoodsIndexService {

	private static final String IGNORE_FIELD = "serialVersionUID,promotionMap,id,goodsId";
	private static final String KEY_SUCCESS = "success";
	private static final String KEY_FAIL = "fail";
	private static final String KEY_PROCESSED = "processed";
	private final Map<String, Field> fieldMap = ReflectUtil.getFieldMap(EsGoodsIndex.class);

	@Autowired
	private EsGoodsIndexRepository goodsIndexRepository;
	@Autowired
	private IEsGoodsSearchService goodsSearchService;
	@Autowired
	private IFeignPromotionApi promotionApi;
	@Autowired
	private IFeignPromotionGoodsApi promotionGoodsApi;
	@Autowired
	private ICustomWordsService customWordsService;
	@Autowired
	private IGoodsSkuService goodsSkuService;
	@Autowired
	private IBrandService brandService;
	@Autowired
	private ICategoryService categoryService;
	@Autowired
	private IStoreGoodsLabelService storeGoodsLabelService;
	@Autowired
	private RedisRepository cache;
	@Autowired
	private RocketMQTemplate rocketMQTemplate;
	@Autowired
	private RocketmqCustomProperties rocketmqCustomProperties;
	@Autowired
	private ElasticsearchOperations restTemplate;

	/**
	 * 去除 重复元素
	 *
	 * @param list
	 * @return
	 */
	public static void removeDuplicate(List<String> list) {
		HashSet<String> h = new HashSet<>(list);
		list.clear();
		list.addAll(h);
	}

	@Override
	public void init() {
		// 获取索引任务标识
		boolean flag = (boolean) cache.get(INIT_INDEX_FLAG.getPrefix());
		// 为空则默认写入没有任务
		if (flag) {
			cache.set(INIT_INDEX_FLAG.getPrefix(), false);
		}
		// 有正在初始化的任务，则提示异常
		if (TRUE.equals(flag)) {
			throw new RuntimeException("索引正在生成");
		}

		// 初始化标识
		cache.set(INIT_INDEX_PROCESS.getPrefix(), null);
		cache.setExpire(INIT_INDEX_FLAG.getPrefix(), true, 10L, TimeUnit.MINUTES);

		ThreadUtil.execAsync(() -> {
			try {
				QueryWrapper<GoodsSkuDTO> skuQueryWrapper = new QueryWrapper<>();
				skuQueryWrapper.eq("gs.auth_flag", GoodsAuthEnum.PASS.name());
				skuQueryWrapper.eq("gs.market_enable", GoodsStatusEnum.UPPER.name());
				skuQueryWrapper.eq("gs.delete_flag", false);
				skuQueryWrapper.gt("gs.quantity", 0);

				Map<String, Long> resultMap = (Map<String, Long>) cache.get(INIT_INDEX_PROCESS.getPrefix());

				if (CollUtil.isEmpty(resultMap)) {
					QueryWrapper<GoodsSku> skuCountQueryWrapper = new QueryWrapper<>();
					skuCountQueryWrapper.eq("auth_flag", GoodsAuthEnum.PASS.name());
					skuCountQueryWrapper.eq("market_enable", GoodsStatusEnum.UPPER.name());
					skuCountQueryWrapper.eq("delete_flag", false);
					skuCountQueryWrapper.ge("quantity", 0);
					resultMap = new HashMap<>();
					resultMap.put(KEY_SUCCESS, 0L);
					resultMap.put(KEY_FAIL, 0L);
					resultMap.put(KEY_PROCESSED, 0L);
					resultMap.put("total", this.goodsSkuService.count(skuCountQueryWrapper));
					cache.set(INIT_INDEX_PROCESS.getPrefix(), resultMap);
				}

				for (int i = 1; ; i++) {
					List<EsGoodsIndex> esGoodsIndices = new ArrayList<>();
					Page<GoodsSkuDTO> skuPage = new Page<>(i, 2000);
					IPage<GoodsSkuDTO> skuIPage = goodsSkuService.getGoodsSkuDTOByPage(skuPage, skuQueryWrapper);

					if (skuIPage == null || CollUtil.isEmpty(skuIPage.getRecords())) {
						break;
					}
					List<GoodsSkuDTO> skus = skuIPage.getRecords();
					List<String> categories = skus.stream().map(GoodsSkuDTO::getCategoryPath).toList();
					List<String> skuIds = skus.stream().map(GoodsSkuDTO::getId).toList();
					List<PromotionGoodsVO> skuValidPromotions = promotionGoodsApi.findSkuValidPromotions(categories, skuIds);

					List<String> brandIds = new ArrayList<>();
					List<String> categoryPaths = new ArrayList<>();
					List<String> storeCategoryPaths = new ArrayList<>();

					for (GoodsSkuDTO goodsSkuDTO : skuIPage.getRecords()) {
						if (CharSequenceUtil.isNotEmpty(goodsSkuDTO.getBrandId())) {
							brandIds.add(goodsSkuDTO.getBrandId());
						}
						if (CharSequenceUtil.isNotEmpty(goodsSkuDTO.getStoreCategoryPath())) {
							storeCategoryPaths.addAll(Arrays.asList(goodsSkuDTO.getStoreCategoryPath().split(",")));
						}
						if (CharSequenceUtil.isNotEmpty((goodsSkuDTO.getCategoryPath()))) {
							categoryPaths.addAll(Arrays.asList(goodsSkuDTO.getCategoryPath().split(",")));
						}
					}

					List<Map<String, Object>> brandList = new ArrayList<>();
					if (CollUtil.isNotEmpty(brandIds)) {
						brandList = brandService.getBrandsMapsByCategory(CollUtil.distinct(brandIds), "id,name,logo");
					}
					List<Map<String, Object>> categoryList = new ArrayList<>();
					if (CollUtil.isNotEmpty(categoryPaths)) {
						categoryList = categoryService.listMapsByIdsOrderByLevel(CollUtil.distinct(categoryPaths), "id,name");
					}
					List<Map<String, Object>> storeCategoryList = new ArrayList<>();
					if (CollUtil.isNotEmpty(storeCategoryPaths)) {
						storeCategoryList = storeGoodsLabelService.listMapsByStoreIds(CollUtil.distinct(storeCategoryPaths), "id,label_name");
					}

					for (GoodsSkuDTO goodsSku : skuIPage.getRecords()) {
						int skuSource = 100;
						EsGoodsIndex esGoodsIndex = wrapperEsGoodsIndex(goodsSku, brandList, categoryList, storeCategoryList);
						long count = esGoodsIndices.stream().filter(j -> j.getGoodsId().equals(esGoodsIndex.getGoodsId())).count();
						if (count >= 1) {
							skuSource -= count;
						}
						esGoodsIndex.setSkuSource(skuSource);

						// 设置促销信息
						List<PromotionGoodsVO> promotionGoods = skuValidPromotions.stream()
							.filter(j ->
								(Objects.nonNull(j.getSkuId()) && j.getSkuId().equals(goodsSku.getId())) ||
									(j.getScopeType().equals(PromotionsScopeTypeEnum.ALL.name()) && j.getStoreId().equals("0")) ||
									(j.getScopeType().equals(PromotionsScopeTypeEnum.ALL.name()) && j.getStoreId().equals(esGoodsIndex.getStoreId())) ||
									(j.getScopeType().equals(PromotionsScopeTypeEnum.PORTION_GOODS_CATEGORY.name()) && j.getScopeId().contains(goodsSku.getCategoryPath())))
							.toList();
						if (CollUtil.isNotEmpty(promotionGoods)) {
							esGoodsIndex.setPromotionMapJson(JSONUtil.toJsonStr(promotionApi.wrapperPromotionMapList(promotionGoods)));
						}

						esGoodsIndices.add(esGoodsIndex);
						// 库存锁是在redis做的，所以生成索引，同时更新一下redis中的库存数量
						// SKU_STOCK.getPrefix() + id;
						cache.set("aaa", goodsSku.getQuantity());
					}

					// 批量插入索引，如果为第一次则删除原索引并创建新索引
					this.initIndex(esGoodsIndices, i == 1);
				}

				cache.set(INIT_INDEX_FLAG.getPrefix(), false);

				// 初始化商品索引
			} catch (Exception e) {
				LogUtils.error("商品索引生成异常：", e);
				// 如果出现异常，则将进行中的任务标识取消掉，打印日志
				cache.set(INIT_INDEX_PROCESS.getPrefix(), null);
				cache.set(INIT_INDEX_FLAG.getPrefix(), false);
			}
		});

	}

	@Override
	public Map<String, Long> getProgress() {
		Map<String, Long> map = (Map<String, Long>) cache.get(INIT_INDEX_PROCESS.getPrefix());
		if (map == null) {
			return Collections.emptyMap();
		}
		boolean flag = (boolean) cache.get(INIT_INDEX_FLAG.getPrefix());
		map.put("flag", TRUE.equals(flag) ? 1L : 0L);
		return map;
	}

	@Override
	public void initIndex() {
		// 索引名称拼接
		String indexName = this.getIndexName();

		// 索引初始化，因为mapping结构问题：
		// 但是如果索引已经自动生成过，这里就不会创建索引，设置mapping，所以这里决定在初始化索引的同时，将已有索引删除，重新创建
		boolean indexExist = this.indexExist(indexName);
		LogUtils.info("检测 {} 索引结构是否存在：{}", indexName, indexExist);
		if (!indexExist) {
			LogUtils.info("初始化索引结构 {}", indexName);
			// 如果索引不存在，则创建索引
			createIndexRequest(indexName);
		}
	}

	@Override
	public void addIndex(EsGoodsIndex goods) {
		try {
			// 分词器分词
			this.analyzeAndSaveWords(goods);
			// 生成索引
			goodsIndexRepository.save(goods);
		} catch (Exception e) {
			LogUtils.error("为商品[" + goods.getGoodsName() + "]生成索引异常", e);
		}
	}

	/**
	 * 添加商品索引
	 *
	 * @param goods 商品索引信息
	 */
	@Override
	public void addIndex(List<EsGoodsIndex> goods) {
		try {
			for (EsGoodsIndex esGoodsIndex : goods) {
				this.analyzeAndSaveWords(esGoodsIndex);
			}
			goodsIndexRepository.saveAll(goods);
		} catch (Exception e) {
			LogUtils.error("批量为商品生成索引异常", e);
		}
	}

	@Override
	public void updateIndex(EsGoodsIndex goods) {
		this.analyzeAndSaveWords(goods);
		goodsIndexRepository.save(goods);
	}

	/**
	 * 商品分词
	 *
	 * @param goods 商品信息
	 */
	private void analyzeAndSaveWords(EsGoodsIndex goods) {
		try {
			List<String> keywordsList = new ArrayList<>();
			// 根据商品参数分词
			if (goods.getAttrList() != null && !goods.getAttrList().isEmpty()) {
				// 保存分词
				for (EsGoodsAttribute esGoodsAttribute : goods.getAttrList()) {
					if (keywordsList.stream().noneMatch(i -> i.toLowerCase(Locale.ROOT).equals(esGoodsAttribute.getValue().toLowerCase(Locale.ROOT)))) {
						keywordsList.add(esGoodsAttribute.getValue());
					}
				}
			}
			// 根据商品名称生成分词
			keywordsList.add(goods.getGoodsName().substring(0, Math.min(goods.getGoodsName().length(), 10)));

			// 去除重复词
			removeDuplicate(keywordsList);
			// 入库自定义分词
			List<CustomWords> customWordsArrayList = new ArrayList<>();
			keywordsList.forEach(item -> customWordsArrayList.add(new CustomWords(item)));
			// 这里采用先批量删除再插入的方法，故意这么做。否则需要挨个匹配是否存在，性能消耗更大
			if (CollUtil.isNotEmpty(customWordsArrayList)) {
				customWordsService.deleteBathByName(keywordsList);
				customWordsService.insertBatchCustomWords(customWordsArrayList);
			}
		} catch (Exception e) {
			LogUtils.info(goods + "自定义分词错误", e);
		}
	}

	/**
	 * 更新商品索引的的部分属性（只填写更新的字段，不需要更新的字段不要填写）
	 *
	 * @param id    商品索引id
	 * @param goods 更新后的购买数量
	 */
	@Override
	public void updateIndex(String id, EsGoodsIndex goods) {
		EsGoodsIndex goodsIndex = this.findById(id);
		// 通过反射获取全部字段，在根据参数字段是否为空，设置要更新的字段
		for (Map.Entry<String, Field> entry : fieldMap.entrySet()) {
			Object fieldValue = ReflectUtil.getFieldValue(goods, entry.getValue());
			if (fieldValue != null && !IGNORE_FIELD.contains(entry.getKey())) {
				ReflectUtil.setFieldValue(goodsIndex, entry.getValue(), fieldValue);
			}
		}
		goodsIndexRepository.save(goodsIndex);
	}

	/**
	 * 更新商品索引的的部分属性（只填写更新的字段，不需要更新的字段不要填写）
	 *
	 * @param queryFields  查询字段
	 * @param updateFields 更新字段
	 */
	@Override
	public void updateIndex(Map<String, Object> queryFields, Map<String, Object> updateFields) {
		UpdateByQueryRequest update = new UpdateByQueryRequest(getIndexName());
		BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
		for (Map.Entry<String, Object> entry : queryFields.entrySet()) {
			TermQueryBuilder termQueryBuilder = new TermQueryBuilder(entry.getKey(), entry.getValue());
			queryBuilder.filter(termQueryBuilder);
		}
		update.setQuery(queryBuilder);
		StringBuilder script = new StringBuilder();
		for (Map.Entry<String, Object> entry : updateFields.entrySet()) {
			script.append("ctx._source.").append(entry.getKey()).append("=").append("'").append(entry.getValue()).append("'").append(";");
		}
		update.setScript(new Script(script.toString()));
		update.setConflicts("proceed");
		try {
			BulkByScrollResponse bulkByScrollResponse = client.updateByQuery(update, RequestOptions.DEFAULT);
			if (bulkByScrollResponse.getVersionConflicts() > 0) {
				throw new RetryException("更新商品索引失败，es内容版本冲突");
			}
		} catch (IOException e) {
			LogUtils.error("更新商品索引异常", e);
		}
	}

	/**
	 * 批量商品索引的的属性（ID 必填, 其他字段只填写更新的字段，不需要更新的字段不要填写。）
	 *
	 * @param goodsIndices 商品索引列表
	 */
	@Override
	public void updateBulkIndex(List<EsGoodsIndex> goodsIndices) {
		try {
			// 索引名称拼接
			String indexName = getIndexName();

			BulkRequest request = new BulkRequest();
			for (EsGoodsIndex goodsIndex : goodsIndices) {
				UpdateRequest updateRequest = new UpdateRequest(indexName, String.valueOf(goodsIndex.getId()));
				JSONObject jsonObject = JSONUtil.parseObj(goodsIndex);
				jsonObject.set("releaseTime", goodsIndex.getReleaseTime());
				updateRequest.doc(jsonObject);
				request.add(updateRequest);
			}
			client.bulk(request, RequestOptions.DEFAULT);
		} catch (IOException e) {
			LogUtils.error("批量更新商品索引异常", e);
		}
	}

	/**
	 * 删除索引
	 *
	 * @param queryFields 查询条件
	 */
	@Override
	public void deleteIndex(Map<String, Object> queryFields) {
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		for (Map.Entry<String, Object> entry : queryFields.entrySet()) {
			boolQueryBuilder.filter(QueryBuilders.termsQuery(entry.getKey(), entry.getValue().toString()));
		}

		DeleteByQueryRequest deleteByQueryRequest = new DeleteByQueryRequest();
		deleteByQueryRequest.setQuery(boolQueryBuilder);
		deleteByQueryRequest.indices(getIndexName());
		deleteByQueryRequest.setConflicts("proceed");
		try {
			BulkByScrollResponse bulkByScrollResponse = client.deleteByQuery(deleteByQueryRequest, RequestOptions.DEFAULT);
			if (bulkByScrollResponse.getVersionConflicts() > 0) {
				throw new RetryException("删除索引失败，es内容版本冲突");
			}
		} catch (IOException e) {
			LogUtils.error("删除索引异常", e);
		}
	}

	/**
	 * 删除索引
	 *
	 * @param id 商品索引信息
	 */
	@Override
	public void deleteIndexById(String id) {
		goodsIndexRepository.deleteById(id);
	}

	/**
	 * 删除索引
	 *
	 * @param ids 商品索引id集合
	 */
	@Override
	public void deleteIndexByIds(List<String> ids) {
		NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
		queryBuilder.withQuery(QueryBuilders.termsQuery("id", ids.toArray()));
		this.restTemplate.delete(queryBuilder.build(), EsGoodsIndex.class);
	}

	@Override
	public void initIndex(List<EsGoodsIndex> goodsIndexList, boolean regeneratorIndex) {
		if (goodsIndexList == null || goodsIndexList.isEmpty()) {
			// 初始化标识
			cache.set(INIT_INDEX_PROCESS.getPrefix(), null);
			cache.set(INIT_INDEX_FLAG.getPrefix(), false);
			return;
		}
		// 索引名称拼接
		String indexName = this.getIndexName();

		// 索引初始化，因为mapping结构问题：
		// 但是如果索引已经自动生成过，这里就不会创建索引，设置mapping，所以这里决定在初始化索引的同时，将已有索引删除，重新创建

		// 如果索引存在，则删除，重新生成。 这里应该有更优解。
		boolean indexExist = this.indexExist(indexName);
		if (regeneratorIndex || !indexExist) {
			if (indexExist) {
				this.deleteIndexRequest(indexName);
			}
			// 如果索引不存在，则创建索引
			this.createIndexRequest(indexName);
		}

		Map<String, Long> resultMap = (Map<String, Long>) cache.get(INIT_INDEX_PROCESS.getPrefix());
		if (!goodsIndexList.isEmpty()) {
			for (EsGoodsIndex goodsIndex : goodsIndexList) {
				try {
					LogUtils.info("生成商品索引：{}", goodsIndex);
					this.addIndex(goodsIndex);
					resultMap.put(KEY_SUCCESS, resultMap.get(KEY_SUCCESS) + 1);
				} catch (Exception e) {
					LogUtils.error("商品{}生成索引错误！", goodsIndex);
					resultMap.put(KEY_FAIL, resultMap.get(KEY_FAIL) + 1);
				}
				resultMap.put(KEY_PROCESSED, resultMap.get(KEY_PROCESSED) + 1);
				cache.set(INIT_INDEX_PROCESS.getPrefix(), resultMap);
			}
		}
		cache.set(INIT_INDEX_PROCESS.getPrefix(), resultMap);
	}

	@Override
	public UpdateRequest updateEsGoodsIndexPromotions(String id, BasePromotionsVO promotion, String key) {
		EsGoodsIndex goodsIndex = findById(id);
		if (goodsIndex != null) {
			// 批发商品不参与促销（除优惠券和满减）
			if (PromotionTools.isPromotionsTypeNeedsToChecked(key) && GoodsSalesModeEnum.WHOLESALE.name().equals(goodsIndex.getSalesModel())) {
				return null;
			}
			// 更新索引
			return this.updateGoodsIndexPromotion(goodsIndex, key, promotion);
		} else {
			LogUtils.error("更新索引商品促销信息失败！skuId 为 {} 的索引不存在！", id);
			return null;
		}
	}

	/**
	 * 更新商品索引的促销信息
	 *
	 * @param ids       skuId集合
	 * @param promotion 促销信息
	 * @param key       促销信息的key
	 */
	@Override
	public void updateEsGoodsIndexPromotions(List<String> ids, BasePromotionsVO promotion, String key) {
		BulkRequest bulkRequest = new BulkRequest();
		LogUtils.info("更新商品索引的促销信息----------");
		LogUtils.info("商品ids: {}", ids);
		LogUtils.info("活动: {}", promotion);
		LogUtils.info("key: {}", key);
		for (String id : ids) {
			UpdateRequest updateRequest = this.updateEsGoodsIndexPromotions(id, promotion, key);
			if (updateRequest != null) {
				bulkRequest.add(updateRequest);
			}
		}
		this.executeBulkUpdateRequest(bulkRequest);
	}


	@Override
	public void updateEsGoodsIndexByList(List<PromotionGoodsVO> promotionGoodsList, BasePromotionsVO promotion, String key) {
		BulkRequest bulkRequest = new BulkRequest();
		LogUtils.info("修改商品活动索引");
		LogUtils.info("促销商品信息: {}", promotionGoodsList);
		LogUtils.info("活动关键字: {}", key);
		LogUtils.info("活动: {}", promotion);
		if (promotionGoodsList != null) {
			// 循环更新 促销商品索引
			for (PromotionGoodsVO promotionGoods : promotionGoodsList) {
				promotion.setStartTime(promotionGoods.getStartTime());
				promotion.setEndTime(promotionGoods.getEndTime());
				UpdateRequest updateRequest = this.updateEsGoodsIndexPromotions(promotionGoods.getSkuId(), promotion, key);
				if (updateRequest != null) {
					bulkRequest.add(updateRequest);
				}
			}
		}
		this.executeBulkUpdateRequest(bulkRequest);
	}

	/**
	 * 更新全部商品索引的促销信息
	 *
	 * @param promotion 促销信息
	 * @param key       促销信息的key
	 */
	@Override
	public void updateEsGoodsIndexAllByList(BasePromotionsVO promotion, String key) {
		ThreadUtil.execAsync(() -> this.executeUpdateEsGoodsIndexAll(promotion, key));

	}

	private void executeUpdateEsGoodsIndexAll(BasePromotionsVO promotion, String key) {
		for (int i = 0; ; i++) {
			List<String> skuIds;
			PageVO pageVO = new PageVO();
			pageVO.setPageNumber(i);
			pageVO.setPageSize(1000);
			EsGoodsSearchDTO searchDTO = new EsGoodsSearchDTO();
			if (PromotionTools.isPromotionsTypeNeedsToChecked(key)) {
				searchDTO.setSalesModel(GoodsSalesModeEnum.RETAIL.name());
			}
			// 如果storeId不为空，则表示是店铺活动
			if (promotion.getStoreId() != null && !promotion.getStoreId().equals(PromotionTools.PLATFORM_ID)) {
				searchDTO.setStoreId(promotion.getStoreId());
			}

			// 查询出店铺商品
			SearchPage<EsGoodsIndex> esGoodsIndices = goodsSearchService.searchGoods(searchDTO, pageVO);

			skuIds = esGoodsIndices.isEmpty() ? new ArrayList<>() :
				esGoodsIndices.getContent().stream().map(SearchHit::getId).toList();
			if (skuIds.isEmpty()) {
				break;
			}
			this.deleteEsGoodsPromotionByPromotionKey(skuIds, key);
			this.updateEsGoodsIndexPromotions(skuIds, promotion, key);
		}
	}

	@Override
	public void deleteEsGoodsPromotionByPromotionKey(List<String> skuIds, String promotionsKey) {
		BulkRequest bulkRequest = new BulkRequest();
		LogUtils.info("删除商品活动索引");
		LogUtils.info("商品skuIds: {}", skuIds);
		LogUtils.info("活动Key: {}", promotionsKey);
		if (skuIds == null || skuIds.isEmpty()) {
			return;
		}
		for (String skuId : skuIds) {
			EsGoodsIndex goodsIndex = findById(skuId);
			// 商品索引不为空
			if (goodsIndex != null) {
				UpdateRequest updateRequest = this.removePromotionByPromotionKey(goodsIndex, promotionsKey);
				if (updateRequest != null) {
					bulkRequest.add(updateRequest);
				}
			} else {
				LogUtils.error("更新索引商品促销信息失败！skuId 为 【{}】的索引不存在！", skuId);
			}
		}
		this.executeBulkUpdateRequest(bulkRequest);
	}

	/**
	 * 删除索引中指定的促销活动id的促销活动
	 *
	 * @param promotionsKey 促销活动Key
	 */
	@Override
	public void deleteEsGoodsPromotionByPromotionKey(String promotionsKey) {
		ThreadUtil.execAsync(() -> {
			BulkRequest bulkRequest = new BulkRequest();
			for (EsGoodsIndex goodsIndex : this.goodsIndexRepository.findAll()) {
				UpdateRequest updateRequest = this.removePromotionByPromotionKey(goodsIndex, promotionsKey);
				if (updateRequest != null) {
					bulkRequest.add(updateRequest);
				}
			}
			this.executeBulkUpdateRequest(bulkRequest);
		});
	}

	/**
	 * 从索引中删除指定促销活动id的促销活动
	 *
	 * @param goodsIndex    索引
	 * @param promotionsKey 促销活动key
	 */
	private UpdateRequest removePromotionByPromotionKey(EsGoodsIndex goodsIndex, String promotionsKey) {
		Map<String, Object> promotionMap = goodsIndex.getOriginPromotionMap();
		if (promotionMap != null && !promotionMap.isEmpty()) {
			// 如果存在同促销ID的活动删除
			Map<String, Object> filterPromotionMap = promotionMap.entrySet().stream().filter(i -> !i.getKey().equals(promotionsKey)).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
			return this.getGoodsIndexPromotionUpdateRequest(goodsIndex.getId(), filterPromotionMap);
		}
		return null;
	}

	/**
	 * 清除所有商品索引的无效促销活动
	 */
	@Override
	public void cleanInvalidPromotion() {
		ThreadUtil.execAsync(this::executeCleanInvalidPromotions);
	}

	private void executeCleanInvalidPromotions() {
		for (int i = 1; ; i++) {
			org.springframework.data.domain.Page<EsGoodsIndex> all = goodsIndexRepository.findAll(PageRequest.of(i, 1000));
			if (all.isEmpty()) {
				break;
			}
			for (EsGoodsIndex goodsIndex : all.toList()) {
				Map<String, Object> promotionMap = goodsIndex.getOriginPromotionMap();
				// 获取商品索引
				if (promotionMap != null && !promotionMap.isEmpty()) {
					// 促销不为空则进行清洗
					promotionMap.entrySet().removeIf(j -> {
						JSONObject promotionJson = JSONUtil.parseObj(j.getValue());
						BasePromotionsVO promotion = promotionJson.toBean(BasePromotionsVO.class);
						return promotion.getEndTime() != null && promotion.getEndTime().getTime() < DateUtil.date().getTime();
					});
				}
			}
			goodsIndexRepository.saveAll(all);
		}
	}


	@Override
	public EsGoodsIndex findById(Long id) {
		Optional<EsGoodsIndex> goodsIndex = goodsIndexRepository.findById(id);
		if (!goodsIndex.isPresent()) {
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

		// 如果商品索引不为空，返回促销信息，否则返回空
		if (goodsIndex != null) {
			Map<String, Object> promotionMap = goodsIndex.getOriginPromotionMap();
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
	public List<String> getPromotionIdByPromotionType(Long id, PromotionTypeEnum promotionTypeEnum) {
		Map<String, Object> promotionMap = this.getPromotionMap(id);
		// 如果没有促销信息，则返回新的
		if (promotionMap == null || promotionMap.isEmpty()) {
			return new ArrayList<>();
		}
		// 对促销进行过滤
		List<String> keyCollect = promotionMap.keySet().stream().filter(i -> i.contains(promotionTypeEnum.name())).toList();
		List<String> promotionIds = new ArrayList<>();
		// 写入促销id
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
	public EsGoodsIndex getResetEsGoodsIndex(GoodsSku goodsSku, List<GoodsParamsDTO> goodsParamDTOS) {
		EsGoodsIndex index = new EsGoodsIndex(goodsSku, goodsParamDTOS);
		// 获取活动信息
		Map<String, Object> goodsCurrentPromotionMap = promotionApi.getGoodsSkuPromotionMap(index.getStoreId(), index.getId());
		// 写入促销信息
		index.setPromotionMapJson(JSONUtil.toJsonStr(goodsCurrentPromotionMap));

		// 发送mq消息
		String destination = rocketmqCustomProperties.getGoodsTopic() + ":" + GoodsTagsEnum.RESET_GOODS_INDEX.name();
		rocketMQTemplate.asyncSend(destination, JSONUtil.toJsonStr(Collections.singletonList(index)), RocketmqSendCallbackBuilder.commonCallback());
		return index;
	}

	/**
	 * 修改商品活动索引
	 *
	 * @param goodsIndex 商品索引
	 * @param key        关键字
	 * @param promotion  活动
	 */
	private UpdateRequest updateGoodsIndexPromotion(EsGoodsIndex goodsIndex, String key, BasePromotionsVO promotion) {
		Map<String, Object> promotionMap;
		// 数据非空处理，如果空给一个新的信息
		if (goodsIndex.getOriginPromotionMap() == null || goodsIndex.getOriginPromotionMap().isEmpty()) {
			promotionMap = new HashMap<>(1);
		} else {
			promotionMap = goodsIndex.getOriginPromotionMap();
		}
//        LogUtils.info("ES修改商品活动索引-原商品索引信息:{}", goodsIndex);
//        LogUtils.info("ES修改商品活动索引-原商品索引活动信息:{}", promotionMap);
		// 如果活动已结束
		if (promotion.getPromotionStatus().equals(PromotionsStatusEnum.END.name()) || promotion.getPromotionStatus().equals(PromotionsStatusEnum.CLOSE.name())) {// 如果存在活动
			// 删除活动
			promotionMap.remove(key);
		} else {
			promotionMap.put(key, promotion);
		}
//        LogUtils.info("ES修改商品活动索引-过滤后商品索引活动信息:{}", promotionMap);
		return this.getGoodsIndexPromotionUpdateRequest(goodsIndex.getId(), promotionMap);
	}

	/**
	 * 以更新部分字段的方式更新索引促销信息
	 *
	 * @param id           索引id
	 * @param promotionMap 促销信息
	 */
	private UpdateRequest getGoodsIndexPromotionUpdateRequest(String id, Map<String, Object> promotionMap) {
		UpdateRequest updateRequest = new UpdateRequest();
		updateRequest.index(getIndexName());
		updateRequest.id(id);
		updateRequest.retryOnConflict(3);
//        updateRequest.version(promotionMap.size());
		Map<String, Object> params = new HashMap<>();
		params.put("promotionMap", JSONUtil.toJsonStr(promotionMap));
		Script script = new Script(ScriptType.INLINE, "painless", "ctx._source.promotionMapJson=params.promotionMap;", params);
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
				LogUtils.info("批量更新商品索引的促销信息中出现部分异常：{}", responses.buildFailureMessage());
			} else {
				LogUtils.info("批量更新商品索引的促销信息结果：{}", responses.status());
			}
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
	private void removePromotionKey(String currentKey, Map<String, Object> promotionMap, String... needRemoveKeys) {
		// 判定是否需要移除
		if (CharSequenceUtil.containsAny(currentKey, needRemoveKeys)) {

			List<String> removeKeys = new ArrayList<>();
			// 促销循环
			for (String entry : promotionMap.keySet()) {
				// 需要移除则进行移除处理
				for (String needRemoveKey : needRemoveKeys) {
					if (entry.contains(needRemoveKey) && currentKey.contains(needRemoveKey)) {
						removeKeys.add(entry);
						break;
					}
				}
			}
			// 移除促销信息
			removeKeys.forEach(promotionMap.keySet()::remove);
		}
	}

	private String getIndexName() {
		// 索引名称拼接
		return elasticsearchProperties.getIndexPrefix() + "_" + EsSuffix.GOODS_INDEX_NAME;
	}

	private EsGoodsIndex wrapperEsGoodsIndex(GoodsSkuDTO goodsSku, List<Map<String, Object>> brandList, List<Map<String, Object>> categoryList, List<Map<String, Object>> storeCategoryList) {
		EsGoodsIndex index = new EsGoodsIndex(goodsSku);

		// 商品参数索引
		if (CharSequenceUtil.isNotEmpty(goodsSku.getParams())) {
			List<GoodsParamsDTO> goodsParamDTOS = JSONUtil.toList(goodsSku.getParams(), GoodsParamsDTO.class);
			index = new EsGoodsIndex(goodsSku, goodsParamDTOS);
		}
		// 商品分类索引
		if (CollUtil.isNotEmpty(categoryList) && CharSequenceUtil.isNotEmpty(goodsSku.getCategoryPath())) {
			StringBuilder categoryNamePath = new StringBuilder();
			categoryList.stream().filter(o -> goodsSku.getCategoryPath().contains(o.get("id").toString())).forEach(p -> categoryNamePath.append(p.get("name")).append(","));
			if (CharSequenceUtil.isNotEmpty(categoryNamePath)) {
				categoryNamePath.deleteCharAt(categoryNamePath.length() - 1);
				index.setCategoryNamePath(categoryNamePath.toString());
			}
		}
		// 商品品牌索引
		if (CollUtil.isNotEmpty(brandList) && CharSequenceUtil.isNotEmpty(goodsSku.getBrandId())) {
			Optional<Map<String, Object>> brandInfo = brandList.stream().filter(p -> p.get("id").equals(goodsSku.getBrandId())).findFirst();
			if (brandInfo.isPresent()) {
				index.setBrandName(brandInfo.get().get("name").toString());
				index.setBrandUrl(brandInfo.get().get("logo").toString());
			}
		}
		// 店铺分类索引
		if (CollUtil.isNotEmpty(storeCategoryList) && CharSequenceUtil.isNotEmpty(goodsSku.getStoreCategoryPath())) {
			StringBuilder storeCategoryNamePath = new StringBuilder();
			storeCategoryList.stream().filter(o -> goodsSku.getStoreCategoryPath().contains(o.get("id").toString())).forEach(p -> storeCategoryNamePath.append(p.get("label_name").toString()).append(","));
			if (CharSequenceUtil.isNotEmpty(storeCategoryNamePath)) {
				storeCategoryNamePath.deleteCharAt(storeCategoryNamePath.length() - 1);
				index.setStoreCategoryNamePath(storeCategoryNamePath.toString());
			}
		}
		return index;
	}
}
