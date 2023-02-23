package com.taotao.cloud.goods.biz.repository.inf;

import com.taotao.cloud.goods.biz.model.entity.DraftGoods;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDraftGoodsRepository extends JpaRepository<DraftGoods, Long> {
}
