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

package com.taotao.cloud.recommend.service;

import com.taotao.cloud.recommend.core.ItemCF;
import com.taotao.cloud.recommend.core.UserCF;
import com.taotao.cloud.recommend.dto.ItemDTO;
import com.taotao.cloud.recommend.dto.RelateDTO;
import java.util.List;

/**
 * 推荐服务
 */
public class Recommend {

    /**
     * 方法描述: 猜你喜欢
     *
     * @param userId 用户id
     * @Return {@link List< ItemDTO >}
     *
     * @since 2020年07月31日 17:28:06
     */
    public static List<ItemDTO> userCfRecommend(int userId) {
        List<RelateDTO> data = FileDataSource.getData();
        List<Integer> recommendations = UserCF.recommend(userId, data);
        return FileDataSource.getItemData().stream()
                .filter(e -> recommendations.contains(e.getId()))
                .toList();
    }

    /**
     * 方法描述: 猜你喜欢
     *
     * @param itemId 物品id
     * @Return {@link List< ItemDTO >}
     *
     * @since 2020年07月31日 17:28:06
     */
    public static List<ItemDTO> itemCfRecommend(int itemId) {
        List<RelateDTO> data = FileDataSource.getData();
        List<Integer> recommendations = ItemCF.recommend(itemId, data);
        return FileDataSource.getItemData().stream()
                .filter(e -> recommendations.contains(e.getId()))
                .toList();
    }
}
