package com.taotao.cloud.recommend.biz.service;


import com.taotao.cloud.recommend.biz.core.ItemCF;
import com.taotao.cloud.recommend.biz.core.UserCF;
import com.taotao.cloud.recommend.biz.dto.ItemDTO;
import com.taotao.cloud.recommend.biz.dto.RelateDTO;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 推荐服务
 */
public class Recommend {

	/**
	 * 方法描述: 猜你喜欢
	 *
	 * @param userId 用户id
	 * @Return {@link List<ItemDTO>}
	 * 
	 * @date 2020年07月31日 17:28:06
	 */
	public static List<ItemDTO> userCfRecommend(int userId) {
		List<RelateDTO> data = FileDataSource.getData();
		List<Integer> recommendations = UserCF.recommend(userId, data);
		return FileDataSource.getItemData().stream()
			.filter(e -> recommendations.contains(e.getId()))
			.collect(Collectors.toList());
	}


	/**
	 * 方法描述: 猜你喜欢
	 *
	 * @param itemId 物品id
	 * @Return {@link List<ItemDTO>}
	 * 
	 * @date 2020年07月31日 17:28:06
	 */
	public static List<ItemDTO> itemCfRecommend(int itemId) {
		List<RelateDTO> data = FileDataSource.getData();
		List<Integer> recommendations = ItemCF.recommend(itemId, data);
		return FileDataSource.getItemData().stream()
			.filter(e -> recommendations.contains(e.getId()))
			.collect(Collectors.toList());
	}


}
