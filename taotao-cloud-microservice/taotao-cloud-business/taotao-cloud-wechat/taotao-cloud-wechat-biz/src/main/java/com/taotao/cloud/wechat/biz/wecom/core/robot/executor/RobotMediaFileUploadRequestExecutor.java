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

package com.taotao.cloud.wechat.biz.wecom.core.robot.executor;

import cn.bootx.starter.wecom.code.WeComCode;
import cn.bootx.starter.wecom.core.robot.domin.UploadMedia;
import java.io.File;
import java.io.IOException;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.enums.WxType;
import me.chanjar.weixin.common.error.WxError;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.util.fs.FileUtils;
import me.chanjar.weixin.common.util.http.RequestExecutor;
import me.chanjar.weixin.common.util.http.ResponseHandler;

/**
 * 机器人文件素材上传
 *
 * @author xxm
 * @since 2022/7/23
 */
public class RobotMediaFileUploadRequestExecutor implements RequestExecutor<WxMediaUploadResult, UploadMedia> {

    @Override
    public WxMediaUploadResult execute(String uri, UploadMedia uploadMedia, WxType wxType)
            throws WxErrorException, IOException {
        File tmpFile = FileUtils.createTmpFile(
                uploadMedia.getInputStream(), uploadMedia.getFilename(), uploadMedia.getFileType());
        String filename = uploadMedia.getFilename() + "." + uploadMedia.getFileType();
        String response;
        response = HttpUtil.createPost(uri)
                .form(WeComCode.MEDIA, tmpFile, filename)
                .execute()
                .body();

        WxError result = WxError.fromJson(response);
        if (result.getErrorCode() != 0) {
            throw new WxErrorException(result);
        }
        return WxMediaUploadResult.fromJson(response);
    }

    @Override
    public void execute(String uri, UploadMedia data, ResponseHandler<WxMediaUploadResult> handler, WxType wxType)
            throws WxErrorException, IOException {
        handler.handle(this.execute(uri, data, wxType));
    }
}
