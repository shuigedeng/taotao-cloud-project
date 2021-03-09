package com.taotao.cloud.java.javaee.s1.c5_springmvc.p2.java.web;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("/upload")
public class UploadController {

    @RequestMapping("/test1")
    public String test1(MultipartFile source, HttpSession session) throws IOException {
        System.out.println("test1");
        // 获取上传文件的 原始名称
        String filename = source.getOriginalFilename();
        // 生成一个唯一的文件名
        String uniqueFilename = UUID.randomUUID().toString();
        // 获取文件后缀  扩展名
        String ext = FilenameUtils.getExtension(filename);// js  jpg png  java  class
        // 拼接完整的唯一文件名
        String uniqueFilename2 = uniqueFilename+"."+ext; // xxxx.xx


        // 获取上传文件的 类型
        String contentType = source.getContentType();
        System.out.println(filename);
        System.out.println(contentType);

        // 保存文件
        //source.transferTo(new File("d:/abc.js"));
        String realPath = session.getServletContext().getRealPath("/upload");
        System.out.println("realPath:"+realPath);
        source.transferTo(new File(realPath+"\\"+uniqueFilename2));
        return "index";
    }
}
