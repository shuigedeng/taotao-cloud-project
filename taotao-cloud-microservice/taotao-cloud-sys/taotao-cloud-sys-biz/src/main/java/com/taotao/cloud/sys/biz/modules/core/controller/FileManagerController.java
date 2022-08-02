package com.taotao.cloud.sys.biz.modules.core.controller;

import com.taotao.cloud.sys.biz.modules.core.controller.dtos.ListFileInfo;
import com.taotao.cloud.sys.biz.modules.core.exception.ToolException;
import com.taotao.cloud.sys.biz.modules.core.service.file.FileManager;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/file/manager")
@Validated
public class FileManagerController {
    @Autowired
    private FileManager fileManager;

    /**
     * 子目录列表
     * @param relativePath 相对路径
     * @return
     */
    @GetMapping("/childNames")
    @ResponseBody
    public List<ListFileInfo> childNames(String relativePath){
        final File tmpBase = fileManager.getTmpBase();
        final File dir = new File(tmpBase, relativePath);
        if (!dir.exists()){
            throw new ToolException("指定路径不存在:"+relativePath);
        }
        if (dir.isFile()){
            throw new ToolException("没有子目录了:"+relativePath);
        }
        final File[] files = dir.listFiles();
        List<ListFileInfo> listFileInfos = new ArrayList<>(files.length);
        for (File file : files) {
            final ListFileInfo listFileInfo = new ListFileInfo(file.getName(), file.length(), file.isDirectory(), file.lastModified());
            final Path relativize = tmpBase.toPath().relativize(file.toPath());
            List<String> paths = new ArrayList<>();
            for (int i = 0; i < relativize.getNameCount(); i++) {
                paths.add(relativize.getName(i).toString());
            }
            listFileInfo.setPath(StringUtils.join(paths,"/"));

            // 如果目录, 计算大小
            if (listFileInfo.isDirectory()){
                final Collection<File> listFiles = FileUtils.listFiles(file, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
                final Long realFileSize = listFiles.stream().map(File::length).reduce(0L, (a, b) -> a + b);
                listFileInfo.setSize(realFileSize);
            }
            listFileInfos.add(listFileInfo);
        }
        return listFileInfos;
    }

    /**
     * 计算文件夹大小
     * @param relativePath
     * @return
     */
    @GetMapping("/calcDirectorySize")
    @ResponseBody
    public Long calcDirectorySize(String relativePath){
        final File tmpBase = fileManager.getTmpBase();
        final File dir = new File(tmpBase, relativePath);
        if (!dir.exists()){
            throw new ToolException("路径不存在:"+relativePath);
        }
        if (dir.isFile()){
            return dir.length();
        }
        final Collection<File> files = FileUtils.listFiles(dir, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        return files.stream().map(File::length).reduce(0L,(a,b) -> a+b);
    }

    /**
     * 删除文件列表, 可删除目录
     * @param relativePath 相对路径
     */
    @PostMapping("/deleteFiles")
    @ResponseBody
    public void deleteFiles(String relativePath) throws IOException {
        final File tmpBase = fileManager.getTmpBase();
        final File dir = new File(tmpBase, relativePath);
        if (!dir.exists()){
            throw new ToolException("路径不存在:"+relativePath);
        }
        FileUtils.forceDelete(dir);
    }

    /**
     * 给定的 baseName 相对于临时目录如果是文件直接下载
     * 如果是目录,查看里面是不是只有一个文件,如果只有一个文件则下载那个文件
     * 如果是目录,但目录里面有很多文件,则打包成 zip 下载
     * @param baseName 相对于临时目录的路径
     */
    @GetMapping("/download")
    public ResponseEntity download(@NotNull String baseName, HttpServletResponse response) throws IOException {
        Resource resource = fileManager.relativeResource(baseName);
        if (resource == null){
            throw new ToolException("未找到资源 "+baseName);
        }

        String filenameEncode = new String(resource.getFilename().getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", filenameEncode);
        headers.add("filename",filenameEncode);
        headers.add("Access-Control-Expose-Headers", "fileName");
        headers.add("Access-Control-Expose-Headers", "Content-Disposition");
//        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
//        headers.add("Content-Disposition", "attachment; filename=" + filenameEncode);
//        headers.add("Pragma", "no-cache");
//        headers.add("Expires", "0");
//        headers.add("Last-Modified", new Date().toString());
//        headers.add("ETag", String.valueOf(System.currentTimeMillis()));
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        ResponseEntity<Resource> body = ResponseEntity.ok()
                .headers(headers)
                .contentLength(resource.contentLength())
                .body(resource);
        return body;
    }
}
