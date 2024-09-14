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

import com.baidu.aip.face.AipFace;
import com.baidu.aip.face.MatchRequest;
import com.baidu.aip.util.Base64Util;
import com.taotao.boot.common.utils.log.LogUtils;
import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FaceUtils {

    @Autowired
    private AipFace client; // 按类型注入IOC容器中的AipFace客户端对象

    @Autowired
    private BaiduFaceProperties properties;

    private HashMap<String, String> options;

    @PostConstruct
    public void init() {
        options = new HashMap<String, String>();
        // 详细配置查看官网的技术文档
        options.put("quality_control", "NORMAL");
        options.put("face_field", "age");
        options.put("max_face_num", "2");
        options.put("face_type", "LIVE");
        options.put("liveness_control", "LOW");
    }

    /**
     * 判断图片中是否包含人脸 -- 人脸检测
     *
     * @param imagePath 需要检测的图片位置
     * @return 人脸检测到与否
     * @throws IOException
     */
    public boolean upToStandard(String imagePath) throws IOException {
        // 上传的图片 两种格式：Base64字符串形式/url地址
        byte[] bytes = Files.readAllBytes(Paths.get(imagePath));
        String encode = Base64Util.encode(bytes);
        /** 参数1：Base64字符串或者图片的url 参数2：图片形式 字符串（BASE64，URL） 参数3：hashMap中的基本参数配置（null:使用默认配置） */
        JSONObject res = client.detect(encode, "BASE64", null);
        LogUtils.info(res.toString(2)); // 以JSON的展开形式打印响应的信息
        // error_code对应的值为0，则表示成功
        return res.getInt("error_code") == 0;
    }

    /**
     * 注册信息时候调用 -- 人脸注册
     *
     * @param userId   保存在用户组里面的标签
     * @param facePath 需要上传的图片的绝对地址
     * @return 注册成功与否
     * @throws IOException
     */
    public boolean registerFace(String userId, String facePath) throws IOException {
        // 向百度智能云账户添加数据
        byte[] bytes = Files.readAllBytes(Paths.get(facePath));
        String image = Base64Util.encode(bytes);
        String imageType = "BASE64";
        JSONObject result = client.addUser(image, imageType, properties.getGroupId(), userId, options);
        LogUtils.info(result.toString(2));
        String msg = result.getString("error_msg");
        // 通用的看error_code，对应的值为0，则表示成功
        return result.getInt("error_code") == 0;
    }

    /**
     * 签到的时候调用 -- 人脸对比
     *
     * @param registerFace      注册时图片保存的绝对路径
     * @param comparedImagePath 签到时图片保存的绝对路径
     * @return 人脸相似度
     * @throws IOException
     */
    public boolean signFace(String registerFace, String comparedImagePath) throws IOException {
        // 文件的格式转换
        byte[] registerImageBytes = Files.readAllBytes(Paths.get(registerFace));
        byte[] comparedImageBytes = Files.readAllBytes(Paths.get(comparedImagePath));
        String image1 = Base64Util.encode(registerImageBytes);
        String image2 = Base64Util.encode(comparedImageBytes);
        // 封装图片信息
        MatchRequest req1 = new MatchRequest(image1, "BASE64");
        MatchRequest req2 = new MatchRequest(image2, "BASE64");
        ArrayList<MatchRequest> requests = new ArrayList<>();
        requests.add(req1);
        requests.add(req2);
        // 向百度智能云发起比较图片的请求
        JSONObject match = client.match(requests);
        LogUtils.info(match.toString(2));
        // 获取两张图片的相似度
        double similarity = match.getJSONObject("result").getDouble("score");
        // 自己定义 相似度 高于多少为同一人 (百度自身推荐值80)
        if (similarity > 85) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 人脸比对
     *
     * @param imgBash64 照片转bash64格式
     * @return
     */
    public Double verifyUser(String imgBash64) {
        // 传入可选参数调用接口
        HashMap<String, Object> options = new HashMap<String, Object>();
        JSONObject res = client.search(imgBash64, "BASE64", "user_01", options);
        LogUtils.info(res.toString(2));
        LogUtils.info("result: {}",res.getJSONObject("result"));
        LogUtils.info("user_list: {}",res.getJSONObject("result").getJSONArray("user_list"));
        JSONObject user = (JSONObject)
                res.getJSONObject("result").getJSONArray("user_list").get(0);

        return (Double) user.get("score");
    }

    public boolean saveLocalImage(String imgStr, File file) {
        // 图像数据为空
        if (imgStr == null) {
            return false;
        } else {
            try {
                // Base64解码
                byte[] bytes = Base64Util.decode(imgStr);
                for (int i = 0; i < bytes.length; ++i) {
                    if (bytes[i] < 0) {
                        bytes[i] += 256;
                    }
                }
                // 生成jpeg图片
                if (!file.exists()) {
                    file.getParentFile().mkdir();
                    OutputStream out = new FileOutputStream(file);
                    out.write(bytes);
                    out.flush();
                    out.close();
                    return true;
                }

            } catch (Exception e) {
				LogUtils.error(e);
                return false;
            }
        }
        return false;
    }

    public boolean faceSetAddUser(String faceBase, String username) {
        // 参数为数据库中注册的人脸
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("user_info", "user's info");
        JSONObject res = client.addUser(faceBase, "BASE64", "user_01", username, options);
        return true;
    }
}
