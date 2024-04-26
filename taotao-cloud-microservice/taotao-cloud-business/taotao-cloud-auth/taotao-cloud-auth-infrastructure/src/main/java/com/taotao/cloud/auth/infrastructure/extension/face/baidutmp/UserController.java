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

package com.taotao.cloud.auth.infrastructure.extension.face.baidutmp;

import com.alibaba.fastjson2.JSONObject;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

// @Controller
// @RequestMapping("/user")
public class UserController {

    @Autowired
    private FaceService faceService;

    @RequestMapping("/test")
    @ResponseBody
    public String test() {
        return "hello world";
    }

    /**
     * 人脸登录
     */
    @RequestMapping("/login")
    @ResponseBody
    public JSONObject searchface(@RequestBody JSONObject jsonObject) {
        StringBuffer imagebast64 = new StringBuffer(jsonObject.getString("imagebast64"));
        String userId = faceService.loginByFace(imagebast64);
        JSONObject res = new JSONObject();
        res.put("userId", userId);
        res.put("code", 200);
        return res;
    }

    /**
     * 人脸注册
     */
    @RequestMapping("/register")
    @ResponseBody
    public JSONObject registerFace(@RequestBody JSONObject jsonObject) {
        StringBuffer imagebast64 = new StringBuffer(jsonObject.getString("imagebast64"));
        String userId = UUID.randomUUID().toString().substring(0, 4);
        Boolean registerFace = faceService.registerFace(userId, imagebast64);

        JSONObject res = new JSONObject();
        res.put("userId", userId);
        if (registerFace) {
            res.put("code", 200);
        }
        return res;
    }
}
