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

package com.taotao.cloud.wechat.biz.niefy.modules.oss.cloud;

import com.github.niefy.common.exception.RRException;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

/**
 * 腾讯云存储
 *
 * @author Mark sunlightcs@gmail.com
 */
@Slf4j
public class QcloudAbstractCloudStorageService extends AbstractCloudStorageService {
    private COSClient client;
    private static final String SEPARTOR = "/";

    public QcloudAbstractCloudStorageService(CloudStorageConfig config) {
        this.config = config;

        // 初始化
        init();
    }

    private void init() {
        COSCredentials credentials = new BasicCOSCredentials(config.getQcloudSecretId(), config.getQcloudSecretKey());

        // 设置bucket所在的区域，华南：gz 华北：tj 华东：sh
        Region region = new Region(config.getQcloudRegion());
        // 初始化客户端配置
        ClientConfig clientConfig = new ClientConfig(region);

        client = new COSClient(credentials, clientConfig);
    }

    @Override
    public String upload(byte[] data, String path) {
        // 腾讯云必需要以"/"开头
        if (!path.startsWith(SEPARTOR)) {
            path = SEPARTOR + path;
        }
        ObjectMetadata objectMetadata = new ObjectMetadata();
        // 设置输入流长度为500
        objectMetadata.setContentLength(data.length);
        // 上传到腾讯云
        PutObjectRequest request = new PutObjectRequest(
                config.getQcloudBucketName(), path, new ByteArrayInputStream(data), objectMetadata);
        client.putObject(request);

        return config.getQcloudDomain() + path;
    }

    @Override
    public String upload(InputStream inputStream, String path) {
        try {
            byte[] data = IOUtils.toByteArray(inputStream);
            return this.upload(data, path);
        } catch (IOException e) {
            throw new RRException("上传文件失败", e);
        }
    }

    @Override
    public String uploadSuffix(byte[] data, String suffix) {
        return upload(data, getPath(config.getQcloudPrefix(), suffix));
    }

    @Override
    public String uploadSuffix(InputStream inputStream, String suffix) {
        return upload(inputStream, getPath(config.getQcloudPrefix(), suffix));
    }
}
