package com.taotao.cloud.data.mongodb.helper.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.data.mongodb.helper.config.Constant;
import com.taotao.cloud.data.mongodb.helper.reflection.ReflectionUtil;
import com.taotao.cloud.data.mongodb.helper.reflection.SerializableFunction;
import com.taotao.cloud.data.mongodb.helper.bean.CreateTime;
import com.taotao.cloud.data.mongodb.helper.bean.InitValue;
import com.taotao.cloud.data.mongodb.helper.bean.Page;
import com.taotao.cloud.data.mongodb.helper.bean.SlowQuery;
import com.taotao.cloud.data.mongodb.helper.bean.SortBuilder;
import com.taotao.cloud.data.mongodb.helper.bean.UpdateBuilder;
import com.taotao.cloud.data.mongodb.helper.bean.UpdateTime;
import com.taotao.cloud.data.mongodb.properties.MongodbProperties;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.convert.QueryMapper;
import org.springframework.data.mongodb.core.convert.UpdateMapper;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import org.springframework.data.mongodb.core.query.Query;

/**
 * mongodb操作器
 *
 * @author shuigedeng
 * @version 2022.05
 * @since 2022-05-27 21:55:12
 */
public class MongoHelper {

	@Autowired
	protected MongoConverter mongoConverter;

	@Autowired
	private MongodbProperties mongodbProperties;

	protected QueryMapper queryMapper;
	protected UpdateMapper updateMapper;

	@Autowired
	protected MongoTemplate mongoTemplate;

	/**
	 * 得到mongo模板
	 *
	 * @return {@link MongoTemplate }
	 * @since 2022-05-27 21:55:12
	 */
	public MongoTemplate getMongoTemplate() {
		return mongoTemplate;
	}

	/**
	 * 初始化
	 *
	 * @since 2022-05-27 21:55:12
	 */
	@PostConstruct
	public void init() {
		queryMapper = new QueryMapper(mongoConverter);
		updateMapper = new UpdateMapper(mongoConverter);
	}

	/**
	 * 插入慢查询
	 *
	 * @param log       日志
	 * @param queryTime 查询时间
	 * @since 2022-05-27 21:55:12
	 */
	private void insertSlowQuery(String log, Long queryTime) {
		if (mongodbProperties.getSlowQuery()) {
			SlowQuery slowQuery = new SlowQuery();
			slowQuery.setQuery(log);
			slowQuery.setTime(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
			slowQuery.setQueryTime(queryTime);
			slowQuery.setSystem(SystemTool.getSystem());
			StackTraceElement[] stack = Thread.currentThread().getStackTrace();

			// 保存堆栈
			StringBuilder stackStr = new StringBuilder();
			for (StackTraceElement stackTraceElement : stack) {
				stackStr.append(stackTraceElement.getClassName())
					.append(".")
					.append(stackTraceElement.getMethodName())
					.append(":")
					.append(stackTraceElement.getLineNumber())
					.append("\n");
			}
			slowQuery.setStack(stackStr.toString());

			mongoTemplate.insert(slowQuery);
		}
	}

	/**
	 * 打印查询语句
	 *
	 * @param query
	 */
	private void logQuery(Class<?> clazz, Query query, Long startTime) {

		MongoPersistentEntity<?> entity = mongoConverter.getMappingContext()
			.getPersistentEntity(clazz);
		Document mappedQuery = queryMapper.getMappedObject(query.getQueryObject(), entity);
		Document mappedField = queryMapper.getMappedObject(query.getFieldsObject(), entity);
		Document mappedSort = queryMapper.getMappedObject(query.getSortObject(), entity);

		String log = "\ndb." + getCollectionName(clazz) + ".find(";

		log += FormatUtils.bson(mappedQuery.toJson()) + ")";

		if (!query.getFieldsObject().isEmpty()) {
			log += ".projection(";
			log += FormatUtils.bson(mappedField.toJson()) + ")";
		}

		if (query.isSorted()) {
			log += ".sort(";
			log += FormatUtils.bson(mappedSort.toJson()) + ")";
		}

		if (query.getLimit() != 0L) {
			log += ".limit(" + query.getLimit() + ")";
		}

		if (query.getSkip() != 0L) {
			log += ".skip(" + query.getSkip() + ")";
		}
		log += ";";

		// 记录慢查询
		Long queryTime = System.currentTimeMillis() - startTime;
		if (queryTime > mongodbProperties.getSlowTime()) {
			insertSlowQuery(log, queryTime);
		}
		if (mongodbProperties.getPrint()) {
			// 打印语句
			LogUtils.info(log + "\n执行时间:" + queryTime + "ms");
		}

	}

	private String getCollectionName(Class<?> clazz) {
		org.springframework.data.mongodb.core.mapping.Document document = clazz.getAnnotation(
			org.springframework.data.mongodb.core.mapping.Document.class);
		if (document != null) {
			if (StrUtil.isNotEmpty(document.value())) {
				return document.value();
			}
			if (StrUtil.isNotEmpty(document.collection())) {
				return document.collection();
			}
		}

		return StrUtil.lowerFirst(clazz.getSimpleName());
	}

	/**
	 * 打印查询语句
	 *
	 * @param query
	 */
	private void logCount(Class<?> clazz, Query query, Long startTime) {

		MongoPersistentEntity<?> entity = mongoConverter.getMappingContext()
			.getPersistentEntity(clazz);
		Document mappedQuery = queryMapper.getMappedObject(query.getQueryObject(), entity);

		String log = "\ndb." + StrUtil.lowerFirst(clazz.getSimpleName()) + ".find(";
		log += FormatUtils.bson(mappedQuery.toJson()) + ")";
		log += ".count();";

		// 记录慢查询
		Long queryTime = System.currentTimeMillis() - startTime;
		if (queryTime > mongodbProperties.getSlowTime()) {
			insertSlowQuery(log, queryTime);
		}
		if (mongodbProperties.getPrint()) {
			// 打印语句
			LogUtils.info(log + "\n执行时间:" + queryTime + "ms");
		}

	}

	/**
	 * 打印查询语句
	 *
	 * @param query
	 */
	private void logDelete(Class<?> clazz, Query query, Long startTime) {

		MongoPersistentEntity<?> entity = mongoConverter.getMappingContext()
			.getPersistentEntity(clazz);
		Document mappedQuery = queryMapper.getMappedObject(query.getQueryObject(), entity);

		String log = "\ndb." + StrUtil.lowerFirst(clazz.getSimpleName()) + ".remove(";
		log += FormatUtils.bson(mappedQuery.toJson()) + ")";
		log += ";";

		// 记录慢查询
		Long queryTime = System.currentTimeMillis() - startTime;
		if (queryTime > mongodbProperties.getSlowTime()) {
			insertSlowQuery(log, queryTime);
		}
		if (mongodbProperties.getPrint()) {
			// 打印语句
			LogUtils.info(log + "\n执行时间:" + queryTime + "ms");
		}

	}

	/**
	 * 打印查询语句
	 *
	 * @param query
	 */
	private void logUpdate(Class<?> clazz, Query query, UpdateBuilder updateBuilder, boolean multi,
		Long startTime) {

		MongoPersistentEntity<?> entity = mongoConverter.getMappingContext()
			.getPersistentEntity(clazz);
		Document mappedQuery = queryMapper.getMappedObject(query.getQueryObject(), entity);
		Document mappedUpdate = updateMapper.getMappedObject(
			updateBuilder.toUpdate().getUpdateObject(), entity);

		String log = "\ndb." + StrUtil.lowerFirst(clazz.getSimpleName()) + ".update(";
		log += FormatUtils.bson(mappedQuery.toJson()) + ",";
		log += FormatUtils.bson(mappedUpdate.toJson()) + ",";
		log += FormatUtils.bson("{multi:" + multi + "})");
		log += ";";

		// 记录慢查询
		Long queryTime = System.currentTimeMillis() - startTime;
		if (queryTime > mongodbProperties.getSlowTime()) {
			insertSlowQuery(log, queryTime);
		}
		if (mongodbProperties.getPrint()) {
			// 打印语句
			LogUtils.info(log + "\n执行时间:" + queryTime + "ms");
		}

	}

	/**
	 * 打印查询语句
	 *
	 * @param object
	 * @param startTime
	 * @param isInsert
	 */
	private void logSave(Object object, LocalDateTime startTime, Boolean isInsert) {
		JSONObject jsonObject = JSONUtil.parseObj(object);

		if (isInsert) {
			jsonObject.remove(Constant.ID);
		}

		String log = "\ndb." + StrUtil.lowerFirst(object.getClass().getSimpleName()) + ".save(";
		log += JSONUtil.toJsonPrettyStr(jsonObject);
		log += ");";

		// 记录慢查询
		Long queryTime = LocalDateTime.now().toInstant(ZoneOffset.ofHours(8)).toEpochMilli()
			- startTime.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
		if (queryTime > mongodbProperties.getSlowTime()) {
			insertSlowQuery(log, queryTime);
		}
		if (mongodbProperties.getPrint()) {
			// 打印语句
			LogUtils.info(log + "\n执行时间:" + queryTime + "ms");
		}
	}

	/**
	 * 打印查询语句
	 *
	 * @param list
	 * @param startTime
	 */
	private void logSave(List<?> list, LocalDateTime startTime) {
		List<JSONObject> cloneList = new ArrayList<>();
		for (Object item : list) {
			JSONObject jsonObject = JSONUtil.parseObj(item);

			jsonObject.remove(Constant.ID);
			cloneList.add(jsonObject);
		}

		Object object = list.get(0);
		String log = "\ndb." + StrUtil.lowerFirst(object.getClass().getSimpleName()) + ".save(";
		log += JSONUtil.toJsonPrettyStr(cloneList);
		log += ");";

		// 记录慢查询
		Long queryTime = LocalDateTime.now().toInstant(ZoneOffset.ofHours(8)).toEpochMilli()
			- startTime.toInstant(ZoneOffset.ofHours(8)).toEpochMilli();
		;
		if (queryTime > mongodbProperties.getSlowTime()) {
			insertSlowQuery(log, queryTime);
		}
		if (mongodbProperties.getPrint()) {
			// 打印语句
			LogUtils.info(log + "\n执行时间:" + queryTime + "ms");
		}
	}

	/**
	 * 插入或更新
	 *
	 * @param object 对象
	 */
	public String insertOrUpdate(Object object) {
		LocalDateTime time = LocalDateTime.now();
		String id = (String) ReflectUtil.getFieldValue(object, Constant.ID);
		Object objectOrg = StrUtil.isNotEmpty(id) ? findById(id, object.getClass()) : null;

		if (objectOrg == null) {
			// 插入
			// 设置插入时间
			setCreateTime(object, time);
			// 设置更新时间
			setUpdateTime(object, time);

			// 设置默认值
			setDefaultVaule(object);
			// 去除id值
			ReflectUtil.setFieldValue(object, Constant.ID, null);

			mongoTemplate.save(object);
			id = (String) ReflectUtil.getFieldValue(object, Constant.ID);
			logSave(object, time, true);
		} else {
			// 更新
			Field[] fields = ReflectUtil.getFields(object.getClass());
			// 拷贝属性
			for (Field field : fields) {
				if (!field.getName().equals(Constant.ID)
					&& ReflectUtil.getFieldValue(object, field) != null) {
					ReflectUtil.setFieldValue(objectOrg, field,
						ReflectUtil.getFieldValue(object, field));
				}
			}

			// 设置更新时间
			setUpdateTime(objectOrg, time);
			mongoTemplate.save(objectOrg);
			logSave(objectOrg, time, false);
		}

		return id;
	}

	/**
	 * 插入
	 *
	 * @param object 对象
	 */
	public String insert(Object object) {
		ReflectUtil.setFieldValue(object, Constant.ID, null);
		insertOrUpdate(object);
		return (String) ReflectUtil.getFieldValue(object, Constant.ID);
	}

	/**
	 * 批量插入
	 *
	 * @param <T>
	 * @param list 对象
	 */
	public <T> void insertAll(List<T> list) {
		LocalDateTime time = LocalDateTime.now();

		for (Object object : list) {
			// 去除id以便插入
			ReflectUtil.setFieldValue(object, Constant.ID, null);
			// 设置插入时间
			setCreateTime(object, time);
			// 设置更新时间
			setUpdateTime(object, time);
			// 设置默认值
			setDefaultVaule(object);
		}

		mongoTemplate.insertAll(list);
		logSave(list, time);

	}

	/**
	 * 设置更新时间
	 *
	 * @param object 对象
	 */
	private void setUpdateTime(Object object, LocalDateTime time) {
		Field[] fields = ReflectUtil.getFields(object.getClass());
		for (Field field : fields) {
			// 获取注解
			if (field.isAnnotationPresent(UpdateTime.class) && field.getType().equals(Long.class)) {
				ReflectUtil.setFieldValue(object, field, time);
			}
		}
	}

	/**
	 * 设置创建时间
	 *
	 * @param object 对象
	 * @param time   时间
	 * @since 2022-05-27 21:55:04
	 */
	private void setCreateTime(Object object, LocalDateTime time) {
		Field[] fields = ReflectUtil.getFields(object.getClass());
		for (Field field : fields) {
			// 获取注解
			if (field.isAnnotationPresent(CreateTime.class) && field.getType().equals(Long.class)) {
				ReflectUtil.setFieldValue(object, field, time);
			}
		}
	}

	/**
	 * 根据id更新
	 *
	 * @param object 对象
	 */
	public void updateById(Object object) {
		if (StrUtil.isEmpty((String) ReflectUtil.getFieldValue(object, Constant.ID))) {
			return;
		}
		if (findById((String) ReflectUtil.getFieldValue(object, Constant.ID), object.getClass())
			== null) {
			return;
		}
		insertOrUpdate(object);
	}

	/**
	 * 根据id更新全部字段
	 *
	 * @param object 对象
	 */
	public void updateAllColumnById(Object object) {
		if (StrUtil.isEmpty((String) ReflectUtil.getFieldValue(object, Constant.ID))) {
			return;
		}
		if (findById((String) ReflectUtil.getFieldValue(object, Constant.ID), object.getClass())
			== null) {
			return;
		}
		LocalDateTime time = LocalDateTime.now();
		setUpdateTime(object, time);
		mongoTemplate.save(object);
		logSave(object, time, false);
	}

	/**
	 * 更新查到的第一项
	 *
	 * @param criteriaWrapper 查询
	 * @param updateBuilder   更新
	 * @param clazz           类
	 */
	public void updateFirst(CriteriaWrapper criteriaWrapper, UpdateBuilder updateBuilder,
		Class<?> clazz) {
		Long time = System.currentTimeMillis();
		Query query = new Query(criteriaWrapper.build());

		mongoTemplate.updateFirst(query, updateBuilder.toUpdate(), clazz);
		logUpdate(clazz, query, updateBuilder, false, time);
	}

	/**
	 * 更新查到的全部项
	 *
	 * @param criteriaWrapper 查询
	 * @param updateBuilder   更新
	 * @param clazz           类
	 */
	public void updateMulti(CriteriaWrapper criteriaWrapper, UpdateBuilder updateBuilder,
		Class<?> clazz) {

		Long time = System.currentTimeMillis();
		Query query = new Query(criteriaWrapper.build());
		mongoTemplate.updateMulti(new Query(criteriaWrapper.build()), updateBuilder.toUpdate(),
			clazz);
		logUpdate(clazz, query, updateBuilder, true, time);
	}

	/**
	 * 根据id删除
	 *
	 * @param id    对象
	 * @param clazz 类
	 */
	public void deleteById(String id, Class<?> clazz) {
		if (StrUtil.isEmpty(id)) {
			return;
		}
		deleteByQuery(new CriteriaAndWrapper().eq(Constant::getId, id), clazz);
	}

	/**
	 * 根据id删除
	 *
	 * @param ids   对象
	 * @param clazz 类
	 */
	public void deleteByIds(List<String> ids, Class<?> clazz) {

		if (ids == null || ids.size() == 0) {
			return;
		}

		deleteByQuery(new CriteriaAndWrapper().in(Constant::getId, ids), clazz);
	}

	/**
	 * 根据条件删除
	 *
	 * @param criteriaWrapper 查询
	 * @param clazz           类
	 */
	public void deleteByQuery(CriteriaWrapper criteriaWrapper, Class<?> clazz) {
		Long time = System.currentTimeMillis();
		Query query = new Query(criteriaWrapper.build());
		mongoTemplate.remove(query, clazz);
		logDelete(clazz, query, time);
	}

	/**
	 * 设置默认值
	 *
	 * @param object 对象
	 */
	private void setDefaultVaule(Object object) {
		Field[] fields = ReflectUtil.getFields(object.getClass());
		for (Field field : fields) {
			// 获取注解
			if (field.isAnnotationPresent(InitValue.class)) {
				InitValue defaultValue = field.getAnnotation(InitValue.class);

				String value = defaultValue.value();

				if (ReflectUtil.getFieldValue(object, field) == null) {
					// 获取字段类型
					Class<?> type = field.getType();
					if (type.equals(String.class)) {
						ReflectUtil.setFieldValue(object, field, value);
					}
					if (type.equals(Short.class)) {
						ReflectUtil.setFieldValue(object, field, Short.parseShort(value));
					}
					if (type.equals(Integer.class)) {
						ReflectUtil.setFieldValue(object, field, Integer.parseInt(value));
					}
					if (type.equals(Long.class)) {
						ReflectUtil.setFieldValue(object, field, Long.parseLong(value));
					}
					if (type.equals(Float.class)) {
						ReflectUtil.setFieldValue(object, field, Float.parseFloat(value));
					}
					if (type.equals(Double.class)) {
						ReflectUtil.setFieldValue(object, field, Double.parseDouble(value));
					}
					if (type.equals(Boolean.class)) {
						ReflectUtil.setFieldValue(object, field, Boolean.parseBoolean(value));
					}
				}
			}
		}
	}

	/**
	 * 累加某一个字段的数量,原子操作
	 *
	 * @param id       id
	 * @param property property
	 * @param count    count
	 * @param clazz    clazz
	 * @since 2022-04-10 22:12:01
	 */
	public <R, E> void addCountById(String id, SerializableFunction<E, R> property, Number count,
                                    Class<?> clazz) {
		UpdateBuilder updateBuilder = new UpdateBuilder().inc(property, count);
		updateFirst(new CriteriaAndWrapper().eq(Constant::getId, id), updateBuilder, clazz);
	}

	/**
	 * 按查询条件获取Page
	 *
	 * @param criteriaWrapper 查询
	 * @param page            分页
	 * @param clazz           类
	 * @return Page 分页
	 */
	public <T> Page<T> findPage(CriteriaWrapper criteriaWrapper, Page<?> page, Class<T> clazz) {
		SortBuilder sortBuilder = new SortBuilder(Constant::getId, Direction.DESC);
		return findPage(criteriaWrapper, sortBuilder, page, clazz);
	}

	/**
	 * 按查询条件获取Page
	 *
	 * @param criteriaWrapper 查询
	 * @param sortBuilder     排序
	 * @param clazz           类
	 * @return Page 分页
	 */
	public <T> Page<T> findPage(CriteriaWrapper criteriaWrapper, SortBuilder sortBuilder,
		Page<?> page, Class<T> clazz) {

		Page<T> pageResp = new Page<T>();
		pageResp.setCurr(page.getCurr());
		pageResp.setLimit(page.getLimit());

		// 查询出总条数
		if (page.getQueryCount()) {
			Long count = findCountByQuery(criteriaWrapper, clazz);
			pageResp.setCount(count);
		}

		// 查询List
		Query query = new Query(criteriaWrapper.build());
		query.with(sortBuilder.toSort());
		query.skip((long) (page.getCurr() - 1) * page.getLimit());// 从那条记录开始
		query.limit(page.getLimit());// 取多少条记录

		Long systemTime = System.currentTimeMillis();
		List<T> list = mongoTemplate.find(query, clazz);
		logQuery(clazz, query, systemTime);

		pageResp.setList(list);

		return pageResp;
	}

	/**
	 * 按查询条件获取Page
	 *
	 * @param sortBuilder 查询
	 * @param page        排序
	 * @param clazz       类
	 * @return Page 分页
	 */
	public <T> Page<T> findPage(SortBuilder sortBuilder, Page<?> page, Class<T> clazz) {
		return findPage(new CriteriaAndWrapper(), sortBuilder, page, clazz);
	}

	/**
	 * 获取Page
	 *
	 * @param page  分页
	 * @param clazz 类
	 * @return Page 分页
	 */
	public <T> Page<T> findPage(Page<?> page, Class<T> clazz) {
		return findPage(new CriteriaAndWrapper(), page, clazz);
	}

	/**
	 * 根据id查找
	 *
	 * @param id    id
	 * @param clazz 类
	 * @return T 对象
	 */
	public <T> T findById(String id, Class<T> clazz) {

		if (StrUtil.isEmpty(id)) {
			return null;
		}
		Long systemTime = System.currentTimeMillis();

		T t = (T) mongoTemplate.findById(id, clazz);

		CriteriaAndWrapper criteriaAndWrapper = new CriteriaAndWrapper().eq(Constant::getId, id);
		logQuery(clazz, new Query(criteriaAndWrapper.build()), systemTime);
		return t;
	}

	/**
	 * 根据条件查找单个
	 *
	 * @param <T>             类型
	 * @param criteriaWrapper /
	 * @param clazz           类
	 * @return T 对象
	 */
	public <T> T findOneByQuery(CriteriaWrapper criteriaWrapper, Class<T> clazz) {
		SortBuilder sortBuilder = new SortBuilder(Constant::getId, Direction.DESC);
		return (T) findOneByQuery(criteriaWrapper, sortBuilder, clazz);
	}

	/**
	 * 根据条件查找单个
	 *
	 * @param criteriaWrapper 查询
	 * @param clazz           类
	 * @return T 对象
	 */
	public <T> T findOneByQuery(CriteriaWrapper criteriaWrapper, SortBuilder sortBuilder,
		Class<T> clazz) {

		Query query = new Query(criteriaWrapper.build());
		query.limit(1);
		query.with(sortBuilder.toSort());

		Long systemTime = System.currentTimeMillis();
		T t = (T) mongoTemplate.findOne(query, clazz);
		logQuery(clazz, query, systemTime);

		return t;

	}

	/**
	 * 根据条件查找单个
	 *
	 * @param sortBuilder 查询
	 * @param clazz       类
	 * @return T 对象
	 */
	public <T> T findOneByQuery(SortBuilder sortBuilder, Class<T> clazz) {
		return (T) findOneByQuery(new CriteriaAndWrapper(), sortBuilder, clazz);
	}

	/**
	 * 根据条件查找List
	 *
	 * @param <T>             类型
	 * @param criteriaWrapper 查询
	 * @param clazz           类
	 * @return List 列表
	 */
	public <T> List<T> findListByQuery(CriteriaWrapper criteriaWrapper, Class<T> clazz) {
		SortBuilder sortBuilder = new SortBuilder().add(Constant::getId, Direction.DESC);
		return findListByQuery(criteriaWrapper, sortBuilder, clazz);

	}

	/**
	 * 根据条件查找List
	 *
	 * @param <T>             类型
	 * @param criteriaWrapper 查询
	 * @param sortBuilder     排序
	 * @param clazz           类
	 * @return List 列表
	 */
	public <T> List<T> findListByQuery(CriteriaWrapper criteriaWrapper, SortBuilder sortBuilder,
		Class<T> clazz) {
		Query query = new Query(criteriaWrapper.build());
		query.with(sortBuilder.toSort());

		Long systemTime = System.currentTimeMillis();
		List<T> list = mongoTemplate.find(query, clazz);
		logQuery(clazz, query, systemTime);
		return list;

	}

	/**
	 * 根据条件查找某个属性
	 *
	 * @param <T>             类型
	 * @param criteriaWrapper 查询
	 * @param documentClass   类
	 * @param property        属性
	 * @param propertyClass   属性类
	 * @return List 列表
	 */
	public <T, R, E> List<T> findPropertiesByQuery(CriteriaWrapper criteriaWrapper,
		Class<?> documentClass, SerializableFunction<E, R> property, Class<T> propertyClass) {
		Query query = new Query(criteriaWrapper.build());
		query.fields().include(ReflectionUtil.getFieldName(property));

		Long systemTime = System.currentTimeMillis();
		List<?> list = mongoTemplate.find(query, documentClass);
		logQuery(documentClass, query, systemTime);

		List<T> propertyList = extractProperty(list, ReflectionUtil.getFieldName(property),
			propertyClass);
		return propertyList;
	}

	/**
	 * 根据条件查找某个属性
	 *
	 * @param criteriaWrapper 查询
	 * @param documentClass   类
	 * @param property        属性
	 * @return List 列表
	 */
	public <R, E> List<String> findPropertiesByQuery(CriteriaWrapper criteriaWrapper,
		Class<?> documentClass, SerializableFunction<E, R> property) {
		return findPropertiesByQuery(criteriaWrapper, documentClass, property, String.class);
	}

	/**
	 * 根据id查找某个属性
	 *
	 * @param ids      查询
	 * @param clazz    类
	 * @param property 属性
	 * @return List 列表
	 */
	public <R, E> List<String> findPropertiesByIds(List<String> ids, Class<?> clazz,
		SerializableFunction<E, R> property) {
		CriteriaAndWrapper criteriaAndWrapper = new CriteriaAndWrapper().in(Constant::getId, ids);
		return findPropertiesByQuery(criteriaAndWrapper, clazz, property);
	}

	/**
	 * 根据条件查找id
	 *
	 * @param criteriaWrapper 查询
	 * @param clazz           类
	 * @return List 列表
	 */
	public List<String> findIdsByQuery(CriteriaWrapper criteriaWrapper, Class<?> clazz) {
		return findPropertiesByQuery(criteriaWrapper, clazz, Constant::getId);
	}

	/**
	 * 根据id集合查找
	 *
	 * @param ids   id集合
	 * @param clazz 类
	 * @return List 列表
	 */
	public <T> List<T> findListByIds(Collection<String> ids, Class<T> clazz) {
		CriteriaAndWrapper criteriaAndWrapper = new CriteriaAndWrapper().in(Constant::getId, ids);
		return findListByQuery(criteriaAndWrapper, clazz);
	}

	/**
	 * 根据id集合查找
	 *
	 * @param ids   id集合
	 * @param clazz 类
	 * @return List 列表
	 */
	public <T> List<T> findListByIds(Collection<String> ids, SortBuilder sortBuilder,
		Class<T> clazz) {
		CriteriaAndWrapper criteriaAndWrapper = new CriteriaAndWrapper().in(Constant::getId, ids);
		return findListByQuery(criteriaAndWrapper, sortBuilder, clazz);
	}

	/**
	 * 根据id集合查找
	 *
	 * @param ids   id集合
	 * @param clazz 类
	 * @return List 列表
	 */
	public <T> List<T> findListByIds(String[] ids, SortBuilder sortBuilder, Class<T> clazz) {
		return findListByIds(Arrays.asList(ids), sortBuilder, clazz);
	}

	/**
	 * 根据id集合查找
	 *
	 * @param ids   id集合
	 * @param clazz 类
	 * @return List 列表
	 */
	public <T> List<T> findListByIds(String[] ids, Class<T> clazz) {
		SortBuilder sortBuilder = new SortBuilder(Constant::getId, Direction.DESC);
		return findListByIds(ids, sortBuilder, clazz);
	}

	/**
	 * 查询全部
	 *
	 * @param <T>   类型
	 * @param clazz 类
	 * @return List 列表
	 */
	public <T> List<T> findAll(Class<T> clazz) {
		SortBuilder sortBuilder = new SortBuilder(Constant::getId, Direction.DESC);
		return findListByQuery(new CriteriaAndWrapper(), sortBuilder, clazz);
	}

	/**
	 * 查询全部
	 *
	 * @param <T>   类型
	 * @param clazz 类
	 * @return List 列表
	 */
	public <T> List<T> findAll(SortBuilder sortBuilder, Class<T> clazz) {
		return findListByQuery(new CriteriaAndWrapper(), sortBuilder, clazz);
	}

	/**
	 * 查找全部的id
	 *
	 * @param clazz 类
	 * @return List 列表
	 */
	public List<String> findAllIds(Class<?> clazz) {
		return findIdsByQuery(new CriteriaAndWrapper(), clazz);
	}

	/**
	 * 查找数量
	 *
	 * @param criteriaWrapper 查询
	 * @param clazz           类
	 * @return Long 数量
	 */
	public Long findCountByQuery(CriteriaWrapper criteriaWrapper, Class<?> clazz) {
		Long systemTime = System.currentTimeMillis();
		Long count = null;

		Query query = new Query(criteriaWrapper.build());
		if (query.getQueryObject().isEmpty()) {
			count = mongoTemplate.getCollection(mongoTemplate.getCollectionName(clazz))
				.estimatedDocumentCount();
		} else {
			count = mongoTemplate.count(query, clazz);
		}

		logCount(clazz, query, systemTime);
		return count;
	}

	/**
	 * 查找全部数量
	 *
	 * @param clazz 类
	 * @return Long 数量
	 */
	public Long findAllCount(Class<?> clazz) {
		return findCountByQuery(new CriteriaAndWrapper(), clazz);
	}

	/**
	 * 获取list中对象某个属性,组成新的list
	 *
	 * @param list     列表
	 * @param clazz    类
	 * @param property 属性
	 * @return List<T> 列表
	 */
	@SuppressWarnings("unchecked")
	private <T> List<T> extractProperty(List<?> list, String property, Class<T> clazz) {
		Set<T> rs = new HashSet<T>();
		for (Object object : list) {
			Object value = ReflectUtil.getFieldValue(object, property);
			if (value != null && value.getClass().equals(clazz)) {
				rs.add((T) value);
			}
		}

		return new ArrayList<T>(rs);
	}

}
