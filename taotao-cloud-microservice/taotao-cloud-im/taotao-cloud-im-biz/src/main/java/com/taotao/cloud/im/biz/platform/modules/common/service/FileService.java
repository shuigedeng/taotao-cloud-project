package com.taotao.cloud.im.biz.platform.modules.common.service;

import com.platform.common.upload.vo.UploadAudioVo;
import com.platform.common.upload.vo.UploadFileVo;
import com.platform.common.upload.vo.UploadVideoVo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件服务
 */
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
