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

package com.taotao.cloud.promotion.biz.task;

import com.taotao.cloud.goods.api.feign.GoodsEsIndexApi;
import com.taotao.cloud.promotion.biz.model.entity.Seckill;
import com.taotao.cloud.promotion.biz.service.business.ISeckillService;
import com.taotao.cloud.sys.api.enums.SettingCategoryEnum;
import com.taotao.cloud.sys.api.feign.SettingApi;
import com.taotao.cloud.sys.api.model.vo.setting.SeckillSetting;
import com.taotao.boot.web.timetask.EveryDayExecute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** 促销活动每日定时器 */
@Component
public class PromotionEverydayExecute implements EveryDayExecute {

    /** ES商品索引 */
    @Autowired
    private GoodsEsIndexApi esGoodsIndexApi;
    /** 系统设置 */
    @Autowired
    private SettingApi settingApi;
    /** 秒杀活动 */
    @Autowired
    private ISeckillService seckillService;

    /** 将已过期的促销活动置为结束 */
    @Override
    public void execute() {
        // 清除所以商品索引的无效促销活动
        this.esGoodsIndexApi.cleanInvalidPromotion();
        // 定时创建活动
        addSeckill();
    }

    /** 添加秒杀活动 从系统设置中获取秒杀活动的配置 添加30天后的秒杀活动 */
    private void addSeckill() {
        SeckillSetting seckillSetting = settingApi.getSeckillSetting(SettingCategoryEnum.SECKILL_SETTING.name());
        for (int i = 1; i <= ISeckillService.PRE_CREATION; i++) {
            Seckill seckill = new Seckill(i, seckillSetting.getHours(), seckillSetting.getSeckillRule());
            seckillService.savePromotions(seckill);
        }
    }
}
