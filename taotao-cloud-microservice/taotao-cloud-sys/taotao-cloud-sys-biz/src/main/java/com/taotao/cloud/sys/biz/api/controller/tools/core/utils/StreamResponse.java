package com.taotao.cloud.sys.biz.api.controller.tools.core.utils;

import java.io.*;
import java.net.URLEncoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

@Component
public class StreamResponse {
    /**
     * 作者:sanri <br/>
     * 时间:2017-10-31下午4:10:50<br/>
     * 功能:预览 <br/>
     *
     * @param input
     * @param mime
     * @throws IOException e
     */
    public void preview(InputStream input, MediaType mediaType, HttpServletResponse response) throws IOException {
        if (input == null) {
            return;
        }
        response.setContentType(mediaType.toString());
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(input);
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(response.getOutputStream())) {
            FileCopyUtils.copy(bufferedInputStream, bufferedOutputStream);
        }
    }

    /**
     * 设置响应头
     * @param mime
     * @param filename
     * @param response
     */
    public void preDownloadSetResponse(MediaType mediaType, String filename, HttpServletResponse response){
        response.setContentType(mediaType.toString());
        final String extension = FilenameUtils.getExtension(filename);
        final String baseName = FilenameUtils.getBaseName(filename);
        String encodeFileName = null;
        try {
            encodeFileName = URLEncoder.encode(baseName, "UTF-8") + "." + extension;
        } catch (UnsupportedEncodingException e) {
            // ignore
        }

//        response.setHeader("Content-Disposition", "attachment;filename=\"" + encodeFileName + "\"");
        response.setHeader("filename",encodeFileName);
        response.setHeader("Content-Disposition","attachment;filename=" + encodeFileName);
        response.setHeader("Access-Control-Expose-Headers", "*");
    }

    /**
     * 作者:sanri <br/>
     * 时间:2017-10-31下午4:18:54<br/>
     * 功能:下载 <br/>
     *
     * @param input
     * @param mime
     * @param fileName
     * @param response
     * @throws IOException
     */
    public void download(InputStream input, MediaType mediaType, String fileName, HttpServletResponse response) throws IOException {
        if (input == null) {
            return;
        }
        preDownloadSetResponse(mediaType,fileName,response);

        long length = input.available();
        if (length != -1) {
            response.setContentLength((int) length);
        }
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(input);
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(response.getOutputStream())) {
            FileCopyUtils.copy(bufferedInputStream, bufferedOutputStream);
        }
    }

}
