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

package com.taotao.cloud.auth.infrastructure.extension.oneClick.mobtech;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taotao.cloud.auth.application.login.extension.oneClick.mobtech.utils.Base64Utils;
import com.taotao.cloud.auth.application.login.extension.oneClick.mobtech.utils.BaseUtils;
import com.taotao.cloud.auth.application.login.extension.oneClick.mobtech.utils.DES;
import com.taotao.cloud.auth.application.login.extension.oneClick.mobtech.utils.SignUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Auth {
    private static String appkey = "*******";
    private static String appSecret = "*****";
    private static String authHost = "http://identify.verify.mob.com/";
    private static String url = authHost + "auth/auth/sdkClientFreeLogin";

    public static String getPhoneNumber(String mbToken, String opToken, String operator) throws Exception {
        HashMap<String, Object> request = new HashMap<>();
        request.put("appkey", appkey);
        request.put("token", mbToken);
        request.put("opToken", opToken);
        request.put("operator", operator);
        request.put("timestamp", System.currentTimeMillis());
        request.put("sign", SignUtil.getSign(request, appSecret));
        String response = Auth.postRequestNoSecurity(url, null, request);
        JSONObject jsonObject = JSONObject.parseObject(response);
        if (200 == jsonObject.getInteger("status")) {
            String res = jsonObject.getString("res");
            byte[] decode = DES.decode(Base64Utils.decode(res.getBytes()), appSecret.getBytes());
            jsonObject.put("res", JSONObject.parseObject(new String(decode)));
        }

        JSONObject jsonObject1 = JSON.parseObject(jsonObject.toString());
        String data = jsonObject1.getString("res");
        JSONObject jsondata = JSON.parseObject(data);
        return jsondata.getString("phone");
    }

    //    public static void main(String[] args) throws Exception {
    //        String authHost = "http://identify.verify.mob.com/";
    //        String url = authHost + "auth/auth/sdkClientFreeLogin";
    //        HashMap<String, Object> request = new HashMap<>();
    //        request.put("appkey", appkey);
    //        request.put("token", token);
    //        request.put("opToken", opToken);
    //        request.put("operator", operator);
    //        request.put("timestamp", System.currentTimeMillis());
    //        request.put("sign", SignUtil.getSign(request, appSecret));
    //        String response = postRequestNoSecurity(url, null, request);
    //
    //        JSONObject jsonObject = JSONObject.parseObject(response);
    //        if (200 == jsonObject.getInteger("status")) {
    //            String res = jsonObject.getString("res");
    //            byte[] decode = DES.decode(Base64Utils.decode(res.getBytes()), appSecret.getBytes());
    //            jsonObject.put("res", JSONObject.parseObject(new String(decode)));
    //        }
    //        LogUtils.info(jsonObject);
    //    }

    public static String postRequestNoSecurity(String url, Map<String, String> headers, Object data) throws Exception {
        String securityReq = JSON.toJSONString(data);
        OkHttpClient okHttpClient =
                new OkHttpClient.Builder().readTimeout(30, TimeUnit.SECONDS).build();
        RequestBody body = RequestBody.create(securityReq, MediaType.parse("application/json"));
        Request.Builder builder = new Request.Builder();
        if (!BaseUtils.isEmpty(headers)) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        final Request request = builder.addHeader("Content-Length", String.valueOf(securityReq.length()))
                .url(url)
                .post(body)
                .build();
        Call call = okHttpClient.newCall(request);
        Response response = call.execute();

        String securityRes = response.body().string();
        return securityRes;
    }
}
