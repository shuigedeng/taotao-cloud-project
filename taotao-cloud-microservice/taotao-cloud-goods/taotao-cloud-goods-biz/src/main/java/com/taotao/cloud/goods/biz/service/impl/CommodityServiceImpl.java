package com.taotao.cloud.goods.biz.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.enums.UserEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.model.PageParam;
import com.taotao.cloud.common.model.SecurityUser;
import com.taotao.cloud.common.utils.common.SecurityUtil;
import com.taotao.cloud.goods.api.dto.CommodityDTO;
import com.taotao.cloud.goods.api.enums.GoodsAuthEnum;
import com.taotao.cloud.goods.api.vo.CommodityGoodsVO;
import com.taotao.cloud.goods.biz.entity.Commodity;
import com.taotao.cloud.goods.biz.entity.GoodsSku;
import com.taotao.cloud.goods.biz.mapper.ICommodityMapper;
import com.taotao.cloud.goods.biz.service.ICommodityService;
import com.taotao.cloud.goods.biz.service.IGoodsSkuService;
import com.taotao.cloud.goods.biz.util.WechatLivePlayerUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 直播商品业务层实现
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:02:18
 */
@AllArgsConstructor
@Service
public class CommodityServiceImpl extends ServiceImpl<ICommodityMapper, Commodity> implements
	ICommodityService {

	private final WechatLivePlayerUtil wechatLivePlayerUtil;
	private final IGoodsSkuService goodsSkuService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean addCommodity(List<Commodity> commodityList) {
		Long storeId = SecurityUtil.getCurrentUser().getStoreId();
		for (Commodity commodity : commodityList) {
			//检测直播商品
			checkCommodity(commodity);
			commodity.setStoreId(storeId);

			//添加直播商品
			JSONObject json = wechatLivePlayerUtil.addGoods(commodity);
			if (!"0".equals(json.getStr("errcode"))) {
				log.error(json.getStr("errmsg"));
				throw new BusinessException(ResultEnum.COMMODITY_ERROR);
			}

			commodity.setLiveGoodsId(Convert.toLong(json.getStr("goodsId")));
			commodity.setAuditId(json.getLong("auditId"));
			//默认为待审核状态
			commodity.setAuditStatus("0");
			this.save(commodity);
		}
		return true;
	}

	private void checkCommodity(Commodity commodity) {
		//商品是否审核通过
		GoodsSku goodsSku = goodsSkuService.getById(commodity.getSkuId());
		if (!goodsSku.getIsAuth().equals(GoodsAuthEnum.PASS.name())) {
			throw new BusinessException(goodsSku.getGoodsName() + " 未审核通过，不能添加直播商品");
		}

		//是否已添加规格商品
		if (this.count(
			new LambdaQueryWrapper<Commodity>().eq(Commodity::getSkuId, commodity.getSkuId()))
			> 0) {
			throw new BusinessException(goodsSku.getGoodsName() + " 已添加规格商品，无法重复增加");
		}
	}

	@Override
	public Boolean deleteCommodity(Long goodsId) {
		SecurityUser currentUser = SecurityUtil.getCurrentUser();
		if (currentUser == null || (currentUser.getType().equals(UserEnum.STORE.getCode())
			&& currentUser.getStoreId() == null)) {
			throw new BusinessException(ResultEnum.USER_AUTHORITY_ERROR);
		}

		JSONObject json = wechatLivePlayerUtil.deleteGoods(goodsId);
		if ("0".equals(json.getStr("errcode"))) {
			return this.remove(
				new LambdaQueryWrapper<Commodity>().eq(Commodity::getLiveGoodsId, goodsId)
					.eq(Commodity::getStoreId, SecurityUtil.getCurrentUser().getStoreId()));
		}
		return false;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean getGoodsWareHouse() {
		//查询审核中的商品
		List<String> goodsIdList = this.baseMapper.getAuditCommodity();
		if (!goodsIdList.isEmpty()) {
			//同步状态
			JSONObject json = wechatLivePlayerUtil.getGoodsWareHouse(goodsIdList);
			//修改状态
			List<CommodityDTO> commodityDTOList = JSONUtil.toList((JSONArray) json.get("goods"),
				CommodityDTO.class);
			for (CommodityDTO commodityDTO : commodityDTOList) {
				//修改审核状态
				this.update(new LambdaUpdateWrapper<Commodity>()
					.eq(Commodity::getLiveGoodsId, commodityDTO.goodsId())
					.set(Commodity::getAuditStatus, commodityDTO.auditStatus()));
			}
		}
		return true;
	}

	@Override
	public IPage<CommodityGoodsVO> commodityList(PageParam pageParam, String name, String auditStatus) {
		SecurityUser currentUser = SecurityUtil.getCurrentUser();
		return this.baseMapper.commodityVOList(pageParam.buildMpPage(),
			new QueryWrapper<CommodityGoodsVO>().like(name != null, "c.name", name)
				.eq(auditStatus != null, "c.audit_status", auditStatus)
				.eq(currentUser.getType().equals(UserEnum.STORE.getCode()), "c.store_id",
					currentUser.getStoreId())
				.orderByDesc("create_time"));
	}
}
