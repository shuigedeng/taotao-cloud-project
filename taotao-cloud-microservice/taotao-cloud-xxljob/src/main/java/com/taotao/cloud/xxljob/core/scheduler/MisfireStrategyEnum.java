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

package com.taotao.cloud.xxljob.core.scheduler;

import com.taotao.cloud.xxljob.core.util.I18nUtil;

/**
 * @author xuxueli 2020-10-29 21:11:23
 */
public enum MisfireStrategyEnum {

    /**
     * do nothing
     */
    DO_NOTHING(I18nUtil.getString("misfire_strategy_do_nothing")),

    /**
     * fire once now
     */
    FIRE_ONCE_NOW(I18nUtil.getString("misfire_strategy_fire_once_now"));

    private String title;

    MisfireStrategyEnum(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public static MisfireStrategyEnum match(String name, MisfireStrategyEnum defaultItem) {
        for (MisfireStrategyEnum item : MisfireStrategyEnum.values()) {
            if (item.name().equals(name)) {
                return item;
            }
        }
        return defaultItem;
    }
}
