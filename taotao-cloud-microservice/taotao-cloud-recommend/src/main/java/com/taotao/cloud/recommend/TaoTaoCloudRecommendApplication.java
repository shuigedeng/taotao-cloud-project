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

package com.taotao.cloud.recommend;

import com.taotao.boot.common.utils.common.PropertyUtils;
import com.taotao.boot.common.utils.log.LogUtils;
import com.taotao.boot.core.startup.StartupSpringApplication;
import com.taotao.cloud.recommend.dto.ItemDTO;
import com.taotao.cloud.recommend.service.Recommend;
import com.taotao.boot.web.annotation.TaoTaoBootApplication;
import com.taotao.cloud.bootstrap.annotation.TaoTaoCloudApplication;
import java.util.List;
import org.springframework.boot.SpringApplication;

/**
 * 推荐系统中心
 *
 * @author shuigedeng
 * @version 2023.04
 * @since 2023-05-11 17:46:59
 */
@TaoTaoBootApplication
@TaoTaoCloudApplication
public class TaoTaoCloudRecommendApplication {

    public static void main(String[] args) {
		new StartupSpringApplication(TaoTaoCloudRecommendApplication.class)
			.setTtcBanner()
			.setTtcProfileIfNotExists("dev")
			.setTtcApplicationProperty("taotao-cloud-recommend")
			//.setTtcAllowBeanDefinitionOverriding(true)
			.run(args);

        LogUtils.info("------基于用户协同过滤推荐---------------下列电影");
        List<ItemDTO> itemList = Recommend.userCfRecommend(2);
        itemList.forEach(e -> LogUtils.info(e.getName()));
        LogUtils.info("------基于物品协同过滤推荐---------------下列电影");
        List<ItemDTO> itemList1 = Recommend.itemCfRecommend(2);
        itemList1.forEach(e -> LogUtils.info(e.getName()));
    }
}
