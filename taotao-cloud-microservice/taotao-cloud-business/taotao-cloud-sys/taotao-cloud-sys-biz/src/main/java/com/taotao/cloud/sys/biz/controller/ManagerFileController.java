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

package com.taotao.cloud.sys.biz.controller;

import com.taotao.boot.common.model.result.Result;
import com.taotao.boot.security.spring.annotation.NotAuth;
import com.taotao.cloud.sys.api.model.vo.FileVO;
import com.taotao.cloud.sys.biz.service.IFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 文件管理API
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/11/12 17:42
 */
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("/file/manager")
@Tag(name = "文件管理API", description = "文件管理API")
public class ManagerFileController {

    private final IFileService fileService;
//
//    @Operation(summary = "上传单个文件", description = "上传单个文件111111111111")
//    @RequestLogger
//    @PreAuthorize("hasAuthority('file:upload')")
//    @PostMapping(value = "/upload", headers = "content-type=multipart/form-data")
//    public Result<UploadFileVO> upload(
//            @Parameter(description = "文件对象", required = true) @NotNull(message = "文件对象不能为空") @RequestPart("file")
//                    MultipartFile file) {
//
//        File upload = fileService.upload(file);
//        UploadFileVO result =
//                UploadFileVO.builder().id(upload.getId()).url(upload.getUrl()).build();
//        return Result.success(result);
//    }
//
//    @Operation(summary = "上传多个文件", description = "上传多个文件")
//    @RequestLogger
//    @PreAuthorize("hasAuthority('file:multiple:upload')")
//    @PostMapping(value = "/multiple/upload", headers = "content-type=multipart/form-data")
//    public Result<List<UploadFileVO>> uploadMultipleFiles(@RequestPart("files") MultipartFile[] files) {
//        if (files.length == 0) {
//            throw new BusinessException("文件不能为空");
//        }
//
//        List<File> uploads = Arrays.stream(files).map(fileService::upload).toList();
//
//        if (!CollectionUtils.isEmpty(uploads)) {
//            // List<UploadFileVO> result = uploads.stream().map(
//            //		upload -> UploadFileVO.builder().id(upload.getId()).url(upload.getUrl()).build())
//            //	.toList();
//            return Result.success(new ArrayList<>());
//        }
//
//        throw new BusinessException("文件上传失败");
//    }

    @Operation(summary = "根据id查询文件信息", description = "根据id查询文件信息aaaaaaaaaaaaaaaaaaaaaaa")
    //@PreAuthorize("hasAuthority('file:info:id')")
    @GetMapping("/info/id/{id:[0-9]*}")
    public Result<FileVO> findFileById(@PathVariable(value = "id") Long id) {
        return new Result<>();
    }


    //
    // @ApiOperation(value = "根据文件名删除oss上的文件", notes = "根据文件名删除oss上的文件")
    // @ApiImplicitParams({
    // 	@ApiImplicitParam(name = "token", value = "登录授权码", required = true, paramType = "header",
    // dataType = "String"),
    // 	@ApiImplicitParam(name = "fileName", value = "路径名称", required = true, dataType = "String",
    // 		example = "robot/2019/04/28/1556429167175766.jpg"),
    // })
    // @PostMapping("file/delete")
    // public Result<Object> delete(@RequestParam("fileName") String fileName) {
    // 	return fileUploadService.delete(fileName);
    // }
    //
    // @ApiOperation(value = "查询oss上的所有文件", notes = "查询oss上的所有文件")
    // @ApiImplicitParams({
    // 	@ApiImplicitParam(name = "token", value = "登录授权码", required = true, paramType = "header",
    // dataType = "String"),
    // })
    // @GetMapping("file/list")
    // public Result<List<OSSObjectSummary>> list() {
    // 	return fileUploadService.list();
    // }
    //
    // @ApiOperation(value = "根据文件名下载oss上的文件", notes = "根据文件名下载oss上的文件")
    // @ApiImplicitParams({
    // 	@ApiImplicitParam(name = "token", value = "登录授权码", required = true, paramType = "header",
    // dataType = "String"),
    // 	@ApiImplicitParam(name = "fileName", value = "路径名称", required = true, dataType = "String",
    // 		example = "robot/2019/04/28/1556429167175766.jpg"),
    // })
    // @GetMapping("file/download")
    // public void download(@RequestParam("fileName") String objectName, HttpServletResponse
    // response) throws IOException {
    // 	//通知浏览器以附件形式下载
    // 	response.setHeader("Content-Disposition",
    // 		"attachment;filename=" + new String(objectName.getBytes(), StandardCharsets.ISO_8859_1));
    // 	fileUploadService.exportOssFile(response.getOutputStream(), objectName);
    // }

	@NotAuth
	@Operation(summary = "测试mybatis sql", description = "测试mybatis sql")
	//@PreAuthorize("hasAuthority('file:info:id')")
	@GetMapping("/testMybatisQueryStructure")
	public Result<List<String>> testMybatisQueryStructure() {
		List<String> result = fileService.testMybatisQueryStructure();
		return Result.success(result);
	}

}
