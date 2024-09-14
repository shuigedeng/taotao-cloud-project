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

package com.taotao.cloud.auth.infrastructure.extension.face.baidu;

import com.taotao.boot.common.utils.io.ResourceUtils;
import com.taotao.boot.common.utils.lang.StringUtils;
import com.taotao.boot.common.utils.log.LogUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.File;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
@Validated
@Tag(name = "百度人脸识别API", description = "百度人脸识别API")
@RestController
@RequestMapping("/login/face")
public class BaiduFaceController {

    @Autowired
    private FaceUtils faceUtils;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(String userName, String faceBase) throws IOException {
        if (!StringUtils.isEmpty(userName) && !StringUtils.isEmpty(faceBase)) {
            // 文件上传的地址
            String upPath = ResourceUtils.getURL("classpath:").getPath() + "static\\photo";
            // 用于查看路径是否正确
            LogUtils.info(upPath);
            // 图片名称
            String fileName = userName + System.currentTimeMillis() + ".png";
            LogUtils.info(upPath + "\\" + fileName);
            File file = new File(upPath + "\\" + fileName);

            // 往自己demo数据库里插入一条用户数据
            // Users user = new Users();
            // user.setUserName(userName);
            // user.setUserPhoto(upPath + "\\" + fileName);
            // Users exitUser = userService.selectUserByName(user);
            // if (exitUser != null) {
            // 	return "2";
            // }
            // userService.addUsers(user);
            //
            // // 往自己demo服务器里面上传摄像头捕获的图片
            // GenerateImage(faceBase, file);

            // 向百度云人脸库插入一张人脸
            faceUtils.upToStandard(upPath + "\\" + fileName);
            faceUtils.registerFace(userName, faceBase);

            // faceUtils.saveLocalImage(faceBase, file);
            // faceUtils.faceSetAddUser(faceBase, userName);
        }
        return "1";
    }
}
