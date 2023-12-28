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

package com.taotao.cloud.sys.biz.config.init;

import com.taotao.cloud.sys.biz.service.business.IRegionService;
import com.taotao.cloud.sys.biz.service.business.IVisitsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 初始化站点统计
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 11:54:05
 */
@Component
public class VisitsInit implements ApplicationRunner {

    @Autowired
    private IVisitsService visitsService;

    @Autowired
    private IRegionService regionService;

    @Override
    public void run(ApplicationArguments args) {
        // LogUtils.info("--------------- 初始化站点统计，如果存在今日统计则跳过 ---------------");
        // IVisitsService.save();
        // LogUtils.info("--------------- 初始化站点统计完成 ---------------");
        //
        // regionService.synchronizationData("");
    }
}
