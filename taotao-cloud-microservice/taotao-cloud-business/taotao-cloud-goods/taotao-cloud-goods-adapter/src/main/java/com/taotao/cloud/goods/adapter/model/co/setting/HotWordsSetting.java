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

package com.taotao.cloud.goods.adapter.model.co.setting;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/** 搜索热词 */
@Data
public class HotWordsSetting implements Serializable {

    // 热词1-5，默认分数1-5

    /** 热词默认配置 */
    private List<HotWordsSettingItem> hotWordsSettingItems = new ArrayList<>();

    /** 每日保存数量 */
    private Integer saveNum;
}
