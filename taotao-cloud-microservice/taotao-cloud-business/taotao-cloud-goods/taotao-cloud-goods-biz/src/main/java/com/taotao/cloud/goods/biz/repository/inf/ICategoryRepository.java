package com.taotao.cloud.goods.biz.repository.inf;

import com.taotao.cloud.goods.biz.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICategoryRepository extends JpaRepository<Category, Long> {
}
