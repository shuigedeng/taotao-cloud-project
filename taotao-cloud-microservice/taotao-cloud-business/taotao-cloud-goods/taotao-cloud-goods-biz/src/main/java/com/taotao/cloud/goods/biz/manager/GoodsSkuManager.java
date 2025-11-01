package com.taotao.cloud.goods.biz.manager;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.taotao.boot.common.enums.ResultEnum;
import com.taotao.boot.common.enums.UserEnum;
import com.taotao.boot.common.exception.BusinessException;
import com.taotao.boot.common.utils.log.LogUtils;
import com.taotao.cloud.goods.biz.model.vo.GoodsSkuSpecGalleryVO;
import com.taotao.cloud.goods.biz.model.vo.GoodsSkuSpecVO;
import com.taotao.cloud.goods.biz.mapper.IGoodsMapper;
import com.taotao.cloud.goods.biz.mapper.IGoodsSkuMapper;
import com.taotao.cloud.goods.biz.model.entity.Goods;
import com.taotao.cloud.stream.framework.rocketmq.RocketmqSendCallbackBuilder;
import com.taotao.cloud.stream.framework.rocketmq.tags.GoodsTagsEnum;
import com.taotao.cloud.stream.properties.RocketmqCustomProperties;
import com.taotao.boot.security.spring.model.SecurityUser;
import com.taotao.boot.security.spring.utils.SecurityUtils;
import com.taotao.boot.web.annotation.Manager;
import com.taotao.boot.webagg.manager.BaseManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.*;
import org.apache.rocketmq.spring.core.RocketMQTemplate;


/**
 * 只能注入mapper或者dao  不能注入service
 *
 * @author shuigedeng
 * @version 2023.07
 * @see BaseManager
 * @since 2023-08-18 15:37:54
 */
@Manager
@AllArgsConstructor
public class GoodsSkuManager extends BaseManager {

	private final IGoodsSkuMapper goodsSkuMapper;
	private final IGoodsMapper goodsMapper;
	/**
	 * rocketMq服务
	 */
	private final RocketMQTemplate rocketMQTemplate;
	/**
	 * rocketMq配置
	 */
	private final RocketmqCustomProperties rocketmqCustomProperties;

	/**
	 * 根据商品分组商品sku及其规格信息
	 *
	 * @param goodsSkuSpecGalleryVOList 商品VO列表
	 * @return 分组后的商品sku及其规格信息
	 */
	public List<GoodsSkuSpecVO> groupBySkuAndSpec(
		List<GoodsSkuSpecGalleryVO> goodsSkuSpecGalleryVOList) {
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

	/**
	 * 发送删除es索引的信息
	 *
	 * @param goodsIds 商品id
	 */
	public void deleteEsGoods(List<Long> goodsIds) {
		// 商品删除消息
		String destination =
			rocketmqCustomProperties.getGoodsTopic() + ":" + GoodsTagsEnum.GOODS_DELETE.name();
		// 发送mq消息
		rocketMQTemplate.asyncSend(
			destination, JSONUtil.toJsonStr(goodsIds),
			RocketmqSendCallbackBuilder.commonCallback());
	}

	// 修改规格索引,发送mq消息
	public void sendUpdateIndexFieldsMap(Map<String, Object> updateIndexFieldsMap) {
		String destination =
			rocketmqCustomProperties.getGoodsTopic() + ":"
				+ GoodsTagsEnum.UPDATE_GOODS_INDEX_FIELD.name();
		rocketMQTemplate.asyncSend(
			destination, JSONUtil.toJsonStr(updateIndexFieldsMap),
			RocketmqSendCallbackBuilder.commonCallback());
	}


	/**
	 * 判断商品是否存在
	 *
	 * @param goodsId 商品id
	 * @return 商品信息
	 */
	public Goods checkExist(Long goodsId) {
		Goods goods = goodsMapper.selectById(goodsId);
		if (goods == null) {
			LogUtils.error("商品ID为" + goodsId + "的商品不存在");
			throw new BusinessException(ResultEnum.GOODS_NOT_EXIST);
		}
		return goods;
	}

	/**
	 * 获取UpdateWrapper（检查用户越权）
	 *
	 * @return updateWrapper
	 */
	public LambdaUpdateWrapper<Goods> getUpdateWrapperByStoreAuthority() {
		LambdaUpdateWrapper<Goods> updateWrapper = new LambdaUpdateWrapper<>();
		SecurityUser authUser = this.checkStoreAuthority();
		if (authUser != null) {
			updateWrapper.eq(Goods::getStoreId, authUser.getStoreId());
		}
		return updateWrapper;
	}

	/**
	 * 检查当前登录的店铺
	 *
	 * @return 当前登录的店铺
	 */
	public SecurityUser checkStoreAuthority() {
		SecurityUser currentUser = SecurityUtils.getCurrentUser();
		// 如果当前会员不为空，且为店铺角色
		if (currentUser != null
			&& (currentUser.getType().equals(UserEnum.STORE.getCode())
			&& currentUser.getStoreId() != null)) {
			return currentUser;
		}
		return null;
	}

	/**
	 * 检查当前登录的店铺
	 *
	 * @return 当前登录的店铺
	 */
	public SecurityUser checkManagerAuthority() {
		SecurityUser currentUser = SecurityUtils.getCurrentUser();
		// 如果当前会员不为空，且为店铺角色
		if (currentUser != null && (currentUser.getType().equals(UserEnum.MANAGER.getCode()))) {
			return currentUser;
		}
		else {
			throw new BusinessException(ResultEnum.USER_AUTHORITY_ERROR);
		}
	}

	/**
	 * 获取QueryWrapper（检查用户越权）
	 *
	 * @return queryWrapper
	 */
	public LambdaQueryWrapper<Goods> getQueryWrapperByStoreAuthority() {
		LambdaQueryWrapper<Goods> queryWrapper = new LambdaQueryWrapper<>();
		SecurityUser authUser = this.checkStoreAuthority();
		if (authUser != null) {
			queryWrapper.eq(Goods::getStoreId, authUser.getStoreId());
		}
		return queryWrapper;
	}
}
