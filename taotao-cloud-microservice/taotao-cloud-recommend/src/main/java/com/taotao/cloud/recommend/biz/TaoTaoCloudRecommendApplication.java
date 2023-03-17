package com.taotao.cloud.recommend.biz;

import com.taotao.cloud.common.utils.common.PropertyUtils;
import com.taotao.cloud.recommend.biz.dto.ItemDTO;
import com.taotao.cloud.recommend.biz.service.Recommend;
import com.taotao.cloud.web.annotation.TaoTaoCloudApplication;
import java.util.List;
import org.springframework.boot.SpringApplication;


@TaoTaoCloudApplication
public class TaoTaoCloudRecommendApplication {

	public static void main(String[] args) {
		PropertyUtils.setDefaultProperty("taotao-cloud-recommend");

		SpringApplication.run(TaoTaoCloudRecommendApplication.class, args);

		System.out.println("------基于用户协同过滤推荐---------------下列电影");
		List<ItemDTO> itemList= Recommend.userCfRecommend(2);
		itemList.forEach(e-> System.out.println(e.getName()));
		System.out.println("------基于物品协同过滤推荐---------------下列电影");
		List<ItemDTO> itemList1= Recommend.itemCfRecommend(2);
		itemList1.forEach(e-> System.out.println(e.getName()));
	}

}
