package com.taotao.cloud.order.biz.application.statemachine.cola.order.repository;


import com.taotao.boot.data.jpa.base.repository.BaseRepository;
import com.taotao.cloud.order.biz.statemachine.cola.entity.Order;
import org.springframework.stereotype.Repository;


@Repository
public interface OrderRepository extends BaseRepository<Order, Integer> {


}
