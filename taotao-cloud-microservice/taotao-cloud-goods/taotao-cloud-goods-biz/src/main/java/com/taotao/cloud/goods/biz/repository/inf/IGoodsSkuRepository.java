package com.taotao.cloud.goods.biz.repository.inf;

import com.taotao.cloud.goods.biz.model.entity.GoodsSku;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IGoodsSkuRepository extends JpaRepository<GoodsSku, Long> {
}
