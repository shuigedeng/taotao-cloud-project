package com.taotao.cloud.mongodb.service;

import com.taotao.cloud.common.model.PageModel;
import java.util.List;
import java.util.Set;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

/**
 * 基地mongo刀
 *
 * @author shuigedeng
 * @version 2022.05
 * @since 2022-05-27 21:47:01
 */
public interface BaseMongoDAO {

	/**
	 * 保存一个对象到mongodb
	 *
	 * @param entity
	 */
	<T> T save(T entity);

	/**
	 * 根据id删除对象
	 *
	 * @param t
	 */
	<T> void deleteById(T t);

	/**
	 * 根据对象的属性删除
	 *
	 * @param t
	 */
	<T> void deleteByCondition(T t);


	/**
	 * 根据id进行更新
	 *
	 * @param id
	 * @param t
	 */
	<T> void updateById(String id, T t);


	/**
	 * 根据对象的属性查询
	 *
	 * @param t
	 * @return {@link List }<{@link T }>
	 * @since 2022-05-27 21:46:03
	 */
	<T> List<T> findByCondition(T t);


	/**
	 * 通过条件查询实体(集合)
	 *
	 * @param query
	 */
	<T> List<T> find(Query query);

	/**
	 * 通过一定的条件查询一个实体
	 *
	 * @param query
	 * @return {@link T }
	 * @since 2022-05-27 21:45:57
	 */
	<T> T findOne(Query query);

	/**
	 * 通过条件查询更新数据
	 *
	 * @param query
	 * @param update
	 * @since 2022-05-27 21:45:55
	 */
	void update(Query query, Update update);

	/**
	 * 通过ID获取记录
	 *
	 * @param id
	 * @return
	 */
	<T> T findById(String id);

	/**
	 * 通过ID获取记录,并且指定了集合名(表的意思)
	 *
	 * @param id
	 * @param collectionName 集合名
	 */
	<T> T findById(String id, String collectionName);

	/**
	 * 通过条件查询,查询分页结果
	 *
	 * @param page
	 * @param query
	 */
	<T> PageModel<T> findPage(PageModel<T> page, Query query);

	/**
	 * 求数据总和
	 *
	 * @param query
	 */
	long count(Query query);

	/**
	 * 获取MongoDB模板操作
	 *
	 * @return {@link MongoTemplate }
	 * @since 2022-05-27 21:45:49
	 */
	MongoTemplate getMongoTemplate();

	/**
	 * 把收集名字
	 *
	 * @return {@link Set }<{@link String }>
	 * @since 2022-05-27 21:44:38
	 */
	Set<String> getCollectionNames();
}
