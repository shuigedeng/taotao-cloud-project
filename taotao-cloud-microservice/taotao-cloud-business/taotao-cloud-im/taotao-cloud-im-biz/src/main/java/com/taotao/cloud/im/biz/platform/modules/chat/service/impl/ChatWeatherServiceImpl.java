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

package com.taotao.cloud.im.biz.platform.modules.chat.service.impl;

import com.platform.common.constant.ApiConstant;
import com.platform.common.exception.BaseException;
import com.platform.common.utils.redis.RedisUtils;
import com.platform.modules.chat.config.AmapConfig;
import com.platform.modules.chat.service.ChatWeatherService;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** 天气预报-服务层实现 q3z3 */
@Service("chatWeatherService")
public class ChatWeatherServiceImpl implements ChatWeatherService {

    // 文档地址
    // https://lbs.amap.com/api/webservice/guide/api/weatherinfo

    /** 接口地址 */
    private static final String URL =
            "https://restapi.amap.com/v3/weather/weatherInfo?city=CITY&&key=KEY&extensions=EXT";

    private static final String EXT_BASE = "base";
    private static final String EXT_ALL = "all";

    @Autowired
    private AmapConfig amapConfig;

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public Dict queryBase(String cityCode) {
        JSONArray jsonArray = doQuery(cityCode, EXT_BASE);
        return Dict.create().parseBean(jsonArray.get(0));
    }

    @Override
    public Dict queryPredict(String cityCode) {
        JSONArray jsonArray = doQuery(cityCode, EXT_ALL);
        return Dict.create().parseBean(jsonArray.get(0));
    }

    private JSONArray doQuery(String city, String extensions) {
        String key = StrUtil.format(ApiConstant.REDIS_MP_WEATHER, city, extensions);
        if (redisUtils.hasKey(key)) {
            return JSONUtil.parseArray(redisUtils.get(key));
        }
        String url =
                URL.replace("CITY", city).replace("KEY", amapConfig.getKey()).replace("EXT", extensions);
        String result = HttpUtil.get(url);
        JSONObject jsonObject = JSONUtil.parseObj(result);
        if (1 != jsonObject.getInt("status")) {
            throw new BaseException("天气接口异常，请稍后再试");
        }
        JSONArray jsonArray = jsonObject.getJSONArray(EXT_BASE.equals(extensions) ? "lives" : "forecasts");
        redisUtils.set(key, JSONUtil.toJsonStr(jsonArray), ApiConstant.REDIS_MP_WEATHER_TIME, TimeUnit.MINUTES);
        return jsonArray;
    }

    @Override
    public List<JSONObject> queryByCityName(String cityName) {
        JSONArray jsonArray = doQuery(cityName, EXT_BASE);
        return jsonArray.toList(JSONObject.class);
    }
}
