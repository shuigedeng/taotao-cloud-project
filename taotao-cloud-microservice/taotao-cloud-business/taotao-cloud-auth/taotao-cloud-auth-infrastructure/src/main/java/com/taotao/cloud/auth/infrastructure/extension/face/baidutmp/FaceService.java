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

import org.springframework.beans.factory.annotation.Autowired;

// @Service
public class FaceService {

    @Autowired
    private BaiduAiUtils baiduAiUtils;

    /**
     * 人脸登录
     */
    public String loginByFace(StringBuffer imagebast64) {
        // 处理base64编码内容
        String image = imagebast64.substring(imagebast64.indexOf(",") + 1, imagebast64.length());
        String userId = baiduAiUtils.faceSearch(image);
        return userId;
    }

    /**
     * 人脸注册
     */
    public Boolean registerFace(String userId, StringBuffer imagebast64) {
        // 处理base64编码内容
        String image = imagebast64.substring(imagebast64.indexOf(",") + 1, imagebast64.length());
        Boolean registerFace = baiduAiUtils.faceRegister(userId, image);
        return registerFace;
    }
}
