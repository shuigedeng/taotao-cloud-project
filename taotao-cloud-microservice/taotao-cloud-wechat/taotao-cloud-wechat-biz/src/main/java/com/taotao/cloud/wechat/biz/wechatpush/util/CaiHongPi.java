package com.taotao.cloud.wechat.biz.wechatpush.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CaiHongPi {
    private static String url = "http://api.tianapi.com/caihongpi/index?key=";
    private static List<String> jinJuList = new ArrayList<>();
    private static String name = "老婆";

    public static String getCaiHongPi(String key) {
        //默认彩虹屁
        String str = "阳光落在屋里，爱你藏在心里";
        try {
            JSONObject jsonObject = JSONObject.parseObject(HttpUtil.getUrl(url+key).replace("XXX", name));
            if (jsonObject.getIntValue("code") == 200) {
                str = jsonObject.getJSONArray("newslist").getJSONObject(0).getString("content");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 载入金句库
     */
    static {
        InputStream inputStream = CaiHongPi.class.getClassLoader().getResourceAsStream("static/jinju.txt");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String str = "";
            String temp = "";
            while ((temp = br.readLine()) != null) {
                if (!StringUtils.isEmpty(temp)) {
                    str = str + "\r\n" + temp;
                } else {
                    jinJuList.add(str);
                    str = "";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getJinJu() {
        Random random = new Random();
        return jinJuList.get(random.nextInt(jinJuList.size()));
    }

    public static void main(String[] args) {
        System.out.println(getJinJu());
//        System.out.println(getCaiHongPi());
    }
}
