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

package com.taotao.cloud.message.biz.austin.support.utils;

import org.dromara.hutoolcore.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author 3y
 * @description 对象存储（七牛云）
 */
public class OssUtils {

    public static void main(String[] args) throws UnsupportedEncodingException {
        // upload();
        String s = getFileUrl("FmnBLE4QtkwlErXIAh9pYS029GZk");
        LogUtils.info(s);
    }

    private static void upload() {
        // 构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.autoRegion());
        // 指定分片上传版本
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;
        // ...其他参数参考类注释

        UploadManager uploadManager = new UploadManager(cfg);
        // ...生成上传凭证，然后准备上传
        String accessKey = "123";
        String secretKey = "123";
        String bucket = "austin3y";
        // 如果是Windows情况下，格式是 D:\\qiniu\\test.png
        String localFilePath = "C:\\Users\\zhongfucheng\\Desktop\\1201.jpg";
        // 默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = IdUtil.fastSimpleUUID();

        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);

        try {
            Response response = uploadManager.put(localFilePath, key, upToken);
            DefaultPutRet putRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);
            // 解析上传成功的结果
            LogUtils.info(putRet.key);
            LogUtils.info(putRet.hash);
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                // ignore
            }
        }
    }

    public static String getFileUrl(String fileName) throws UnsupportedEncodingException {

        String domainOfBucket = "http://devtools.qiniu.com/austin3y";
        String encodedFileName = URLEncoder.encode(fileName, "utf-8").replace("+", "%20");
        String finalUrl = String.format("%s/%s", domainOfBucket, encodedFileName);
        LogUtils.info(finalUrl);
        return finalUrl;
    }
}
