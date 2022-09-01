package com.taotao.cloud.order.biz.service.cart.render.impl;

import cn.hutool.core.text.CharSequenceUtil;
import com.taotao.cloud.common.enums.PromotionTypeEnum;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.goods.api.enums.GoodsAuthEnum;
import com.taotao.cloud.goods.api.enums.GoodsStatusEnum;
import com.taotao.cloud.goods.api.feign.IFeignGoodsSkuService;
import com.taotao.cloud.member.api.feign.IFeignMemberService;
import com.taotao.cloud.order.api.model.dto.cart.TradeDTO;
import com.taotao.cloud.order.api.enums.cart.CartTypeEnum;
import com.taotao.cloud.order.api.enums.cart.DeliveryMethodEnum;
import com.taotao.cloud.order.api.enums.cart.RenderStepEnums;
import com.taotao.cloud.order.api.model.vo.cart.CartSkuVO;
import com.taotao.cloud.order.api.model.vo.cart.CartVO;
import com.taotao.cloud.order.biz.model.entity.order.Order;
import com.taotao.cloud.order.biz.service.cart.render.ICartRenderStep;
import com.taotao.cloud.order.biz.service.order.IOrderService;
import com.taotao.cloud.promotion.api.feign.IFeignPintuanService;
import com.taotao.cloud.promotion.api.web.vo.PointsGoodsVO;
import lombok.AllArgsConstructor;
import org.apache.shardingsphere.distsql.parser.autogen.CommonDistSQLStatementParser.UserContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 商品有效性校验
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 08:50:37
 */
@AllArgsConstructor
@Service
public class CheckDataRender implements ICartRenderStep {

	private final IFeignGoodsSkuService goodsSkuService;

	private final IOrderService orderService;

	private final IFeignPintuanService pintuanService;

	private final IFeignMemberService memberService;

	private final PointsGoodsService pointsGoodsService;

	@Override
	public RenderStepEnums step() {
		return RenderStepEnums.CHECK_DATA;
	}

	@Override
	public void render(TradeDTO tradeDTO) {
		//预校验
		preCalibration(tradeDTO);

		//校验商品有效性
		checkData(tradeDTO);

		//店铺分组数据初始化
		groupStore(tradeDTO);
	}

	/**
	 * 校验商品属性
	 *
	 * @param tradeDTO 购物车视图
	 * @since 2022-04-28 08:52:11
	 */
	private void checkData(TradeDTO tradeDTO) {
		//循环购物车中的商品
		for (CartSkuVO cartSkuVO : tradeDTO.getSkuList()) {

			//如果失效，确认sku为未选中状态
			if (Boolean.TRUE.equals(cartSkuVO.getInvalid())) {
				//设置购物车未选中
				cartSkuVO.setChecked(false);
			}

			//缓存中的商品信息
			GoodsSku dataSku = goodsSkuService.getGoodsSkuByIdFromCache(
				cartSkuVO.getGoodsSku().getId());
			//商品有效性判定
			if (dataSku == null || dataSku.getUpdateTime()
				.before(cartSkuVO.getGoodsSku().getUpdateTime())) {
				//设置购物车未选中
				cartSkuVO.setChecked(false);
				//设置购物车此sku商品已失效
				cartSkuVO.setInvalid(true);
				//设置失效消息
				cartSkuVO.setErrorMessage("商品信息发生变化,已失效");
				continue;
			}
			//商品上架状态判定
			if (!GoodsAuthEnum.PASS.name().equals(dataSku.getIsAuth())
				|| !GoodsStatusEnum.UPPER.name().equals(dataSku.getMarketEnable())) {
				//设置购物车未选中
				cartSkuVO.setChecked(false);
				//设置购物车此sku商品已失效
				cartSkuVO.setInvalid(true);
				//设置失效消息
				cartSkuVO.setErrorMessage("商品已下架");
				continue;
			}
			//商品库存判定
			if (dataSku.getQuantity() < cartSkuVO.getNum()) {
				//设置购物车未选中
				cartSkuVO.setChecked(false);
				//设置失效消息
				cartSkuVO.setErrorMessage("商品库存不足,现有库存数量[" + dataSku.getQuantity() + "]");
			}
		}
	}

	/**
	 * 店铺分组
	 *
	 * @param tradeDTO tradeDTO
	 */
	private void groupStore(TradeDTO tradeDTO) {
		//渲染的购物车
		List<CartVO> cartList = new ArrayList<>();

		//根据店铺分组
		Map<String, List<CartSkuVO>> storeCollect = tradeDTO.getSkuList().parallelStream()
			.collect(Collectors.groupingBy(CartSkuVO::getStoreId));
		for (Map.Entry<String, List<CartSkuVO>> storeCart : storeCollect.entrySet()) {
			if (!storeCart.getValue().isEmpty()) {
				CartVO cartVO = new CartVO(storeCart.getValue().get(0));
				if (CharSequenceUtil.isEmpty(cartVO.getDeliveryMethod())) {
					cartVO.setDeliveryMethod(DeliveryMethodEnum.LOGISTICS.name());
				}
				cartVO.setSkuList(storeCart.getValue());
				storeCart.getValue().stream().filter(i -> Boolean.TRUE.equals(i.getChecked()))
					.findFirst().ifPresent(cartSkuVO -> cartVO.setChecked(true));
				cartList.add(cartVO);
			}
		}
		tradeDTO.setCartList(cartList);
	}

	/**
	 * 订单预校验 1、自己拼团自己创建都拼团判定、拼团限购 2、积分购买，积分足够与否
	 *
	 * @param tradeDTO tradeDTO
	 */
	private void preCalibration(TradeDTO tradeDTO) {

		//拼团订单预校验
		if (tradeDTO.getCartTypeEnum().equals(CartTypeEnum.PINTUAN)) {
			//拼团判定，不能参与自己创建的拼团
			if (tradeDTO.getParentOrderSn() != null) {
				//订单接收
				Order parentOrder = orderService.getBySn(tradeDTO.getParentOrderSn());
				//参与活动判定
				if (parentOrder.getMemberId().equals(UserContext.getCurrentUser().getId())) {
					throw new BusinessException(ResultEnum.PINTUAN_JOIN_ERROR);
				}
			}
			//判断拼团商品的限购数量
			Optional<String> pintuanId = tradeDTO.getSkuList().get(0).getPromotions().stream()
				.filter(i -> i.getPromotionType().equals(PromotionTypeEnum.PINTUAN.name()))
				.map(PromotionGoods::getPromotionId).findFirst();
			if (pintuanId.isPresent()) {
				Pintuan pintuan = pintuanService.getById(pintuanId.get());
				Integer limitNum = pintuan.getLimitNum();
				for (CartSkuVO cartSkuVO : tradeDTO.getSkuList()) {
					if (limitNum != 0 && cartSkuVO.getNum() > limitNum) {
						throw new BusinessException(ResultEnum.PINTUAN_LIMIT_NUM_ERROR);
					}
				}
			}
			//积分商品，判断用户积分是否满足
		} else if (tradeDTO.getCartTypeEnum().equals(CartTypeEnum.POINTS)) {
			String skuId = tradeDTO.getSkuList().get(0).getGoodsSku().getId();
			//获取积分商品VO
			PointsGoodsVO pointsGoodsVO = pointsGoodsService.getPointsGoodsDetailBySkuId(skuId);
			if (pointsGoodsVO == null) {
				throw new BusinessException(ResultEnum.POINT_GOODS_ERROR);
			}
			Member member = memberService.getUserInfo();
			if (member.getPoint() < pointsGoodsVO.getPoints()) {
				throw new BusinessException(ResultEnum.USER_POINTS_ERROR);
			}
		}

	}

}
