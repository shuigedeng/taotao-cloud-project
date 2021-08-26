/**
 * Copyright &copy; 2016-2020 公众学业 All rights reserved.
 */
package com.taotao.cloud.opencv.biz.common.web;

import com.acts.opencv.common.mapper.JsonMapper;
import com.acts.opencv.common.utils.Constants;
import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 控制器支持类
 * 创建者	Songer
 * 创建时间	2016年7月21日
 *
 */
public abstract class BaseController{

	/**
	 * 添加Model消息
	 * @param message
	 */
	protected void addMessage(Model model, String... messages) {
		StringBuilder sb = new StringBuilder();
		for (String message : messages){
			sb.append(message).append(messages.length>1?"<br/>":"");
		}
		model.addAttribute("message", sb.toString());
	}

	/**
	 * 添加Flash消息
	 * @param message
	 */
	protected void addMessage(RedirectAttributes redirectAttributes, String... messages) {
		StringBuilder sb = new StringBuilder();
		for (String message : messages){
			sb.append(message).append(messages.length>1?"<br/>":"");
		}
		redirectAttributes.addFlashAttribute("message", sb.toString());
	}


	/**
	 * 客户端返回JSON字符串
	 * @param response
	 * @param string
	 * @return
	 */
	protected void renderString(HttpServletResponse response, Object object) {
		try {
			response.reset();
			response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
			response.setHeader("Cache-Control", "no-cache, must-revalidate");
			PrintWriter writer = response.getWriter();
			writer.write(JsonMapper.toJsonString(object));
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 客户端返回图片类型
	 * @param response
	 * @param object void
	 * @throws IOException
	 * @Date 2018年3月13日
	 * 更新日志
	 * 2018年3月13日 Songer  首次创建
	 *
	 */
	protected void renderImage(HttpServletResponse response, byte[] object) {
		try {
			response.reset();
			response.setContentType("image/*");
			ServletOutputStream output = response.getOutputStream();
			output.flush();
			output.write(object);
			output.close();
			// ServletOutputStream output = response.getOutputStream();
			// FileInputStream fis = new FileInputStream("E:\\tomcat7\\webapps\\java_opencv\\statics\\distimage\\lena.png");
			// byte[] buffer = new byte[1024];
			// int i = -1;
			// while ((i = fis.read(buffer)) != -1) {
			// output.write(buffer, 0, i);
			// }
			// output.flush();
			// output.close();
			// fis.close();
		} catch (IOException e) {
			// 如果是ClientAbortException异常，可以不用管，原因是页面参数变化太快，response请求被中断
			try {
				// response.reset();
				PrintWriter writer = response.getWriter();
				response.setContentType("text/html;charset=utf-8");
				writer.write("无法打开图片!");
				writer.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	/**
	 * 客户端返回字符串
	 * @param response
	 * @param string
	 * @return
	 */
	protected void renderString(HttpServletResponse response) {
		 renderString(response, Constants.SUCCESS);
	}

	/**
	 * 初始化数据绑定
	 * 1. 将所有传递进来的String进行HTML编码，防止XSS攻击
	 * 2. 将字段中Date类型转换为String类型
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		// String类型转换，将所有传递进来的String进行HTML编码，防止XSS攻击
		binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				setValue(text == null ? null : StringEscapeUtils.escapeHtml4(text.trim()));
			}
			@Override
			public String getAsText() {
				Object value = getValue();
				return value != null ? value.toString() : "";
			}
		});
	}

}
