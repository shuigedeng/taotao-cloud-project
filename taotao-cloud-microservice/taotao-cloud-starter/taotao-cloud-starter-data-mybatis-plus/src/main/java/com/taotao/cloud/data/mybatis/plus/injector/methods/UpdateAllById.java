package com.taotao.cloud.data.mybatis.plus.injector.methods;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.extension.injector.methods.AlwaysUpdateSomeColumnById;

import java.util.function.Predicate;

/**
 * 修改所有的字段
 */
public class UpdateAllById extends AlwaysUpdateSomeColumnById {

	public UpdateAllById(Predicate<TableFieldInfo> predicate) {
		super(predicate);
	}

	@Override
	public String getMethod(SqlMethod sqlMethod) {
		// 自定义 mapper 方法名
		return "updateAllById";
	}
}
