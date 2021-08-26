package com.taotao.cloud.seckill.biz.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.itstyle.seckill.common.entity.Seckill;

public interface SeckillRepository extends JpaRepository<Seckill, Long> {
	
	
}
