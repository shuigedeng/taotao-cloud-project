package com.taotao.cloud.data.mybatis.plus.pagehelper;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.lang.StringUtil;
import org.springframework.web.bind.annotation.PostMapping;

public class SQLFilter {

	public static String sqlInject(String str) {
		if (StringUtil.isBlank(str)) {
			return null;
		}        // 去掉'|"|;|\字符
		str = StringUtil.replace(str, "'", "");
		str = StringUtil.replace(str, "\"", "");
		str = StringUtil.replace(str, ";", "");
		str = StringUtil.replace(str, "\\",
			"");        // 转换成小写
		str = str.toLowerCase();        // 非法字符
		String[] keywords = {"master", "truncate", "insert", "select", "delete", "update",
			"declare", "alert",
			"drop"};        // 判断是否包含非法字符
		for (String keyword : keywords) {
			if (str.indexOf(keyword) != -1) {
				throw new RuntimeException("包含非法字符");
			}
		}
		return str;
	}

	//@PostMapping("getPageList")
	//public Result getPageList(@RequestBody PageParam<TUser> pageParm) {       //接收参数
	//	PagedList<TUser> pl = PageUtils.exportPagedList(pageParm);
	//	return Result.success(userService.queryPageList(pl, pageParm.getParam()));
	//}
	//
	//public PagedList<TUser> queryPageList(PagedList<TUser> page, TUser user) {
	//	PageInfo<TUser> pageInfo = PageHelper.startPage(page).doSelectPageInfo(
	//		() -> list(user));       //转换结果
	//	return PageUtils.toPageList(pageInfo);
	//}

}
