package com.taotao.cloud.im.biz.platform.modules.chat.service;

import cn.hutool.core.lang.Dict;
import cn.hutool.json.JSONObject;

import java.util.List;

/**
 * <p>
 * 待办事项 服务层
 * q3z3
 * </p>
 */
public interface ChatWeatherService {

    /**
     * 实况天气
     */
    Dict queryBase(String cityCode);

    /**
     * 预报天气
     */
    Dict queryPredict(String cityCode);

    /**
     * 预报天气
     */
    List<JSONObject> queryByCityName(String cityName);
}
