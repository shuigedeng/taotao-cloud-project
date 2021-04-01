package com.taotao.cloud.web.controller;

import com.taotao.cloud.common.utils.DateUtil;
import java.beans.PropertyEditorSupport;
import java.util.Date;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

/**
 * 基础控制类
 *
 * @author aristotle
 * @since 2020-06-01
 */
public class BaseController {

	/**
	 * 将前台传递过来的日期格式的字符串，自动转化为Date类型
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		// Date 类型转换
		binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				setValue(DateUtil.parseLocalDateTime(text, DateUtil.DATETIME_FORMATTER));
			}
		});
	}
}
