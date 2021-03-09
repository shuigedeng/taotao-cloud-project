package com.taotao.cloud.java.javaee.s1.c5_springmvc.p2.java.web;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileInputStream;
import java.io.IOException;

@Controller
@RequestMapping("/download")
public class DownloadController {

    @RequestMapping("/test1")
    public void test1(String name, HttpSession session, HttpServletResponse response) throws IOException {

        String realPath = session.getServletContext().getRealPath("/upload");
        String filePath = realPath+"\\"+name;

        //设置响应头  告知浏览器，要以附件的形式保存内容   filename=浏览器显示的下载文件名
        response.setHeader("content-disposition","attachment;filename="+name);

        // 响应
        IOUtils.copy(new FileInputStream(filePath), response.getOutputStream());

        //return null;
    }
}
