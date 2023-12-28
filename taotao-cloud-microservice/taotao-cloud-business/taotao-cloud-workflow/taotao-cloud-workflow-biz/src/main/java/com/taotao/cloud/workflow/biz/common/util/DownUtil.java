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

package com.taotao.cloud.workflow.biz.common.util;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.Cleanup;
import org.apache.poi.ss.usermodel.Workbook;

/** */
public class DownUtil {

    /**
     * 下载excel
     *
     * @param fileName excel名称
     * @param workbook
     */
    public static void dowloadExcel(Workbook workbook, String fileName) {
        try {
            HttpServletResponse response = ServletUtil.getResponse();
            response.setCharacterEncoding(Constants.UTF_8);
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader(
                    "Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, Constants.UTF_8));
            @Cleanup ServletOutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
        } catch (IOException e) {
            LogUtils.error(e);
        }
    }

    /**
     * 下载文件
     *
     * @param file 文件
     * @param fileName 订单信息.pdf
     */
    public static void dowloadFile(File file, String fileName) {
        String escapeFileName = XSSEscape.escape(fileName);
        HttpServletResponse response = ServletUtil.getResponse();
        HttpServletRequest request = ServletUtil.getRequest();
        try {
            @Cleanup InputStream is = new FileInputStream(file);
            @Cleanup BufferedInputStream bis = new BufferedInputStream(is);
            response.setCharacterEncoding(Constants.UTF_8);
            response.setContentType("application/x-download");
            // 编码的文件名字,关于中文乱码的改造
            String codeFileName = "";
            String agent = request.getHeader("USER-AGENT").toLowerCase();
            if (-1 != agent.indexOf("msie") || -1 != agent.indexOf("trident")) {
                // IE
                codeFileName = URLEncoder.encode(escapeFileName, Constants.UTF_8);
            } else if (-1 != agent.indexOf("mozilla")) {
                // 火狐，谷歌
                codeFileName = new String(escapeFileName.getBytes(Constants.UTF_8), "iso-8859-1");
            } else {
                codeFileName = URLEncoder.encode(escapeFileName, Constants.UTF_8);
            }
            response.setHeader("Content-Disposition", "attachment;filename=\"" + codeFileName + "\"");
            @Cleanup OutputStream os = response.getOutputStream();
            int i;
            byte[] buff = new byte[1024 * 8];
            while ((i = bis.read(buff)) != -1) {
                os.write(buff, 0, i);
            }
            os.flush();
        } catch (Exception e) {
            LogUtils.error(e);
        }
    }

    /**
     * 下载文件
     *
     * @param paths 路径
     * @param fileName 订单信息.pdf
     */
    public static Boolean dowloadFile(String paths, String fileName, String downName) {
        if (StringUtil.isNotEmpty(downName)) {
            fileName = downName;
        }
        String escapeFileName = XSSEscape.escape(fileName);
        HttpServletResponse response = ServletUtil.getResponse();
        HttpServletRequest request = ServletUtil.getRequest();
        try {
            @Cleanup InputStream is = new FileInputStream(new File(XSSEscape.escapePath(paths)));
            @Cleanup BufferedInputStream bis = new BufferedInputStream(is);
            response.setCharacterEncoding(Constants.UTF_8);
            response.setContentType("text/plain");
            if (escapeFileName.contains(".svg")) {
                response.setContentType("image/svg+xml");
            }
            // 编码的文件名字,关于中文乱码的改造
            String codeFileName = "";
            String agent = request.getHeader("USER-AGENT").toLowerCase();
            if (-1 != agent.indexOf("msie") || -1 != agent.indexOf("trident")) {
                // IE
                codeFileName = URLEncoder.encode(escapeFileName, Constants.UTF_8);
            } else if (-1 != agent.indexOf("mozilla")) {
                // 火狐，谷歌
                codeFileName = new String(escapeFileName.getBytes(Constants.UTF_8), "iso-8859-1");
            } else {
                codeFileName = URLEncoder.encode(escapeFileName, Constants.UTF_8);
            }
            response.setHeader(
                    "Content-Disposition", "attachment;filename=" + new String(codeFileName.getBytes(), "utf-8"));
            @Cleanup OutputStream os = response.getOutputStream();
            int i;
            byte[] buff = new byte[1024 * 8];
            while ((i = bis.read(buff)) != -1) {
                os.write(buff, 0, i);
            }
            os.flush();
        } catch (Exception e) {
            LogUtils.error(e);
            return false;
        }
        return true;
    }

    /** 显示验证码 */
    public static void downCode(Integer stringLength) {
        HttpServletResponse response = DownUtil.getResponse();
        CodeUtil codeUtil = new CodeUtil();
        codeUtil.getRandcode(response, stringLength);
    }

    /** 流返回界面 */
    public static void write(BufferedImage image) {
        try {
            HttpServletResponse response = DownUtil.getResponse();
            @Cleanup ServletOutputStream outputStream = response.getOutputStream();
            // 将内存中的图片通过流动形式输出到客户端
            ImageIO.write(image, "PNG", outputStream);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    /** 设置img的response */
    public static HttpServletResponse getResponse() {
        HttpServletResponse response = ServletUtil.getResponse();
        response.setCharacterEncoding(Constants.UTF_8);
        // 设置相应类型,告诉浏览器输出的内容为图片
        response.setContentType("image/jpeg");
        // 设置响应头信息，告诉浏览器不要缓存此内容
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expire", 0);
        return response;
    }

    /** 显示文件 */
    public static void dowloadFile(String file) {
        try {
            @Cleanup OutputStream outputStream = null;
            @Cleanup InputStream in = null;
            // 读取指定路径下面的文件
            in = new FileInputStream(file);
            outputStream = new BufferedOutputStream(ServletUtil.getResponse().getOutputStream());
            // 创建存放文件内容的数组
            byte[] buff = new byte[1024];
            // 所读取的内容使用n来接收
            int n;
            // 当没有读取完时,继续读取,循环
            while ((n = in.read(buff)) != -1) {
                // 将字节数组的数据全部写入到输出流中
                outputStream.write(buff, 0, n);
            }
            // 强制将缓存区的数据进行输出
            outputStream.flush();
        } catch (Exception e) {
            e.getMessage();
        }
    }
}
