package com.taotao.cloud.generator.mbg;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.internal.DefaultCommentGenerator;

public class MySQLCommentGenerator extends DefaultCommentGenerator {

	private final Properties properties;

	public MySQLCommentGenerator() {
		properties = new Properties();
	}

	@Override
	public void addConfigurationProperties(Properties properties) {
	// 获取自定义的 properties
		this.properties.putAll(properties);
	}

	/**
	 * 重写给实体类加的注释
	 */
	@Override
	public void addModelClassComment(TopLevelClass topLevelClass,
		IntrospectedTable introspectedTable) {
		String author = properties.getProperty("author");
		String dateFormat = properties.getProperty("dateFormat", "yyyy-MM-dd");
		SimpleDateFormat dateFormatter = new SimpleDateFormat(dateFormat);
// 获取表注释
		String remarks = introspectedTable.getRemarks();
		topLevelClass.addJavaDocLine("/**");
		topLevelClass.addJavaDocLine(" * " + remarks);
		topLevelClass.addJavaDocLine(" *");
		topLevelClass.addJavaDocLine(" * @author " + author);
		topLevelClass.addJavaDocLine(" * @date " + dateFormatter.format(new Date()));
		topLevelClass.addJavaDocLine(" */");
	}

	/**
	 * 重写给实体类字段加的注释
	 */
	@Override
	public void addFieldComment(Field field, IntrospectedTable introspectedTable,
		IntrospectedColumn introspectedColumn) {
// 获取列注释
		String remarks = introspectedColumn.getRemarks();
		field.addJavaDocLine("/**");
		field.addJavaDocLine(" * " + remarks);
		field.addJavaDocLine(" */");
	}

	/**
	 * 重写给实体类get方法加的注释
	 */
	@Override
	public void addGetterComment(Method method, IntrospectedTable introspectedTable,
		IntrospectedColumn introspectedColumn) {
// 获取表注释
		String remarks = introspectedColumn.getRemarks();
		method.addJavaDocLine("/**");
		method.addJavaDocLine(" * " + method.getName());
		method.addJavaDocLine(" */");
	}

}
