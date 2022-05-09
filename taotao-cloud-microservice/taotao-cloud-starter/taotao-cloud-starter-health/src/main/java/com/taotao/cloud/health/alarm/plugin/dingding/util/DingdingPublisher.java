package com.taotao.cloud.health.alarm.plugin.dingding.util;

import com.alibaba.fastjson.JSONObject;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DingdingPublisher {
    private static final String TEMPLATE = "title:\t%s\n\ncontent:\t%s";
    private static final Logger logger = LoggerFactory.getLogger(DingdingPublisher.class);
    private static final String DING_TALK_URL = "https://oapi.dingtalk.com/robot/send?access_token=";
    private static final MediaType JSON;
    private static OkHttpClient okHttpClient;

    static {
        okHttpClient = new OkHttpClient();
        JSON = MediaType.get("application/json; charset=utf-8");
    }

    public static void sendMessage(String title, String content, String token) {
        String msg = String.format(TEMPLATE, title, content);

        try {
            doPost(msg, token);
        } catch (Exception e) {
            logger.error("failed to publish msg: {} to DingDing! {}", msg, e);
        }
    }

    public static String doPost(String msg, String token) throws IOException {
        RequestBody body = RequestBody.create(buildTextMsgBody(msg), JSON);

        try (Response response = okHttpClient
                .newCall(new Request.Builder().url(DING_TALK_URL + token).post(body).build()).execute()) {
            return response.body().string();
        }
    }

    private static String buildTextMsgBody(String content) {
        JSONObject msg = new JSONObject();
        msg.put("msgtype", "text");
        JSONObject text = new JSONObject();
        text.put("content", content);
        msg.put("text", text);
        return msg.toJSONString();
    }
}
