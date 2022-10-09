package com.taotao.cloud.goods.biz.repository.inf;

import com.taotao.cloud.goods.biz.model.entity.CustomWords;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICustomWordsRepository extends JpaRepository<CustomWords, Long> {
}
