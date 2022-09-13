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
package com.taotao.cloud.web.base.repository;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.taotao.cloud.web.base.entity.SuperEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.jpa.repository.support.QuerydslJpaPredicateExecutor;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.querydsl.SimpleEntityPathResolver;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;

/**
 * 基础jpa Repository
 *
 * @param <T> the type of the entity to handle
 * @param <I> the type of the entity's identifier
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-04 07:32:26
 */
public abstract class BaseCrSuperRepository<T extends SuperEntity<T, I>, I extends Serializable> extends
	SimpleJpaRepository<T, I> {

	private final JPAQueryFactory jpaQueryFactory;
	private final QuerydslJpaPredicateExecutor<T> jpaPredicateExecutor;
	private final EntityManager em;
	private final EntityPath<T> path;
	private final Querydsl querydsl;

	public BaseCrSuperRepository(Class<T> domainClass, EntityManager em) {
		super(domainClass, em);
		this.em = em;
		this.jpaPredicateExecutor = new QuerydslJpaPredicateExecutor<>(
			JpaEntityInformationSupport.getEntityInformation(domainClass, em), em,
			SimpleEntityPathResolver.INSTANCE, getRepositoryMethodMetadata());
		this.jpaQueryFactory = new JPAQueryFactory(em);
		this.path = SimpleEntityPathResolver.INSTANCE.createPath(domainClass);
		this.querydsl = new Querydsl(em, new PathBuilder<T>(path.getType(), path.getMetadata()));
	}

	/**
	 * 分页查询
	 *
	 * @param predicate 条件参数
	 * @param pageable  分页参数
	 * @param orders    排序参数
	 * @return 分页数据
	 * @since 2021-10-09 20:29:49
	 */
	public Page<T> findPageable(Predicate predicate, Pageable pageable,
								OrderSpecifier<?>... orders) {
		final JPAQuery<T> countQuery = jpaQueryFactory.selectFrom(path);
		countQuery.where(predicate);
		JPQLQuery<T> query = querydsl.applyPagination(pageable, countQuery);
		query.orderBy(orders);
		return PageableExecutionUtils.getPage(query.fetch(), pageable, query::fetchCount);
	}

	/**
	 * 获取条数
	 *
	 * @param predicate 条件参数
	 * @return 条数
	 * @since 2021-10-09 20:30:31
	 */
	public int count(Predicate predicate) {
		return jpaQueryFactory.selectFrom(path)
			.where(predicate)
			.fetch()
			.size();
	}

	/**
	 * 是否存在
	 *
	 * @param predicate 条件参数
	 * @return 结果
	 * @since 2021-10-09 20:30:15
	 */
	public Boolean exists(Predicate predicate) {
		return jpaPredicateExecutor.exists(predicate);
	}

	/**
	 * 获取数据
	 *
	 * @param predicate 条件参数
	 * @return 数据列表
	 * @since 2021-10-09 20:30:44
	 */
	public List<T> fetch(Predicate predicate) {
		return jpaQueryFactory.selectFrom(path)
			.where(predicate)
			.fetch();
	}

	/**
	 * 获取单条数据
	 *
	 * @param predicate 条件参数
	 * @return 单条数据
	 * @since 2021-10-09 20:30:50
	 */
	public T fetchOne(Predicate predicate) {
		return jpaQueryFactory.selectFrom(path)
			.where(predicate)
			.fetchOne();
	}

	/**
	 * 获取数量
	 *
	 * @param predicate 条件参数
	 * @return 数量
	 * @since 2021-10-09 20:31:11
	 */
	public int fetchCount(Predicate predicate) {
		return jpaQueryFactory.selectFrom(path)
			.where(predicate)
			.fetch()
			.size();
	}

	/**
	 * 查询
	 *
	 * @param predicate 条件参数
	 * @param expr      表达式
	 * @param o         排序参数
	 * @return 数据列表
	 * @since 2021-10-09 20:31:18
	 */
	public List<?> find(Predicate predicate, Expression<?> expr, OrderSpecifier<?>... o) {
		return jpaQueryFactory
			.select(expr)
			.from(path)
			.where(predicate)
			.orderBy(o)
			.fetch();
	}

	public JPAQueryFactory jpaQueryFactory() {
		return jpaQueryFactory;
	}

	public QuerydslJpaPredicateExecutor<T> jpaPredicateExecutor() {
		return jpaPredicateExecutor;
	}

	public EntityManager entityManager() {
		return em;
	}

	public EntityPath<T> path() {
		return path;
	}

	public Querydsl querydsl() {
		return querydsl;
	}
}
