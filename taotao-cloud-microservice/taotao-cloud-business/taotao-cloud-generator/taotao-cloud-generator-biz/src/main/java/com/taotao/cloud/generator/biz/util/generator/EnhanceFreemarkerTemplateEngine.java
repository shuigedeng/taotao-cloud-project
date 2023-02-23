package com.taotao.cloud.generator.biz.util.generator;

import com.baomidou.mybatisplus.generator.config.builder.CustomFile;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import java.util.List;
import java.util.Map;

/**
 * 代码生成器支持自定义[DTO\VO等]模版
 */
public class EnhanceFreemarkerTemplateEngine extends FreemarkerTemplateEngine {

	@Override
	protected void outputCustomFile(List<CustomFile> customFiles,
		TableInfo tableInfo, Map<String, Object> objectMap) {
		//String entityName = tableInfo.getEntityName();
		//String otherPath = this.getPathInfo(OutputFile.other);
		//customFile.forEach((key, value) -> {
		//	String fileName = String.format(otherPath + File.separator + entityName + "%s", key);
		//	this.outputFile(new File(fileName), objectMap, value, true);
		//});

		super.outputCustomFile(customFiles, tableInfo, objectMap);

	}
}
