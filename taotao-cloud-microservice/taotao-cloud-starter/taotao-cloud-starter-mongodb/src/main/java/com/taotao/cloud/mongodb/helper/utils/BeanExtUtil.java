package com.taotao.cloud.mongodb.helper.utils;

import cn.hutool.core.bean.BeanUtil;
import com.taotao.cloud.mongodb.helper.bean.Page;
import java.util.ArrayList;
import java.util.List;

public class BeanExtUtil {

	/**
	 * 根据List对象属性批量创建对应的Class List对象
	 *
	 * @param list
	 * @param clazz
	 * @return
	 */
	public static <T> List<T> copyListByProperties(List<?> list, Class<T> clazz) {
		if (list == null) {
			return null;
		}

		List<T> rsList = new ArrayList<>();
		for (Object source : list) {
			rsList.add((T) BeanUtil.copyProperties(source, clazz));
		}

		return rsList;
	}


	/**
	 * 根据PageResp对象属性批量创建对应的Class PageResp对象
	 *
	 * @param pageResp
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> Page<T> copyPageByProperties(Page<?> page, Class<T> clazz) {
		Page<T> pageNew = copyBeanByProperties(page, Page.class);
		pageNew.setList(copyListByProperties(page.getList(), clazz));
		return pageNew;
	}

	/**
	 * 按照Bean对象属性创建对应的Class对象
	 */
	public static <T> T copyBeanByProperties(Object source, Class<T> tClass) {
		return BeanUtil.copyProperties(source, tClass);
	}


}
