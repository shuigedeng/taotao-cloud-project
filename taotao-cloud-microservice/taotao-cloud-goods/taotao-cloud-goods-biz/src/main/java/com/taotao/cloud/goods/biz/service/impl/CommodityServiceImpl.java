package com.taotao.cloud.goods.biz.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.PageUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.goods.api.dto.CommodityDTO;
import com.taotao.cloud.goods.api.enums.GoodsAuthEnum;
import com.taotao.cloud.goods.api.vo.CommodityVO;
import com.taotao.cloud.goods.biz.entity.Commodity;
import com.taotao.cloud.goods.biz.entity.GoodsSku;
import com.taotao.cloud.goods.biz.mapper.CommodityMapper;
import com.taotao.cloud.goods.biz.service.CommodityService;
import com.taotao.cloud.goods.biz.service.GoodsSkuService;
import com.taotao.cloud.goods.biz.util.WechatLivePlayerUtil;
import java.util.List;
import java.util.Objects;
import org.apache.shardingsphere.distsql.parser.autogen.CommonDistSQLStatementParser.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 直播商品业务层实现
 */
@Service
public class CommodityServiceImpl extends ServiceImpl<CommodityMapper, Commodity> implements
	CommodityService {

	@Autowired
	private WechatLivePlayerUtil wechatLivePlayerUtil;
	@Autowired
	private GoodsSkuService goodsSkuService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean addCommodity(List<Commodity> commodityList) {
		String storeId = Objects.requireNonNull(UserContext.getCurrentUser()).getStoreId();
		for (Commodity commodity : commodityList) {
			//检测直播商品
			checkCommodity(commodity);
			commodity.setStoreId(storeId);
			//添加直播商品
			JSONObject json = wechatLivePlayerUtil.addGoods(commodity);
			if (!"0".equals(json.getStr("errcode"))) {
				log.error(json.getStr("errmsg"));
				throw new ServiceException(ResultCode.COMMODITY_ERROR);
			}
			commodity.setLiveGoodsId(Convert.toInt(json.getStr("goodsId")));
			commodity.setAuditId(json.getStr("auditId"));
			//默认为待审核状态
			commodity.setAuditStatus("0");
			this.save(commodity);
		}
		return true;
	}

	private void checkCommodity(Commodity commodity) {
		//商品是否审核通过
		GoodsSku goodsSku = goodsSkuService.getById(commodity.getSkuId());
		if (!goodsSku.getAuthFlag().equals(GoodsAuthEnum.PASS.name())) {
			throw new ServiceException(goodsSku.getGoodsName() + " 未审核通过，不能添加直播商品");
		}
		//是否已添加规格商品
		if (this.count(
			new LambdaQueryWrapper<Commodity>().eq(Commodity::getSkuId, commodity.getSkuId()))
			> 0) {
			throw new ServiceException(goodsSku.getGoodsName() + " 已添加规格商品，无法重复增加");
		}
	}

	@Override
	public boolean deleteCommodity(String goodsId) {
		AuthUser currentUser = UserContext.getCurrentUser();
		if (currentUser == null || (currentUser.getRole().equals(UserEnums.STORE)
			&& currentUser.getStoreId() == null)) {
			throw new ServiceException(ResultCode.USER_AUTHORITY_ERROR);
		}
		JSONObject json = wechatLivePlayerUtil.deleteGoods(goodsId);
		if ("0".equals(json.getStr("errcode"))) {
			return this.remove(
				new LambdaQueryWrapper<Commodity>().eq(Commodity::getLiveGoodsId, goodsId)
					.eq(Commodity::getStoreId, currentUser.getStoreId()));
		}
		return false;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void getGoodsWareHouse() {
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
					.eq(Commodity::getLiveGoodsId, commodityDTO.getGoods_id())
					.set(Commodity::getAuditStatus, commodityDTO.getAudit_status()));
			}
		}
	}

	@Override
	public IPage<CommodityVO> commodityList(PageVO pageVO, String name, String auditStatus) {
		return this.baseMapper.commodityVOList(PageUtil.initPage(pageVO),
			new QueryWrapper<CommodityVO>().like(name != null, "c.name", name)
				.eq(auditStatus != null, "c.audit_status", auditStatus)
				.eq(Objects.requireNonNull(UserContext.getCurrentUser()).getRole()
						.equals(UserEnums.STORE), "c.store_id",
					UserContext.getCurrentUser().getStoreId())
				.orderByDesc("create_time"));
	}
}
