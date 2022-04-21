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
package com.taotao.cloud.data.jpa.listener;

import cn.hutool.db.sql.SqlFormatter;
import com.taotao.cloud.common.utils.log.LogUtil;
import java.io.Serializable;
import java.util.Iterator;
import org.hibernate.CallbackException;
import org.hibernate.EmptyInterceptor;
import org.hibernate.EntityMode;
import org.hibernate.Transaction;
import org.hibernate.type.Type;

/**
 * HibernateInterceptor
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-10 17:10:27
 */
public class HibernateInterceptor extends EmptyInterceptor {

	@Override
	public void onDelete(Object entity, Serializable id, Object[] state,
		String[] propertyNames, Type[] types) {
		LogUtil.info("HibernateInterceptor >>>>>>>>>>>>>>>>>>>>>>>>> onDelete");
		super.onDelete(entity, id, state, propertyNames, types);
	}

	@Override
	public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState,
		Object[] previousState, String[] propertyNames, Type[] types) {
		LogUtil.info("HibernateInterceptor >>>>>>>>>>>>>>>>>>>>>>>>> onFlushDirty");
		return super.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
	}

	@Override
	public boolean onLoad(Object entity, Serializable id, Object[] state,
		String[] propertyNames, Type[] types) {
		LogUtil.info("HibernateInterceptor >>>>>>>>>>>>>>>>>>>>>>>>> onLoad");
		return super.onLoad(entity, id, state, propertyNames, types);
	}

	@Override
	public boolean onSave(Object entity, Serializable id, Object[] state,
		String[] propertyNames, Type[] types) {
		LogUtil.info("HibernateInterceptor >>>>>>>>>>>>>>>>>>>>>>>>> onSave");
		return super.onSave(entity, id, state, propertyNames, types);
	}

	@Override
	public void postFlush(Iterator entities) {
		LogUtil.info("HibernateInterceptor >>>>>>>>>>>>>>>>>>>>>>>>> postFlush");
		super.postFlush(entities);
	}

	@Override
	public void preFlush(Iterator entities) {
		LogUtil.info("HibernateInterceptor >>>>>>>>>>>>>>>>>>>>>>>>> preFlush");
		super.preFlush(entities);
	}

	@Override
	public Boolean isTransient(Object entity) {
		LogUtil.info("HibernateInterceptor >>>>>>>>>>>>>>>>>>>>>>>>> isTransient");
		return super.isTransient(entity);
	}

	@Override
	public Object instantiate(String entityName, EntityMode entityMode,
		Serializable id) {
		LogUtil.info("HibernateInterceptor >>>>>>>>>>>>>>>>>>>>>>>>> instantiate");
		return super.instantiate(entityName, entityMode, id);
	}

	@Override
	public int[] findDirty(Object entity, Serializable id, Object[] currentState,
		Object[] previousState, String[] propertyNames, Type[] types) {
		LogUtil.info("HibernateInterceptor >>>>>>>>>>>>>>>>>>>>>>>>> findDirty");
		return super.findDirty(entity, id, currentState, previousState, propertyNames, types);
	}

	@Override
	public String getEntityName(Object object) {
		LogUtil.info("HibernateInterceptor >>>>>>>>>>>>>>>>>>>>>>>>> getEntityName");
		return super.getEntityName(object);
	}

	@Override
	public Object getEntity(String entityName, Serializable id) {
		LogUtil.info("HibernateInterceptor >>>>>>>>>>>>>>>>>>>>>>>>> getEntity");
		return super.getEntity(entityName, id);
	}

	@Override
	public void afterTransactionBegin(Transaction tx) {
		LogUtil.info("HibernateInterceptor >>>>>>>>>>>>>>>>>>>>>>>>> afterTransactionBegin");
		super.afterTransactionBegin(tx);
	}

	@Override
	public void afterTransactionCompletion(Transaction tx) {
		LogUtil.info("HibernateInterceptor >>>>>>>>>>>>>>>>>>>>>>>>> afterTransactionCompletion");
		super.afterTransactionCompletion(tx);
	}

	@Override
	public void beforeTransactionCompletion(Transaction tx) {
		LogUtil.info("HibernateInterceptor >>>>>>>>>>>>>>>>>>>>>>>>> beforeTransactionCompletion");
		super.beforeTransactionCompletion(tx);
	}

	@Override
	public String onPrepareStatement(String sql) {
		LogUtil.info("HibernateInterceptor >>>>>>>>>>>>>>>>>>>>>>>>> onPrepareStatement");
		LogUtil.info(SqlFormatter.format(sql));

		return super.onPrepareStatement(sql);
	}

	@Override
	public void onCollectionRemove(Object collection, Serializable key) throws CallbackException {
		LogUtil.info("HibernateInterceptor >>>>>>>>>>>>>>>>>>>>>>>>> onCollectionRemove");
		super.onCollectionRemove(collection, key);
	}

	@Override
	public void onCollectionRecreate(Object collection, Serializable key) throws CallbackException {
		LogUtil.info("HibernateInterceptor >>>>>>>>>>>>>>>>>>>>>>>>> onCollectionRecreate");
		super.onCollectionRecreate(collection, key);
	}

	@Override
	public void onCollectionUpdate(Object collection, Serializable key) throws CallbackException {
		LogUtil.info("HibernateInterceptor >>>>>>>>>>>>>>>>>>>>>>>>> onCollectionUpdate");
		super.onCollectionUpdate(collection, key);
	}
}
