package com.taotao.cloud.goods.biz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.common.FileUtil;
import com.taotao.cloud.goods.biz.entity.GoodsGallery;
import com.taotao.cloud.goods.biz.mapper.GoodsGalleryMapper;
import com.taotao.cloud.goods.biz.service.GoodsGalleryService;
import com.taotao.cloud.sys.api.enums.SettingEnum;
import com.taotao.cloud.sys.api.feign.IFeignSettingService;
import com.taotao.cloud.sys.api.vo.setting.GoodsSettingVO;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 商品相册接口实现
 */
@AllArgsConstructor
@Service
public class GoodsGalleryServiceImpl extends
	ServiceImpl<GoodsGalleryMapper, GoodsGallery> implements GoodsGalleryService {

	/**
	 * 设置
	 */
	@Autowired
	private IFeignSettingService settingService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean add(List<String> goodsGalleryList, Long goodsId) {
		//删除原来商品相册信息
		this.baseMapper.delete(new UpdateWrapper<GoodsGallery>().eq("goods_id", goodsId));
		//确定好图片选择器后进行处理
		int i = 0;
		for (String origin : goodsGalleryList) {
			//获取带所有缩略的相册
			GoodsGallery galley = this.getGoodsGallery(origin);
			galley.setGoodsId(goodsId);
			//默认第一个为默认图片
			galley.setIsDefault(i == 0 ? 1 : 0);
			i++;
			this.baseMapper.insert(galley);
		}
		return true;
	}

	@Override
	public GoodsGallery getGoodsGallery(String origin) {
		GoodsGallery goodsGallery = new GoodsGallery();
		//获取商品系统配置决定是否审核
		Result<GoodsSettingVO> setting = settingService.getGoodsSetting(
			SettingEnum.GOODS_SETTING.name());
		GoodsSettingVO goodsSetting = setting.data();
		//缩略图
		String thumbnail = FileUtil.getUrl(origin, goodsSetting.getAbbreviationPictureWidth(),
			goodsSetting.getAbbreviationPictureHeight());
		//小图
		String small = FileUtil.getUrl(origin, goodsSetting.getSmallPictureWidth(),
			goodsSetting.getSmallPictureHeight());
		//赋值
		goodsGallery.setSmall(small);
		goodsGallery.setThumbnail(thumbnail);
		goodsGallery.setOriginal(origin);
		return goodsGallery;
	}

	@Override
	public List<GoodsGallery> goodsGalleryList(Long goodsId) {
		//根据商品id查询商品相册
		return this.baseMapper.selectList(new QueryWrapper<GoodsGallery>().eq("goods_id", goodsId));
	}
}
