package com.taotao.cloud.web.utils;

import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.taotao.cloud.common.utils.servlet.RequestUtils;
import org.apache.poi.ss.usermodel.Workbook;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 了实效
 *
 * @author shuigedeng
 * @version 2022.05
 * @since 2022-05-16 15:00:41
 */
public class DownUtil {

	/**
	 * 下载excel
	 *
	 * @param fileName excel名称
	 * @param workbook
	 */
	public static void dowloadExcel(Workbook workbook, String fileName) {
		try {
			HttpServletResponse response = RequestUtils.getResponse();
			response.setCharacterEncoding(Constants.UTF_8);
			response.setHeader("content-Type", "application/vnd.ms-excel");
			response.setHeader("Content-Disposition",
				"attachment;filename=" + URLEncoder.encode(fileName, Constants.UTF_8));
			ServletOutputStream outputStream = response.getOutputStream();
			workbook.write(outputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 下载文件
	 *
	 * @param file     文件
	 * @param fileName 订单信息.pdf
	 */
	public static void dowloadFile(File file, String fileName) {
		HttpServletResponse response = RequestUtils.getResponse();
		HttpServletRequest request = RequestUtils.getRequest();
		try {
			InputStream is = new FileInputStream(file);
			BufferedInputStream bis = new BufferedInputStream(is);
			response.setCharacterEncoding(Constants.UTF_8);
			response.setContentType("application/x-download");
			//编码的文件名字,关于中文乱码的改造
			String codeFileName = "";
			String agent = request.getHeader("USER-AGENT").toLowerCase();
			if (agent.contains("msie") || agent.contains("trident")) {
				//IE
				codeFileName = URLEncoder.encode(fileName, Constants.UTF_8);
			} else if (agent.contains("mozilla")) {
				//火狐，谷歌
				codeFileName = new String(fileName.getBytes(StandardCharsets.UTF_8), "iso-8859-1");
			} else {
				codeFileName = URLEncoder.encode(fileName, Constants.UTF_8);
			}
			response.setHeader("Content-Disposition",
				"attachment;filename=\"" + codeFileName + "\"");
			OutputStream os = response.getOutputStream();
			int i;
			byte[] buff = new byte[1024 * 8];
			while ((i = bis.read(buff)) != -1) {
				os.write(buff, 0, i);
			}
			os.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 下载文件
	 *
	 * @param paths    路径
	 * @param fileName 订单信息.pdf
	 */
	public static Boolean dowloadFile(String paths, String fileName) {
		HttpServletResponse response = RequestUtils.getResponse();
		HttpServletRequest request = RequestUtils.getRequest();
		try {
			InputStream is = new FileInputStream(paths);
			BufferedInputStream bis = new BufferedInputStream(is);
			response.setCharacterEncoding(Constants.UTF_8);
			response.setContentType("text/plain");
			if (fileName.contains(".svg")) {
				response.setContentType("image/svg+xml");
			}
			//编码的文件名字,关于中文乱码的改造
			String codeFileName = "";
			String agent = request.getHeader("USER-AGENT").toLowerCase();
			if (agent.contains("msie") || agent.contains("trident")) {
				//IE
				codeFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
			} else if (agent.contains("mozilla")) {
				//火狐，谷歌
				codeFileName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
			} else {
				codeFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
			}
			response.setHeader("Content-Disposition",
				"attachment;filename=" + new String(codeFileName.getBytes(), StandardCharsets.UTF_8));
			OutputStream os = response.getOutputStream();
			int i;
			byte[] buff = new byte[1024 * 8];
			while ((i = bis.read(buff)) != -1) {
				os.write(buff, 0, i);
			}
			os.flush();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 流返回界面
	 */
	public static void write(BufferedImage image) {
		try {
			HttpServletResponse response = DownUtil.getResponse();
			ServletOutputStream outputStream = response.getOutputStream();
			//将内存中的图片通过流动形式输出到客户端
			ImageIO.write(image, "PNG", outputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 设置img的response
	 */
	public static HttpServletResponse getResponse() {
		HttpServletResponse response = RequestUtils.getResponse();
		response.setCharacterEncoding(Constants.UTF_8);
		//设置相应类型,告诉浏览器输出的内容为图片
		response.setContentType("image/jpeg");
		//设置响应头信息，告诉浏览器不要缓存此内容
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expire", 0);
		return response;
	}
}
