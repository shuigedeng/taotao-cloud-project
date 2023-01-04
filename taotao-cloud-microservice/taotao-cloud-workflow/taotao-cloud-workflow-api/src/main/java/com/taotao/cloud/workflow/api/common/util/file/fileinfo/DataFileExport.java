package com.taotao.cloud.workflow.api.common.util.file.fileinfo;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 数据接口文件导入导出
 *
 */
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
        DownloadVO vo = DownloadVO.builder().name(fileName).url(UploaderUtil.uploaderFile(userProvider.get().getId() + "#" + fileName + "#" + "export")).build();
        return vo;
    }

}
