package com.taotao.cloud.sys.biz.modules.mybtis.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import com.taotao.cloud.sys.biz.modules.core.service.file.FileManager;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;


import lombok.extern.slf4j.Slf4j;

/**
 * mybatis xml 文件管理
 */
@Service
@Slf4j
public class MybatisXmlFileManager {
    @Autowired
    private FileManager fileManager;

    /**
     * 给某一个项目添加一个 xml 文件
     * @param project
     * @param file
     */
    public void addXmlFiles(String project, List<MultipartFile> files) throws IOException {
        File projectDir = fileManager.mkDataDir(MybatisService.MODULE + "/" + project);
        for (MultipartFile file : files) {
            final File destFile = new File(projectDir, file.getOriginalFilename());
            FileCopyUtils.copy(file.getInputStream(),new FileOutputStream(destFile));
        }
    }

    /**
     * 删除一些 xml 文件
     * @param project
     * @param fileNames
     */
    public void dropXmlFiles(String project,List<String> fileNames){
        File projectDir = fileManager.mkDataDir(MybatisService.MODULE + "/" + project);
        for (String fileName : fileNames) {
            final File file = new File(projectDir, fileName);
            FileUtils.deleteQuietly(file);
        }
    }

    /**
     * 列出所有项目
     * @return
     */
    public List<String> projects(){
        File projectDir = fileManager.mkDataDir(MybatisService.MODULE);
        return Arrays.asList(projectDir.list());
    }

    /**
     * 列出项目所有的 xml 文件
     * @param project
     * @return
     */
    public List<String> projectXmlFiles(String project){
        File projectDir = fileManager.mkDataDir(MybatisService.MODULE + "/" + project);
        return Arrays.asList(projectDir.list());
    }

    /**
     * 读取 xml 文件内容
     * @param project
     * @param fileName
     * @return
     */
    public String xmlFileContent(String project,String fileName) throws IOException {
        File projectDir = fileManager.mkDataDir(MybatisService.MODULE + "/" + project);
        final File file = new File(projectDir, fileName);
        return FileUtils.readFileToString(file, StandardCharsets.UTF_8);
    }

    /**
     * 获取 xml 文件, 供别的地方调用
     * @param project
     * @param fileName
     * @return
     */
    public File xmlFile(String project,String fileName){
        File projectDir = fileManager.mkDataDir(MybatisService.MODULE + "/" + project);
        return new File(projectDir, fileName);
    }
}
