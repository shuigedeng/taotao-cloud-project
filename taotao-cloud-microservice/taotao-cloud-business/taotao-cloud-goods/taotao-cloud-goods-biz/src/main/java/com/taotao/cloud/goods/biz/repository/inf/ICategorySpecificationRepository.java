package com.taotao.cloud.goods.biz.repository.inf;

import com.taotao.cloud.goods.biz.model.entity.CategorySpecification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICategorySpecificationRepository extends JpaRepository<CategorySpecification, Long> {
}