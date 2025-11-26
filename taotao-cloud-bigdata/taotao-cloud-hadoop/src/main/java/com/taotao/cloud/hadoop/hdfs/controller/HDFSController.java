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

package com.taotao.cloud.hadoop.hdfs.controller;

import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.core.model.Result;
import com.taotao.cloud.hadoop.hdfs.model.User;
import com.taotao.cloud.hadoop.hdfs.service.HdfsService;
import java.util.List;
import java.util.Map;
import com.taotao.boot.common.utils.lang.StringUtils;
import org.apache.hadoop.fs.BlockLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * HDFSController
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2020/10/29 15:40
 */
@RestController
@RequestMapping("/hadoop/hdfs")
public class HDFSController {

    @Autowired private HdfsService HdfsService;

    // @ApiOperation("创建文件夹")
    // @SysOperateLog(description = "创建文件夹")
    // @PreAuthorize("hasAuthority('sys:user:add')")
    @RequestMapping(value = "mkdir", method = RequestMethod.POST)
    public Result<String> mkdir(@RequestParam("path") String path) throws Exception {
        if (StringUtils.isEmpty(path)) {
            LogUtil.error("请求参数为空");
            return Result.failed("请求参数为空");
        }
        // 创建空文件夹
        boolean isOk = HdfsService.mkdir(path);
        if (isOk) {
            return Result.success("文件夹创建成功");
        } else {
            return Result.failed("文件夹创建失败");
        }
    }

    // @ApiOperation("读取HDFS目录信息")
    // @SysOperateLog(description = "读取HDFS目录信息")
    // @PreAuthorize("hasAuthority('sys:user:add')")
    @PostMapping(value = "readPathInfo")
    public Result<List<Map<String, Object>>> readPathInfo(@RequestParam("path") String path)
            throws Exception {
        List<Map<String, Object>> list = HdfsService.readPathInfo(path);
        return Result.success(list);
    }

    // @ApiOperation("获取HDFS文件在集群中的位置")
    // @SysOperateLog(description = "获取HDFS文件在集群中的位置")
    // @PreAuthorize("hasAuthority('sys:user:add')")
    @PostMapping("/getFileBlockLocations")
    public Result<BlockLocation[]> getFileBlockLocations(@RequestParam("path") String path)
            throws Exception {
        BlockLocation[] blockLocations = HdfsService.getFileBlockLocations(path);
        return Result.success(blockLocations);
    }

    // @ApiOperation("创建文件")
    // @SysOperateLog(description = "创建文件")
    // @PreAuthorize("hasAuthority('sys:user:add')")
    @PostMapping("/createFile")
    public Result<String> createFile(
            @RequestParam("path") String path, @RequestParam("file") MultipartFile file)
            throws Exception {
        if (StringUtils.isEmpty(path) || file.isEmpty()) {
            return Result.failed("请求参数为空");
        }
        HdfsService.createFile(path, file);
        return Result.success("创建文件成功");
    }

    // @ApiOperation("读取HDFS文件内容")
    // @SysOperateLog(description = "读取HDFS文件内容")
    // @PreAuthorize("hasAuthority('sys:user:add')")
    @PostMapping("/readFile")
    public Result<String> readFile(@RequestParam("path") String path) throws Exception {
        String targetPath = HdfsService.readFile(path);
        return Result.success(targetPath);
    }

    // @ApiOperation("读取HDFS文件转换成Byte类型")
    // @SysOperateLog(description = "读取HDFS文件转换成Byte类型")
    // @PreAuthorize("hasAuthority('sys:user:add')")
    @PostMapping("/openFileToBytes")
    public Result<String> openFileToBytes(@RequestParam("path") String path) throws Exception {
        byte[] files = HdfsService.openFileToBytes(path);
        return Result.success(new String(files));
    }

    // @ApiOperation("读取HDFS文件装换成User对象")
    // @SysOperateLog(description = "读取HDFS文件装换成User对象")
    // @PreAuthorize("hasAuthority('sys:user:add')")
    @PostMapping("/openFileToUser")
    public Result<User> openFileToUser(@RequestParam("path") String path) throws Exception {
        User user = HdfsService.openFileToObject(path, User.class);
        return Result.success(user);
    }

    // @ApiOperation("读取文件列表")
    // @SysOperateLog(description = "读取文件列表")
    // @PreAuthorize("hasAuthority('sys:user:add')")
    @PostMapping("/listFile")
    public Result<List<Map<String, String>>> listFile(@RequestParam("path") String path)
            throws Exception {
        List<Map<String, String>> returnList = HdfsService.listFile(path);
        return Result.success(returnList);
    }

    // @ApiOperation("重命名文件")
    // @SysOperateLog(description = "重命名文件")
    // @PreAuthorize("hasAuthority('sys:user:add')")
    @PostMapping("/renameFile")
    public Result<String> renameFile(
            @RequestParam("oldName") String oldName, @RequestParam("newName") String newName)
            throws Exception {
        if (StringUtils.isEmpty(oldName) || StringUtils.isEmpty(newName)) {
            return Result.failed("请求参数为空");
        }
        boolean isOk = HdfsService.renameFile(oldName, newName);
        if (isOk) {
            return Result.success("文件重命名成功");
        } else {
            return Result.failed("文件重命名失败");
        }
    }

    // @ApiOperation("删除文件")
    // @SysOperateLog(description = "删除文件")
    // @PreAuthorize("hasAuthority('sys:user:add')")
    @PostMapping("/deleteFile")
    public Result<String> deleteFile(@RequestParam("path") String path) throws Exception {
        boolean isOk = HdfsService.deleteFile(path);
        if (isOk) {
            return Result.success("文件删除成功");
        } else {
            return Result.failed("文件删除失败");
        }
    }

    // @ApiOperation("删除文件")
    // @SysOperateLog(description = "删除文件")
    // @PreAuthorize("hasAuthority('sys:user:add')")
    @PostMapping("/uploadFile")
    public Result<Boolean> uploadFile(
            @RequestParam("path") String path, @RequestParam("uploadPath") String uploadPath)
            throws Exception {
        HdfsService.uploadFile(path, uploadPath);
        return Result.success(true);
    }

    // @ApiOperation("下载文件")
    // @SysOperateLog(description = "下载文件")
    // @PreAuthorize("hasAuthority('sys:user:add')")
    @PostMapping("/downloadFile")
    public Result<Boolean> downloadFile(
            @RequestParam("path") String path, @RequestParam("downloadPath") String downloadPath)
            throws Exception {
        HdfsService.downloadFile(path, downloadPath);
        return Result.success(true);
    }

    // @ApiOperation("HDFS文件复制")
    // @SysOperateLog(description = "HDFS文件复制")
    // @PreAuthorize("hasAuthority('sys:user:add')")
    @PostMapping("/copyFile")
    public Result<Boolean> copyFile(
            @RequestParam("sourcePath") String sourcePath,
            @RequestParam("targetPath") String targetPath)
            throws Exception {
        HdfsService.copyFile(sourcePath, targetPath);
        return Result.success(true);
    }

    // @ApiOperation("查看文件是否已存在")
    // @SysOperateLog(description = "查看文件是否已存在")
    // @PreAuthorize("hasAuthority('sys:user:add')")
    @PostMapping("/existFile")
    public Result<Boolean> existFile(@RequestParam("path") String path) throws Exception {
        boolean isExist = HdfsService.existFile(path);
        return Result.success(isExist);
    }
}
