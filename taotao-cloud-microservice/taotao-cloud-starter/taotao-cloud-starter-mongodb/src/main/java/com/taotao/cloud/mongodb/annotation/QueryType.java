package com.taotao.cloud.mongodb.annotation;

import java.lang.reflect.Field;
import java.util.List;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.util.StringUtils;

/**
 * 查询媒介 1. equals：相等 2. like:mongodb的like查询 3. in:用于列表的in类型查询
 *
 * @author shuigedeng
 * @version 2022.05
 * @since 2022-05-27 21:49:36
 */
public enum QueryType {
	/**
	 * 相等
	 */
	EQUALS {
		@Override
		public Criteria buildCriteria(QueryField queryFieldAnnotation, Field field, Object value) {
			if (check(queryFieldAnnotation, field, value)) {
				String queryField = getQueryFieldName(queryFieldAnnotation, field);
				return Criteria.where(queryField).is(value.toString());
			}
			return new Criteria();
		}
	},
	/**
	 * mongodb的like查询
	 */
	LIKE {
		@Override
		public Criteria buildCriteria(QueryField queryFieldAnnotation, Field field, Object value) {
			if (check(queryFieldAnnotation, field, value)) {
				String queryField = getQueryFieldName(queryFieldAnnotation, field);
				return Criteria.where(queryField).regex(value.toString());
			}
			return new Criteria();
		}
	},
	/**
	 * 用于列表的in类型查询
	 */
	IN {
		@Override
		public Criteria buildCriteria(QueryField queryFieldAnnotation, Field field, Object value) {
			if (check(queryFieldAnnotation, field, value)) {
				if (value instanceof List) {
					String queryField = getQueryFieldName(queryFieldAnnotation, field);
					// 此处必须转型为List，否则会在in外面多一层[]
					return Criteria.where(queryField).in((List<?>) value);
				}
			}
			return new Criteria();
		}
	};

	/**
	 * 检查
	 *
	 * @param queryField 查询字段
	 * @param field      场
	 * @param value      价值
	 * @return boolean
	 * @since 2022-05-27 21:49:37
	 */
	private static boolean check(QueryField queryField, Field field, Object value) {
		return !(queryField == null || field == null || value == null);
	}

	/**
	 * 建立标准
	 *
	 * @param queryFieldAnnotation 查询字段注释
	 * @param field                场
	 * @param value                价值
	 * @return {@link Criteria }
	 * @since 2022-05-27 21:49:37
	 */
	public abstract Criteria buildCriteria(QueryField queryFieldAnnotation, Field field,
		Object value);


	/**
	 * 如果实体bean的字段上QueryField注解没有设置attribute属性时，默认为该字段的名称
	 *
	 * @param queryField 查询字段
	 * @param field
	 * @return {@link String }
	 * @since 2022-05-27 21:49:37
	 */
	private static String getQueryFieldName(QueryField queryField, Field field) {
		String queryFieldValue = queryField.attribute();
		if (!StringUtils.hasText(queryFieldValue)) {
			queryFieldValue = field.getName();
		}
		return queryFieldValue;
	}
}
