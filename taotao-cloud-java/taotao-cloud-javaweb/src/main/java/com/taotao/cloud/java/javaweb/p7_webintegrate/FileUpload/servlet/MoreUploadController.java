package com.taotao.cloud.java.javaweb.p7_webintegrate.FileUpload.servlet;

import com.qf.utils.UploadUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

@WebServlet(name = "MoreUploadController",value = "/moreUpload")
@MultipartConfig(maxFileSize = 1024*1024*100,maxRequestSize = 1024*1024*200)
public class MoreUploadController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String basePath = request.getServletContext().getRealPath("/WEB-INF/upload");
        File file = new File(basePath);
        if(!file.exists()){
            file.mkdirs();
        }
        //获得表单提交的所有数据
        Collection<Part> parts = request.getParts();

        for (Part part : parts) {
            String filename = part.getSubmittedFileName();
            if(filename!=null){//文件
                if(filename.trim().equals("")){
                    continue;
                }
                String newName = UploadUtils.newFileName(filename);
                String newPath = UploadUtils.newFilePath(basePath,filename);

                part.write(newPath+"\\"+newName);

            }else{//普通表单项
                String username = request.getParameter(part.getName());
                System.out.println(username);
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
