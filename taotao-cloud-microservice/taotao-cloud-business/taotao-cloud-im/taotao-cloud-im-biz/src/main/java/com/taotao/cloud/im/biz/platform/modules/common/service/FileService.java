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

package com.taotao.cloud.im.biz.platform.modules.common.service;

import com.platform.common.upload.vo.UploadAudioVo;
import com.platform.common.upload.vo.UploadFileVo;
import com.platform.common.upload.vo.UploadVideoVo;
import org.springframework.web.multipart.MultipartFile;

/** 文件服务 */
public interface FileService {

    /**
     * 文件上传
     *
     * @param file
     * @return
     */
    UploadFileVo uploadFile(MultipartFile file);

    /**
     * 文件视频
     *
     * @param file
     * @return
     */
    UploadVideoVo uploadVideo(MultipartFile file);

    /**
     * 文件音频
     *
     * @param file
     * @return
     */
    UploadAudioVo uploadAudio(MultipartFile file);
}
