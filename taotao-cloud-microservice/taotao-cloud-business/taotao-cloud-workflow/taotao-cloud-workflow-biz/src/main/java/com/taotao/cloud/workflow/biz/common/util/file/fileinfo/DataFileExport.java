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

package com.taotao.cloud.workflow.biz.common.util.file.fileinfo;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** 数据接口文件导入导出 */
@Component
@Slf4j
public class DataFileExport implements FileExport {
    @Autowired
    private UserProvider userProvider;

    @Autowired
    private ConfigValueUtil configValueUtil;

    @Override
    public DownloadVO exportFile(Object obj, String filePath, String fileName, String tableName) {
        /** 1.model拼凑成Json字符串 */
        String json = JsonUtil.getObjectToString(obj);
        /** 2.写入到文件中 */
        fileName += System.currentTimeMillis() + "." + tableName;
        FileUtil.writeToFile(json, filePath, fileName);
        /** 是否需要上产到minio */
        try {
            UploadUtil.uploadFile(configValueUtil.getFileType(), filePath + fileName, FileTypeEnum.EXPORT, fileName);
        } catch (IOException e) {
            log.error("上传文件失败，错误" + e.getMessage());
        }
        /** 生成下载下载文件路径 */
        DownloadVO vo = DownloadVO.builder()
                .name(fileName)
                .url(UploaderUtil.uploaderFile(userProvider.get().getId() + "#" + fileName + "#" + "export"))
                .build();
        return vo;
    }
}
