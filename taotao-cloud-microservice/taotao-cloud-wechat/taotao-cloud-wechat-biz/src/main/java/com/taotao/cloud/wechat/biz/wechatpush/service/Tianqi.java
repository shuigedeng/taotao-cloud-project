package com.taotao.cloud.wechat.biz.wechatpush.service;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taotao.cloud.wechat.biz.wechatpush.util.HttpUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class Tianqi {

    @Value("${weather.ak}")
    private String ak;

    @Value("${weather.district_id}")
    private String district_id;

    public JSONObject getNanjiTianqi() {
        String result = null;
        JSONObject today = new JSONObject();
        try {
            result = HttpUtil.getUrl("https://api.map.baidu.com/weather/v1/?district_id=" + district_id + "&data_type=all&ak=" + ak);
            JSONObject jsonObject = JSONObject.parseObject(result);
            if (jsonObject.getString("message").equals("success")) {
                JSONArray arr = jsonObject.getJSONObject("result").getJSONArray("forecasts");
                today = arr.getJSONObject(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return today;
    }
}
