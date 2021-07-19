/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.data.jpa.repository;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.jpa.repository.support.QuerydslJpaPredicateExecutor;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.querydsl.SimpleEntityPathResolver;
import org.springframework.data.repository.support.PageableExecutionUtils;

/**
 * 基础jpa Repository
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/9/28 16:00
 */
public abstract class BaseJpaRepository<T, ID> extends SimpleJpaRepository<T, ID> {

	protected final JPAQueryFactory jpaQueryFactory;
	protected final QuerydslJpaPredicateExecutor<T> jpaPredicateExecutor;
	protected final EntityManager em;
	private final EntityPath<T> path;
	protected final Querydsl querydsl;

	public BaseJpaRepository(Class<T> domainClass, EntityManager em) {
		super(domainClass, em);
		this.em = em;
		this.jpaPredicateExecutor = new QuerydslJpaPredicateExecutor<>(
			JpaEntityInformationSupport.getEntityInformation(domainClass, em), em,
			SimpleEntityPathResolver.INSTANCE, getRepositoryMethodMetadata());
		this.jpaQueryFactory = new JPAQueryFactory(em);
		this.path = SimpleEntityPathResolver.INSTANCE.createPath(domainClass);
		this.querydsl = new Querydsl(em, new PathBuilder<T>(path.getType(), path.getMetadata()));
	}

	public Page<T> findAll(Predicate predicate, Pageable pageable, OrderSpecifier<?>... orders) {
		final JPAQuery<T> countQuery = jpaQueryFactory.selectFrom(path);
		countQuery.where(predicate);
		JPQLQuery<T> query = querydsl.applyPagination(pageable, countQuery);
		query.orderBy(orders);
		return PageableExecutionUtils.getPage(query.fetch(), pageable, countQuery::fetchCount);
	}

	public long count(Predicate predicate) {
		return jpaQueryFactory.selectFrom(path)
			.where(predicate)
			.fetchCount();
	}

	public Boolean exists(Predicate predicate) {
		return jpaPredicateExecutor.exists(predicate);
	}

	public List<T> fetch(Predicate predicate) {
		return jpaQueryFactory.selectFrom(path)
			.where(predicate)
			.fetch();
	}

	public T fetchOne(Predicate predicate) {
		return jpaQueryFactory.selectFrom(path)
			.where(predicate)
			.fetchOne();
	}

	public long fetchCount(Predicate predicate) {
		return jpaQueryFactory.selectFrom(path)
			.where(predicate)
			.fetchCount();
	}

	public List<?> find(Predicate predicate, Expression<?> expr, OrderSpecifier<?>... o) {
		return jpaQueryFactory
			.select(expr)
			.from(path)
			.where(predicate)
			.orderBy(o)
			.fetch();
	}
}
