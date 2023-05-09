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

package com.taotao.cloud.file.biz.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.io.FileTypeUtil;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.utils.common.SecurityUtils;
import com.taotao.cloud.file.api.model.vo.UploadFileVO;
import com.taotao.cloud.file.biz.entity.File;
import com.taotao.cloud.file.biz.mapper.IFileMapper;
import com.taotao.cloud.file.biz.repository.FileRepository;
import com.taotao.cloud.file.biz.repository.IFileRepository;
import com.taotao.cloud.file.biz.service.IFileService;
import com.taotao.cloud.oss.common.exception.UploadFileException;
import com.taotao.cloud.oss.common.model.OssInfo;
import com.taotao.cloud.oss.common.model.UploadFileInfo;
import com.taotao.cloud.oss.common.service.StandardOssClient;
import com.taotao.cloud.oss.common.service.UploadFileService;
import com.taotao.cloud.oss.common.util.FileUtil;
import com.taotao.cloud.web.base.service.impl.BaseSuperServiceImpl;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传服务
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/11/12 17:43
 */
@Service
public class FileServiceImpl extends BaseSuperServiceImpl<IFileMapper, File, FileRepository, IFileRepository, Long>
        implements IFileService {

    @Autowired
    private UploadFileService uploadFileService;

    @Autowired
    private StandardOssClient standardOssClient;

    @Override
    public File upload(MultipartFile file) {
        try {
            UploadFileInfo upload = uploadFileService.upload(file);

            // 添加文件数据
            return new File();
        } catch (UploadFileException e) {
            throw new BusinessException(e.getMessage());
        }
    }

    @Override
    public File findFileById(Long id) {
        Optional<File> optionalFile = ir().findById(id);
        return optionalFile.orElseThrow(() -> new BusinessException(ResultEnum.FILE_NOT_EXIST));
    }

    @Override
    public UploadFileVO uploadFile(String type, MultipartFile multipartFile) {
        // 上传文件
        OssInfo ossInfo = standardOssClient.upLoadWithMultipartFile(multipartFile);

        // 添加文件
        File file = File.builder()
                .bizType(type)
                .type(Optional.of(ossInfo.getUploadFileInfo().getFileType()).get())
                .contextType(multipartFile.getContentType())
                .ext(FileUtil.getExtension(multipartFile))
                .original(multipartFile.getOriginalFilename())
                .url(ossInfo.getUrl())
                .name(ossInfo.getName())
                .length(ossInfo.getLength())
                .md5(Optional.of(ossInfo.getUploadFileInfo().getFileMd5()).get())
                .build();

        try {
            file.setCreateTime(LocalDateTime.parse(ossInfo.getCreateTime(), DatePattern.NORM_DATETIME_FORMATTER));
            file.setUpdateTime(LocalDateTime.parse(ossInfo.getLastUpdateTime(), DatePattern.NORM_DATETIME_FORMATTER));
            file.setDataType(FileTypeUtil.getType(multipartFile.getInputStream()));
            file.setCreateBy(SecurityUtils.getCurrentUser().getUserId());
            file.setCreateName(SecurityUtils.getCurrentUser().getUsername());
        } catch (Exception ignored) {
        }
        file.setDelFlag(false);
        this.save(file);

        // 添加文件日志
        return null;
    }
    //
    // @Override
    // public Result<Object> delete(String objectName) {
    // 	try {
    // 		ossClient.deleteObject(ossConfig.getBucketName(), objectName);
    // 	} catch (OSSException | ClientException e) {
    // 		e.printStackTrace();
    // 		return new Result<>(ResultEnum.ERROR.getCode(), "删除文件失败");
    // 	}
    // 	return new Result<>();
    // }
    //
    // @Override
    // public Result<List<OSSObjectSummary>> list() {
    // 	// 设置最大个数。
    // 	final int maxKeys = 100;
    //
    // 	ObjectListing objectListing = ossClient.listObjects(new
    // ListObjectsRequest(ossConfig.getBucketName()).withMaxKeys(maxKeys));
    // 	List<OSSObjectSummary> result = objectListing.getObjectSummaries();
    // 	return new Result<List<OSSObjectSummary>>(result);
    // }
    //
    // @Override
    // public void exportOssFile(ServletOutputStream outputStream, String objectName) {
    // 	OSSObject ossObject = ossClient.getObject(ossConfig.getBucketName(), objectName);
    //
    // 	BufferedInputStream in = new BufferedInputStream(ossObject.getObjectContent());
    // 	BufferedOutputStream out = new BufferedOutputStream(outputStream);
    // 	try {
    // 		byte[] buffer = new byte[1024];
    // 		int lenght;
    // 		while ((lenght = in.read(buffer)) != -1) {
    // 			out.write(buffer, 0, lenght);
    // 		}
    //
    // 		out.flush();
    // 		out.close();
    // 		in.close();
    // 	} catch (IOException e) {
    // 		e.printStackTrace();
    // 	}
    // }

}
