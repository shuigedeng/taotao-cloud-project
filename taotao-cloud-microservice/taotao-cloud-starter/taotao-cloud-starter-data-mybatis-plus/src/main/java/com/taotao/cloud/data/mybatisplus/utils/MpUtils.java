/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.data.mybatisplus.utils;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import com.taotao.cloud.common.utils.context.ContextUtils;
import com.taotao.cloud.common.utils.log.LogUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

/**
 * MybatisBatchUtil
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2022/03/24 14:18
 */
public class MpUtils {

	/**
	 * 默认每次处理1000条
	 */
	private static final int BATCH_SIZE = 1000;

	/**
	 * 批量处理修改或者插入
	 *
	 * @param data        处理的数据
	 * @param mapperClass mapper类
	 * @param function    function 处理逻辑
	 * @return 影响的总行数
	 * @since 2022-03-24 14:29:12
	 */
	public <T, U, R> int batchUpdateOrInsert(List<T> data, Class<U> mapperClass,
		BiFunction<T, U, R> function) {
		int i = 1;
		SqlSessionFactory sqlSessionFactory = ContextUtils.getBean(SqlSessionFactory.class, false);
		if (Objects.isNull(sqlSessionFactory)) {
			throw new MybatisPlusException("未获取到sqlSession");
		}

		SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);
		try {
			U mapper = sqlSession.getMapper(mapperClass);
			int size = data.size();
			for (T element : data) {
				function.apply(element, mapper);
				if ((i % BATCH_SIZE == 0) || i == size) {
					sqlSession.flushStatements();
				}
				i++;
			}
		} catch (Exception e) {
			sqlSession.rollback();
			LogUtils.error(e);
		} finally {
			sqlSession.close();
		}

		return i - 1;
	}



	///**
	// * mp page转换为 PageResult 同时进行dto转换
	// */
	//public static <T> PageResult<T> convert2DtoPageResult(Page<? extends EntityBaseFunction<T>> page){
	//	if (Objects.isNull(page)){
	//		return new PageResult<>();
	//	}
	//	List<T> collect = page.getRecords()
	//		.stream()
	//		.map(EntityBaseFunction::toDto)
	//		.collect(Collectors.toList());
	//	// 构造 PageResult 对象
	//	return new PageResult<T>()
	//		.setSize(page.getSize())
	//		.setCurrent(page.getCurrent())
	//		.setTotal(page.getTotal())
	//		.setRecords(collect);
	//}
	//
	///**
	// * page转换为 PageResult
	// */
	//public static <T> PageResult<T> convert2PageResult(Page<T> page){
	//	if (Objects.isNull(page)){
	//		return new PageResult<>();
	//	}
	//	// 构造 PageResult 对象
	//	return new PageResult<T>()
	//		.setSize(page.getSize())
	//		.setCurrent(page.getCurrent())
	//		.setTotal(page.getTotal())
	//		.setRecords(page.getRecords());
	//}
	//
	///**
	// * 获取分页对象 MyBatis-Plus
	// */
	//public static <T> Page<T> getMpPage(PageParam page, Class<T> clazz){
	//	return Page.of(page.getCurrent(),page.getSize());
	//}
	//
	///**
	// * 获取行名称
	// * @param function Lambda表达式
	// * @return 字段名
	// */
	//public static <T> String getColumnName(SFunction<T,?> function){
	//	LambdaMeta meta = LambdaUtils.extract(function);
	//	Map<String, ColumnCache> columnMap = LambdaUtils.getColumnMap(meta.getInstantiatedClass());
	//	Assert.notEmpty(columnMap, "错误:无法执行.因为无法获取到实体类的表对应缓存!");
	//	String fieldName = PropertyNamer.methodToProperty(meta.getImplMethodName());
	//	ColumnCache columnCache = columnMap.get(LambdaUtils.formatKey(fieldName));
	//	return columnCache.getColumn();
	//}
	//
	///**
	// * 批量执行语句, 通常用于for循环方式的批量插入
	// */
	//public static <T> void executeBatch(List<T> saveList, Consumer<List<T>> consumer,int batchSize){
	//	// 开始游标
	//	int start = 0;
	//	// 结束游标
	//	int end = Math.min(batchSize, saveList.size());
	//	while (start < end){
	//		List<T> list = ListUtil.sub(saveList, start, end);
	//		start = end;
	//		end = Math.min(end + batchSize, saveList.size());
	//		consumer.accept(list);
	//	}
	//}
	//
	///**
	// * 初始化数据库Entity
	// * @param entityList 对象列表
	// * @param userId 用户id
	// * @param <T> 泛型 MpIdEntity
	// */
	//public static <T extends MpIdEntity> void initEntityList(List<? extends MpIdEntity> entityList,Long userId){
	//	for (MpIdEntity t : entityList) {
	//		// 设置id
	//		t.setId(IdUtil.getSnowflakeNextId());
	//		if (t instanceof MpBaseEntity){
	//			MpBaseEntity entity = (MpBaseEntity) t;
	//			entity.setCreator(userId);
	//			entity.setCreateTime(LocalDateTime.now());
	//			entity.setLastModifier(userId);
	//			entity.setLastModifiedTime(LocalDateTime.now());
	//			entity.setVersion(0);
	//		}
	//	}
	//}
	//
	///**
	// * 字段存在长文本注解则在查询时被排除
	// */
	//public static boolean excludeBigField(TableFieldInfo tableFieldInfo) {
	//	BigField annotation = tableFieldInfo.getField().getAnnotation(BigField.class);
	//	return Objects.isNull(annotation);
	//}
	//
	///**
	// * 获取最新的一条
	// */
	//public static  <T> Optional<T> findOne(LambdaQueryChainWrapper<T> lambdaQuery){
	//	Page<T> mpPage = new Page<>(0,1);
	//	Page<T> page = lambdaQuery.page(mpPage);
	//	if (page.getTotal() > 0) {
	//		return Optional.of(page.getRecords().get(0));
	//	}
	//	return Optional.empty();
	//}

	private static final String MYSQL_ESCAPE_CHARACTER = "`";

	/**
	 * 将拦截器添加到链中
	 * @param interceptor 链
	 * @param inner 拦截器
	 * @param index 位置
	 */
	public static void addInterceptor(MybatisPlusInterceptor interceptor, InnerInterceptor inner, int index) {
		List<InnerInterceptor> inners = new ArrayList<>(interceptor.getInterceptors());
		inners.add(index, inner);
		interceptor.setInterceptors(inners);
	}

	/**
	 * 获得 Table 对应的表名
	 * <p>
	 * 兼容 MySQL 转义表名 `t_xxx`
	 * @param table 表
	 * @return 去除转移字符后的表名
	 */
	public static String getTableName(Table table) {
		String tableName = table.getName();
		if (tableName.startsWith(MYSQL_ESCAPE_CHARACTER) && tableName.endsWith(MYSQL_ESCAPE_CHARACTER)) {
			tableName = tableName.substring(1, tableName.length() - 1);
		}
		return tableName;
	}

	/**
	 * 构建 Column 对象
	 * @param tableName 表名
	 * @param tableAlias 别名
	 * @param column 字段名
	 * @return Column 对象
	 */
	public static Column buildColumn(String tableName, Alias tableAlias, String column) {
		return new Column(tableAlias != null ? tableAlias.getName() + "." + column : column);
	}
}
