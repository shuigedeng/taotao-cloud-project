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
 */
@NoRepositoryBean
public interface BaseIrSuperRepository<T extends SuperEntity<T, I>, I extends Serializable> extends
	JpaRepository<T, I>, JpaSpecificationExecutor<T> {

	@QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
	@Override
	List<T> findAll();

	@QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
	@Override
	List<T> findAll(Sort sort);

	@QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
	@Override
	Optional<T> findOne(Specification<T> specification);

	@QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
	@Override
	List<T> findAll(Specification<T> specification);

	@QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
	@Override
	Page<T> findAll(Specification<T> specification, Pageable pageable);

	@QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
	@Override
	List<T> findAll(Specification<T> specification, Sort sort);

	@QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
	@Override
	long count(Specification<T> specification);

	@QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
	@Override
	Page<T> findAll(Pageable pageable);

	@QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
	@Override
	Optional<T> findById(I id);

	@QueryHints(@QueryHint(name = org.hibernate.annotations.QueryHints.CACHEABLE, value = "true"))
	@Override
	long count();

	@Transactional
	@Override
	void deleteById(I id);
}
