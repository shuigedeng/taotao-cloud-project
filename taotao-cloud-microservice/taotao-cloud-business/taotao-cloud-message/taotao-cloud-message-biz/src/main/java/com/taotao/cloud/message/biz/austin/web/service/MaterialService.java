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

package com.taotao.cloud.message.biz.austin.web.service;

import com.taotao.cloud.message.biz.austin.common.vo.BasicResultVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * 素材接口
 *
 * @author 3y
 */
public interface MaterialService {

    /**
     * 钉钉素材上传
     *
     * @param file
     * @param sendAccount
     * @param fileType
     * @return
     */
    BasicResultVO dingDingMaterialUpload(MultipartFile file, String sendAccount, String fileType);

    /**
     * 企业微信（机器人）素材上传
     *
     * @param file
     * @param sendAccount
     * @param fileType
     * @return
     */
    BasicResultVO enterpriseWeChatRootMaterialUpload(MultipartFile file, String sendAccount, String fileType);

    /**
     * 企业微信（应用消息）素材上传
     *
     * @param file
     * @param sendAccount
     * @param fileType
     * @return
     */
    BasicResultVO enterpriseWeChatMaterialUpload(MultipartFile file, String sendAccount, String fileType);
}
