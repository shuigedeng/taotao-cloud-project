package com.taotao.cloud.web.base.repository;

import com.taotao.cloud.web.base.entity.SuperEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.QueryHint;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * <p> Description : 基础Repository </p>
 *
 * @author shuigedeng
 * @version 2022.09
 * @since 2022-10-21 11:36:53
 */
@NoRepositoryBean
public interface BaseInterfaceSuperRepository<T extends SuperEntity<T, I>, I extends Serializable> extends
	JpaRepository<T, I>, JpaSpecificationExecutor<T> {

	/**
	 * 找到所有
	 *
	 * @return {@link List }<{@link T }>
	 * @since 2022-10-21 11:36:53
	 */
	@QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
	@Override
	List<T> findAll();

	/**
	 * 找到所有
	 *
	 * @param sort 排序
	 * @return {@link List }<{@link T }>
	 * @since 2022-10-21 11:36:53
	 */
	@QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
	@Override
	List<T> findAll(Sort sort);

	/**
	 * 找到一个
	 *
	 * @param specification 规范
	 * @return {@link Optional }<{@link T }>
	 * @since 2022-10-21 11:36:53
	 */
	@QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
	@Override
	Optional<T> findOne(Specification<T> specification);

	/**
	 * 找到所有
	 *
	 * @param specification 规范
	 * @return {@link List }<{@link T }>
	 * @since 2022-10-21 11:36:54
	 */
	@QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
	@Override
	List<T> findAll(Specification<T> specification);

	/**
	 * 找到所有
	 *
	 * @param specification 规范
	 * @param pageable      可分页
	 * @return {@link Page }<{@link T }>
	 * @since 2022-10-21 11:36:54
	 */
	@QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
	@Override
	Page<T> findAll(Specification<T> specification, Pageable pageable);

	/**
	 * 找到所有
	 *
	 * @param specification 规范
	 * @param sort          排序
	 * @return {@link List }<{@link T }>
	 * @since 2022-10-21 11:36:54
	 */
	@QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
	@Override
	List<T> findAll(Specification<T> specification, Sort sort);

	/**
	 * 数
	 *
	 * @param specification 规范
	 * @return long
	 * @since 2022-10-21 11:36:54
	 */
	@QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
	@Override
	long count(Specification<T> specification);

	/**
	 * 找到所有
	 *
	 * @param pageable 可分页
	 * @return {@link Page }<{@link T }>
	 * @since 2022-10-21 11:36:54
	 */
	@QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
	@Override
	Page<T> findAll(Pageable pageable);

	/**
	 * 发现通过id
	 *
	 * @param id id
	 * @return {@link Optional }<{@link T }>
	 * @since 2022-10-21 11:36:54
	 */
	@QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
	@Override
	Optional<T> findById(I id);

	/**
	 * 数
	 *
	 * @return long
	 * @since 2022-10-21 11:36:54
	 */
	@QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
	@Override
	long count();

	/**
	 * 删除通过id
	 *
	 * @param id id
	 * @since 2022-10-21 11:36:54
	 */
	@Transactional
	@Override
	void deleteById(I id);
}
