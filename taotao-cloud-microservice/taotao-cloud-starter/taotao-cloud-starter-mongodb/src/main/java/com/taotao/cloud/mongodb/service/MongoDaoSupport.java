package com.taotao.cloud.mongodb.service;

import com.taotao.cloud.common.model.PageModel;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.mongodb.annotation.QueryField;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Set;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

/**
 * MongoDB通用Dao抽象实现
 *
 * @param <T>
 * @date 2020-10-20
 */
public class MongoDaoSupport implements BaseMongoDAO {

	@Autowired
	@Qualifier("mongoTemplate")
	private MongoTemplate mongoTemplate;

	/**
	 * 保存一个对象到mongodb
	 *
	 * @param bean
	 * @return
	 */
	@Override
	public <T> T save(T bean) {
		mongoTemplate.save(bean);
		return bean;
	}

	/**
	 * 根据id删除对象
	 *
	 * @param t
	 */
	@Override
	public <T> void deleteById(T t) {
		mongoTemplate.remove(t);
	}


	/**
	 * 根据对象的属性删除
	 *
	 * @param t
	 */
	@Override
	public <T> void deleteByCondition(T t) {
		Query query = buildBaseQuery(t);
		mongoTemplate.remove(query, getEntityClass());
	}

	/**
	 * 根据id进行更新
	 *
	 * @param id
	 * @param t
	 */
	@Override
	public <T> void updateById(String id, T t) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(id));
		Update update = buildBaseUpdate(t);
		update(query, update);
	}

	/**
	 * 根据对象的属性查询
	 *
	 * @param t
	 * @return
	 */
	@Override
	public <T> List<T> findByCondition(T t) {
		Query query = buildBaseQuery(t);
		return mongoTemplate.find(query, getEntityClass());
	}

	/**
	 * 通过条件查询实体(集合)
	 *
	 * @param query
	 * @return
	 */
	@Override
	public <T> List<T> find(Query query) {
		return mongoTemplate.find(query, this.getEntityClass());
	}

	/**
	 * 通过一定的条件查询一个实体
	 *
	 * @param query
	 * @return
	 */
	@Override
	public <T> T findOne(Query query) {
		return mongoTemplate.findOne(query, this.getEntityClass());
	}

	/**
	 * 通过条件查询更新数据
	 *
	 * @param query
	 * @param update
	 */
	@Override
	public void update(Query query, Update update) {
		mongoTemplate.updateMulti(query, update, this.getEntityClass());
	}

	/**
	 * 通过ID获取记录
	 *
	 * @param id
	 * @return
	 */
	@Override
	public <T> T findById(String id) {
		return mongoTemplate.findById(id, this.getEntityClass());
	}

	/**
	 * 通过ID获取记录,并且指定了集合名(表的意思)
	 *
	 * @param id
	 * @param collectionName
	 */
	@Override
	public T findById(String id, String collectionName) {
		Set<String> collectionNames = mongoTemplate.getCollectionNames();
		return mongoTemplate.findById(id, this.getEntityClass(), collectionName);
	}

	@Override
	public Set<String> getCollectionNames() {
		Set<String> collectionNames = mongoTemplate.getCollectionNames();
		LogUtil.info(collectionNames.toString());
		return collectionNames;
	}

	/**
	 * 通过条件查询,查询分页结果
	 *
	 * @param page
	 * @param query
	 */
	@Override
	public <T> PageModel<T> findPage(PageModel<T> page, Query query) {
		//如果没有条件 则所有全部
		query = query == null ? new Query(Criteria.where("_id").exists(true)) : query;
		long count = this.count(query);

		query.skip((long) (page.currentPage() - 1) * page.pageSize()).limit(page.pageSize());
		List<T> rows = this.find(query);

		int divisor = (int) count / page.pageSize();
		int remainder = (int) count % page.pageSize();

		return new PageModel<>(count, remainder == 0 ? divisor == 0 ? 1 : divisor : divisor + 1,
			page.currentPage(), page.pageSize(), rows);
	}

	/**
	 * 求数据总和
	 *
	 * @param query
	 */
	@Override
	public long count(Query query) {
		return mongoTemplate.count(query, this.getEntityClass());
	}

	/**
	 * 根据vo构建查询条件Query
	 *
	 * @param t
	 */
	private <T> Query buildBaseQuery(T t) {
		Query query = new Query();

		Field[] fields = t.getClass().getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			try {
				Object value = field.get(t);
				if (value != null) {
					QueryField queryField = field.getAnnotation(QueryField.class);
					if (queryField != null) {
						query.addCriteria(
							queryField.type().buildCriteria(queryField, field, value));
					}
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return query;
	}

	/**
	 * 根据vo构建更新条件Query
	 *
	 * @param t
	 */
	private <T> Update buildBaseUpdate(T t) {
		Update update = new Update();

		Field[] fields = t.getClass().getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			try {
				Object value = field.get(t);
				if (value != null) {
					update.set(field.getName(), value);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return update;
	}

	/**
	 * 获取需要操作的实体类class
	 */
	protected <T> Class<T> getEntityClass() {
		return ((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
	}

	/**
	 * 获取MongoDB模板操作
	 *
	 * @return MongoTemplate
	 */
	@Override
	public MongoTemplate getMongoTemplate() {
		return mongoTemplate;
	}

}
