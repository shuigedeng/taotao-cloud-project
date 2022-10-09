package com.taotao.cloud.goods.biz.repository.inf;

import com.taotao.cloud.goods.biz.model.entity.Commodity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ICommodityRepository extends JpaRepository<Commodity, Long> {
}
