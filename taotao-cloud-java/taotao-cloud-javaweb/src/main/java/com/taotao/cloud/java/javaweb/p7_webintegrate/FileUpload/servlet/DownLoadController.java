package com.taotao.cloud.java.javaweb.p7_webintegrate.FileUpload.servlet;


import com.taotao.cloud.java.javaweb.p7_webintegrate.FileUpload.utils.UploadUtils;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;

@WebServlet(name = "DownLoadController",value = "/downLoad")
public class DownLoadController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        String basePath = request.getServletContext().getRealPath("/WEB-INF/upload");
        //UUID文件名
        String uuidFilename = request.getParameter("filename");
        //通过_拆分，UUID和源文件名称
        String filename = uuidFilename.split("_")[1];
        //通过工具类，使用源文件名称获得散列存储的路径，就是下载的路径
        String downPath = UploadUtils.newFilePath(basePath,filename);

        //设置响应头，响应的文件类型和如何处理该文件  附件下载
        response.setHeader("content-disposition","attachment;filename="+ URLEncoder.encode(filename,"UTF-8"));
        FileInputStream is = new FileInputStream(downPath+"\\"+uuidFilename);
        ServletOutputStream sos = response.getOutputStream();
        byte[] buffer = new byte[1024*1024*100];
        int len = 0;
        while((len=is.read(buffer))!=-1){
            sos.write(buffer,0,len);
        }

        sos.close();
        is.close();

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
