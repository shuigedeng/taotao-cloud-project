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

package com.taotao.cloud.sys.biz.service.impl;

import com.taotao.cloud.sys.api.model.vo.UploadFileVO;
import com.taotao.cloud.sys.biz.model.entity.File;
import com.taotao.cloud.sys.biz.service.IFileService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传服务
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/11/12 17:43
 */
@AllArgsConstructor
@Service
public class FileServiceImpl
	implements IFileService {

	@Override
	public File upload(MultipartFile uploadFile) {
		return null;
	}

	@Override
	public File findFileById(Long id) {
		return null;
	}

	@Override
	public UploadFileVO uploadFile(String type, MultipartFile file) {
		return null;
	}

	@Override
	public List<String> testMybatisQueryStructure() {
		return List.of();
	}

	@Override
	public boolean testSeata() {
		return false;
	}

	@Override
	public void test(long fileId) {

	}

//    @Autowired
//    private UploadFileService uploadFileService;
//
//    @Autowired
//    private StandardOssClient standardOssClient;

//    private final TenantServiceApi tenantServiceApi;
//    private final IFeignQuartzJobApi feignQuartzJobApi;
//    private final ISeataTccService seataTccService;
//
//
//    @Override
//	@Transactional(rollbackFor = Exception.class)
//    @GlobalTransactional(rollbackFor = Exception.class)
//    public boolean testSeata() {
//        File file = new File();
//        file.setId(1L);
//        file.setCreateTime(LocalDateTime.now());
//        file.setCreateBy(1L);
//        file.setUpdateTime(LocalDateTime.now());
//        file.setCreateBy(1L);
//        file.setVersion(1);
//        file.setDelFlag(false);
//        file.setCreateName("xxx");
//        file.setBizType("asdfasf");
//        file.setDataType("DOC");
//        file.setOriginal("sdfasf");
//        file.setUrl("sdfasdf");
//        file.setMd5("sdfasf");
//        file.setType("sadf");
//        file.setContextType("sdf");
//        file.setName("sdf");
//        file.setExt("sdfa");
//        file.setLength(50L);
//        baseMapper.insert(file);
//
//        QuartzJobDTO quartzJobDTO = new QuartzJobDTO();
//        quartzJobDTO.setId(1L);
//        quartzJobDTO.setJobName("demoJob");
//        quartzJobDTO.setConcurrent(0);
//        quartzJobDTO.setJobClassName("com.taotao.cloud.xx.job.demoJob");
//        quartzJobDTO.setRemark("demo");
//        quartzJobDTO.setParams("sdfasf");
//        quartzJobDTO.setGroupName("demoJobGroup");
//        quartzJobDTO.setCronExpression("0 0 0 0 0 0");
//        quartzJobDTO.setMethodName("handleMessage");
//        quartzJobDTO.setBeanName("demoJob");
//        Boolean result = feignQuartzJobApi.addQuartzJobDTOTestSeata(quartzJobDTO);
//        if(result == null){
//            throw new BusinessException(500, "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
//        }
//
//        TenantDTO tenantDTO = new TenantDTO();
//        tenantDTO.setId(1L);
//        tenantDTO.setDelFlag(false);
//        tenantDTO.setName("123");
//        tenantDTO.setCreateTime(LocalDateTime.now());
//        tenantDTO.setExpireTime(LocalDateTime.now());
//        tenantDTO.setUpdateTime(LocalDateTime.now());
//        tenantDTO.setStatus(1);
//        tenantDTO.setAccountCount(1);
//        tenantDTO.setPackageId(1L);
//        tenantDTO.setPassword("sdfasf");
//        tenantDTO.setTenantAdminId(1L);
//        tenantDTO.setTenantAdminMobile("sdfsa");
//        tenantDTO.setTenantAdminName("sdfsa");
//        String s = tenantServiceApi.addTenantWithTestSeata(tenantDTO);
//        if(s == null){
//            throw new BusinessException(500, "qqqqqqqqq");
//        }
//
//        return true;
//    }
//
//    @Override
//    @GlobalTransactional
//    public void test(long fileId) {
//        seataTccService.tryInsert(1L);
//    }
//
//    @Override
//    public List<String> testMybatisQueryStructure() {
//        return this.baseMapper.testMybatisQueryStructure();
//    }
//    @Override
//    public File upload(MultipartFile file) {
//        try {
////            UploadFileInfo upload = uploadFileService.upload(file);
//
//            // 添加文件数据
//            return new File();
//        } catch (UploadFileException e) {
//            throw new BusinessException(e.getMessage());
//        }
//    }
//
//    @Override
//    @Transactional
//    public File findFileById(Long id) {
//
//        // 添加文件
//        File file = File.builder()
//                .bizType("测试")
//                .type("sdfasdfsdf")
//                .contextType("xxxx")
//                .ext("")
//                .original("asdfasdf")
//                .url("asdfasf")
//                .name("sdfasf")
//                .length(80L)
//                .md5("sdfasdf")
//                .build();
//
//        try {
//            file.setCreateTime(LocalDateTime.now());
//            file.setUpdateTime(LocalDateTime.now());
//            file.setDataType("sfdasfd");
//            file.setCreateBy(56L);
//            file.setCreateName("Sdfasf");
//        } catch (Exception ignored) {
//        }
//        this.save(file);
//
////        Optional<File> optionalFile = ir().findById(id);
////        return optionalFile.orElseThrow(() -> new BusinessException(ResultEnum.FILE_NOT_EXIST));
//        return file;
//    }
//
//    @Override
//    public UploadFileVO uploadFile(String type, MultipartFile multipartFile) {
//        // 上传文件
////        OssInfo ossInfo = standardOssClient.upLoadWithMultipartFile(multipartFile);
////
////        // 添加文件
////        File file = File.builder()
////                .bizType(type)
////                .type(Optional.of(ossInfo.getUploadFileInfo().getFileType()).get())
////                .contextType(multipartFile.getContentType())
////                .ext(FileUtil.getExtension(multipartFile))
////                .original(multipartFile.getOriginalFilename())
////                .url(ossInfo.getUrl())
////                .name(ossInfo.getName())
////                .length(ossInfo.getLength())
////                .md5(Optional.of(ossInfo.getUploadFileInfo().getFileMd5()).get())
////                .build();
////
////        try {
////            file.setCreateTime(LocalDateTime.parse(ossInfo.getCreateTime(), DatePattern.NORM_DATETIME_FORMATTER));
////            file.setUpdateTime(LocalDateTime.parse(ossInfo.getLastUpdateTime(), DatePattern.NORM_DATETIME_FORMATTER));
////            file.setDataType(FileTypeUtil.getType(multipartFile.getInputStream()));
////            file.setCreateBy(SecurityUtils.getCurrentUser().getUserId());
////            file.setCreateName(SecurityUtils.getCurrentUser().getUsername());
////        } catch (Exception ignored) {
////        }
////        file.setDelFlag(false);
////        this.save(file);
////
////        // 添加文件日志
//        return null;
//    }
//    //
//    // @Override
//    // public Result<Object> delete(String objectName) {
//    // 	try {
//    // 		ossClient.deleteObject(ossConfig.getBucketName(), objectName);
//    // 	} catch (OSSException | ClientException e) {
//    // 		LogUtils.error(e);
//    // 		return new Result<>(ResultEnum.ERROR.getCode(), "删除文件失败");
//    // 	}
//    // 	return new Result<>();
//    // }
//    //
//    // @Override
//    // public Result<List<OSSObjectSummary>> list() {
//    // 	// 设置最大个数。
//    // 	final int maxKeys = 100;
//    //
//    // 	ObjectListing objectListing = ossClient.listObjects(new
//    // ListObjectsRequest(ossConfig.getBucketName()).withMaxKeys(maxKeys));
//    // 	List<OSSObjectSummary> result = objectListing.getObjectSummaries();
//    // 	return new Result<List<OSSObjectSummary>>(result);
//    // }
//    //
//    // @Override
//    // public void exportOssFile(ServletOutputStream outputStream, String objectName) {
//    // 	OSSObject ossObject = ossClient.getObject(ossConfig.getBucketName(), objectName);
//    //
//    // 	BufferedInputStream in = new BufferedInputStream(ossObject.getObjectContent());
//    // 	BufferedOutputStream out = new BufferedOutputStream(outputStream);
//    // 	try {
//    // 		byte[] buffer = new byte[1024];
//    // 		int lenght;
//    // 		while ((lenght = in.read(buffer)) != -1) {
//    // 			out.write(buffer, 0, lenght);
//    // 		}
//    //
//    // 		out.flush();
//    // 		out.close();
//    // 		in.close();
//    // 	} catch (IOException e) {
//    // 		LogUtils.error(e);
//    // 	}
//    // }

}
