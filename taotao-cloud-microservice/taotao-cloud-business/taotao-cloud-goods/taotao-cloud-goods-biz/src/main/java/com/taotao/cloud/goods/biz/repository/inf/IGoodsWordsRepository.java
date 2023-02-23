package com.taotao.cloud.goods.biz.repository.inf;

import com.taotao.cloud.goods.biz.model.entity.GoodsWords;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IGoodsWordsRepository extends JpaRepository<GoodsWords, Long> {
}
