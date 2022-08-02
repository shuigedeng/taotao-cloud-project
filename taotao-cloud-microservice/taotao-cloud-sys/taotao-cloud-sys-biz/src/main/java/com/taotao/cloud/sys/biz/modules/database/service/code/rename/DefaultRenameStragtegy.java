package com.taotao.cloud.sys.biz.modules.database.service.code.rename;

import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;
import com.sanri.tools.modules.database.service.code.dtos.JavaBeanInfo;
import com.sanri.tools.modules.database.service.dtos.meta.TableMetaData;
import com.sanri.tools.modules.database.service.meta.dtos.Column;
import com.sanri.tools.modules.database.service.meta.dtos.PrimaryKey;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.math.BigDecimal;
import java.sql.JDBCType;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DefaultRenameStragtegy implements RenameStrategy {
	private Converter<String, String> propertyConverter = CaseFormat.LOWER_UNDERSCORE
			.converterTo(CaseFormat.LOWER_CAMEL);
	private Converter<String, String> classNameConverter = CaseFormat.LOWER_UNDERSCORE
			.converterTo(CaseFormat.UPPER_CAMEL);

	public static MultiValueMap<Class<?>, JDBCType> multiValueMap = new LinkedMultiValueMap<>();
	static {
		multiValueMap.put(String.class, Arrays.asList(JDBCType.CHAR, JDBCType.VARCHAR, JDBCType.LONGVARCHAR,
				JDBCType.NVARCHAR, JDBCType.NCHAR));
		multiValueMap.put(Date.class, Arrays.asList(JDBCType.TIMESTAMP, JDBCType.DATE));
		multiValueMap.put(Integer.class, Arrays.asList(JDBCType.INTEGER, JDBCType.SMALLINT, JDBCType.TINYINT));
		multiValueMap.put(Double.class, Arrays.asList(JDBCType.DECIMAL, JDBCType.NUMERIC));
	}

	@Override
	public JavaBeanInfo mapping(TableMetaData tableMetaData) {
		String tableName = tableMetaData.getActualTableName().getTableName();
		String className = classNameConverter.convert(tableName);
		JavaBeanInfo javaBeanInfo = new JavaBeanInfo(className);
		List<Column> columns = tableMetaData.getColumns();
		List<PrimaryKey> primaryKeys = tableMetaData.getPrimaryKeys();
		//        Map<String, List<String>> pkColumnNameMap = primaryKeys.stream().collect(Collectors.groupingBy(PrimaryKey::getPkName, Collectors.mapping(PrimaryKey::getColumnName, Collectors.toList())));
		List<String> primaryKeyColumns = primaryKeys.stream().map(PrimaryKey::getColumnName)
				.collect(Collectors.toList());

		for (Column column : columns) {
			String columnName = column.getColumnName();
			boolean isKey = primaryKeyColumns.contains(columnName);
			String fieldName = propertyConverter.convert(columnName);

			// 获取 java 类型
			int dataType = column.getDataType();
			int columnSize = column.getColumnSize();
			int decimalDigits = column.getDecimalDigits();
			String remark = column.getRemark();
			JDBCType jdbcType = JDBCType.valueOf(dataType);
			if (multiValueMap.get(String.class).contains(jdbcType)) {
				if (jdbcType == JDBCType.CHAR && columnSize == 1) {
					javaBeanInfo.addField(new JavaBeanInfo.BeanField(column, Character.class.getSimpleName(), fieldName,
							remark, isKey));
					continue;
				}
				javaBeanInfo.addField(new JavaBeanInfo.BeanField(column, "String", fieldName, remark, isKey));
				continue;
			}
			if (multiValueMap.get(Date.class).contains(jdbcType)) {
				javaBeanInfo.addImport("java.util.Date");
				javaBeanInfo.addField(new JavaBeanInfo.BeanField(column, "Date", fieldName, remark, isKey));
				continue;
			}
			if (multiValueMap.get(Integer.class).contains(jdbcType)) {
				// smallint , tinyint , integer 都认为是 int
				javaBeanInfo.addField(new JavaBeanInfo.BeanField(column, "Integer", fieldName, remark, isKey));
				continue;
			}
			if (jdbcType == JDBCType.BIGINT) {
				javaBeanInfo.addField(new JavaBeanInfo.BeanField(column, "Long", fieldName, remark, isKey));
				continue;
			}
			if (multiValueMap.get(Double.class).contains(jdbcType)) {
				javaBeanInfo.addImport(BigDecimal.class.getName());
				javaBeanInfo.addField(new JavaBeanInfo.BeanField(column, "BigDecimal", fieldName, remark, isKey));
				continue;
			}

		}
		return javaBeanInfo;
	}
}
