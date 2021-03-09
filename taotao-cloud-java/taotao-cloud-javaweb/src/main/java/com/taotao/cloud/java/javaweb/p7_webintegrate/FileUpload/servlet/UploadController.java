package com.taotao.cloud.java.javaweb.p7_webintegrate.FileUpload.servlet;

import com.qf.utils.UploadUtils;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "UploadController",value = "/upload")
@MultipartConfig(maxFileSize = 1024*1024*100,maxRequestSize = 1024*1024*200)
public class UploadController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //实现文件上传

        //1.设置乱码
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        //2.获取请求的数据
        String username = request.getParameter("username");
        Part part = request.getPart("file1");

        //3.获取上传文件的路径  真实路径
        String uploadPath = request.getServletContext().getRealPath("/WEB-INF/upload");
        File file = new File(uploadPath);
        if(!file.exists()){
            file.mkdirs();//新建文件夹
        }

        //4.文件上传
        //生成唯一文件名
        String oldName = part.getSubmittedFileName();

        List<String> nameList = new ArrayList<>();
        nameList.add(".jpg");
        nameList.add(".png");
        nameList.add(".jpeg");

        String extName = oldName.substring(oldName.lastIndexOf("."));
        if(!nameList.contains(extName)){
            response.getWriter().println(oldName+"不符合文件上传的规则！");
            return;
        }

        String newName = UploadUtils.newFileName(oldName);
        //生成二级、三级目录 实现散列存储
        String newPath = UploadUtils.newFilePath(uploadPath,oldName);

        part.write(newPath+"\\"+newName);

        //5.响应客户端 上传成功！

        response.getWriter().println(part.getSubmittedFileName()+"上传成功！");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
