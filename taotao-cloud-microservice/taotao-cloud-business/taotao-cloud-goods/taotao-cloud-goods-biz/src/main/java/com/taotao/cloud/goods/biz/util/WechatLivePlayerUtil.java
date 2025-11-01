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

package com.taotao.cloud.goods.biz.util;

import com.taotao.cloud.goods.biz.model.entity.Commodity;
import com.taotao.cloud.goods.biz.model.entity.Studio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 微信小程序直播工具类
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:03:35
 */
@Component
public class WechatLivePlayerUtil {

	// @Autowired
	// private WechatAccessTokenUtil wechatAccessTokenUtil;

	/**
	 * 微信媒体实效
	 */
	@Autowired
	private WechatMediaUtil wechatMediaUtil;

	/**
	 * 创建小程序直播间
	 *
	 * @param studio 小程序直播
	 * @return {@link Map }<{@link String }, {@link String }>
	 * @since 2022-04-27 17:03:35
	 */
	public Map<String, String> create(Studio studio) {
		// 发送url
		String url = "https://api.weixin.qq.com/wxaapi/broadcast/room/create?access_token=";
		// 添加直播间
		Map<String, String> map = this.mockRoom(studio);
		JSONObject json = this.doPostWithJson(url, map);
		Map<String, String> roomMap = new HashMap<>(2);
		roomMap.put("roomId", json.getStr("roomId"));
		roomMap.put("qrcodeUrl", json.getStr("qrcode_url"));
		return roomMap;
	}

	/**
	 * 创建小程序直播间
	 *
	 * @param studio 小程序直播
	 * @return boolean
	 * @since 2022-04-27 17:03:35
	 */
	public boolean editRoom(Studio studio) {
		// 发送url
		String url = "https://api.weixin.qq.com/wxaapi/broadcast/room/editroom?access_token=";

		// 修改直播间
		Map<String, String> map = this.mockRoom(studio);
		map.put("id", studio.getRoomId().toString());
		this.doPostWithJson(url, map);
		return true;
	}

	/**
	 * 获取直播间回放
	 *
	 * @param roomId 房间ID
	 * @return {@link String }
	 * @since 2022-04-27 17:03:35
	 */
	public String getLiveInfo(Integer roomId) {
		// 发送url
		String url = "https://api.weixin.qq.com/wxa/business/getliveinfo?access_token=";
		Map<String, Object> map = new HashMap<>(16);
		// 获取回放
		map.put("action", "get_replay");
		// 直播间ID
		map.put("room_id", roomId);
		// 起始拉取视频，0表示从第一个视频片段开始拉取
		map.put("start", "0");
		// 每次拉取的数量，建议100以内
		map.put("limit", "1");
		JSONObject json = this.doPostWithJson(url, map);
		// TODO get media_url
		return json.getStr("live_replay");
	}

	/**
	 * 推送直播间商品
	 *
	 * @param roomId  房间ID
	 * @param goodsId 商品ID
	 * @return {@link Boolean }
	 * @since 2022-04-27 17:03:35
	 */
	public Boolean pushGoods(Integer roomId, Long goodsId) {
		// 发送url
		String url = "https://api.weixin.qq.com/wxaapi/broadcast/room/addgoods?access_token=";
		Map<String, Object> map = new HashMap<>(16);
		// 直播间回放
		Long[] ids = {goodsId};
		map.put("ids", ids);
		// 商品ID
		map.put("roomId", roomId);
		this.doPostWithJson(url, map);
		return true;
	}

	/**
	 * 删除直播间商品
	 *
	 * @param roomId  房间ID
	 * @param goodsId 商品ID
	 * @return {@link Boolean }
	 * @since 2022-04-27 17:03:35
	 */
	public Boolean goodsDeleteInRoom(Integer roomId, Long goodsId) {
		// 发送url
		String url = "https://api.weixin.qq.com/wxaapi/broadcast/goods/deleteInRoom?access_token=";
		Map<String, Object> map = new HashMap<>(2);
		// 直播间回放
		map.put("goodsId", goodsId);
		// 商品ID
		map.put("roomId", roomId);
		this.doPostWithJson(url, map);
		return true;
	}

	/**
	 * 添加直播商品
	 *
	 * @param commodity 直播商品
	 * @return {@link JSONObject }
	 * @since 2022-04-27 17:03:35
	 */
	public JSONObject addGoods(Commodity commodity) {
		// 发送url
		String url = "https://api.weixin.qq.com/wxaapi/broadcast/goods/add?access_token=";

		// 商品名称，最长14个汉字
		if (commodity.getName().length() > 14) {
			commodity.setName(commodity.getName().substring(0, 13));
		}

		//// 新建微信商品DTO
		// GoodsInfo goodsInfo = new GoodsInfo(commodity);
		//// 上传微信临时图片
		// goodsInfo.setCoverImgUrl(wechatMediaUtil.uploadMedia("image",
		// commodity.getGoodsImage()));
		// Map<String, GoodsInfo> map = new HashMap<>(2);
		//// 调用新增直播商品接口
		// map.put("goodsInfo", goodsInfo);
		// return this.doPostWithJson(url, map);
		return null;
	}

	/**
	 * 删除直播商品
	 *
	 * @param goodsId 商品ID
	 * @return {@link JSONObject }
	 * @since 2022-04-27 17:03:35
	 */
	public JSONObject deleteGoods(Long goodsId) {
		// 发送url
		String url = "https://api.weixin.qq.com/wxaapi/broadcast/goods/delete?access_token=";
		Map<String, Object> map = new HashMap<>(2);
		map.put("goodsId", goodsId);
		return this.doPostWithJson(url, map);
	}

	/**
	 * 查询直播商品状态
	 *
	 * @param goodsIdList 商品ID列表
	 * @return {@link JSONObject }
	 * @since 2022-04-27 17:03:36
	 */
	public JSONObject getGoodsWareHouse(List<String> goodsIdList) {
		// 发送url
		String url = "https://api.weixin.qq.com/wxa/business/getgoodswarehouse?access_token=";
		Map<String, Object> map = new HashMap<>(2);
		map.put("goods_ids", goodsIdList);
		return this.doPostWithJson(url, map);
	}

	/**
	 * 请求微信接口
	 *
	 * @param url 链接
	 * @param map 参数
	 * @return {@link JSONObject }
	 * @since 2022-04-27 17:03:36
	 */
	private JSONObject doPostWithJson(String url, Map map) {
		//// 获取token
		// String token = wechatAccessTokenUtil.cgiAccessToken(ClientTypeEnum.WECHAT_MP);
		//// 请求链接添加token
		// url += token;
		//// 发起请求
		// String content = HttpUtils.doPostWithJson(url, map);
		//// 记录请求结果
		// log.info("微信小程序请求结果：" + content);
		//// 获取请求内容，如果token过期则重新获取，如果出错则抛出错误
		// JSONObject jsonObject = new JSONObject(content);
		// if (("0").equals(jsonObject.get("errcode").toString())) {
		//	return jsonObject;
		// } else if (("40001").equals(jsonObject.get("errcode"))) {
		//	wechatAccessTokenUtil.removeAccessToken(ClientTypeEnum.WECHAT_MP);
		//	return this.doPostWithJson(url, map);
		// } else {
		//	throw new BusinessException(jsonObject.get("errmsg").toString());
		// }
		return null;
	}

	/**
	 * 模拟房间
	 *
	 * @param studio 工作室
	 * @return {@link Map }<{@link String }, {@link String }>
	 * @since 2022-04-27 17:03:36
	 */
	private Map<String, String> mockRoom(Studio studio) {
		Map<String, String> map = new HashMap<>(16);
		// 背景图
		map.put("coverImg", wechatMediaUtil.uploadMedia("image", studio.getCoverImg()));
		// 分享图
		map.put("shareImg", wechatMediaUtil.uploadMedia("image", studio.getShareImg()));
		// 购物直播频道封面图
		map.put("feedsImg", wechatMediaUtil.uploadMedia("image", studio.getFeedsImg()));
		// 直播间名字
		map.put("name", studio.getName());
		// 直播计划开始时间
		map.put("startTime", studio.getStartTime());
		// 直播计划结束时间
		map.put("endTime", studio.getEndTime());
		// 主播昵称
		map.put("anchorName", studio.getAnchorName());
		// 主播微信号
		map.put("anchorWechat", studio.getAnchorWechat());
		// 直播间类型
		map.put("type", "0");
		// 是否关闭点赞
		map.put("closeLike", "0");
		// 是否关闭货架
		map.put("closeGoods", "0");
		// 是否关闭评论
		map.put("closeComment", "0");
		// 直播间名字
		map.put("closeReplay", "0");

		return map;
	}
}
