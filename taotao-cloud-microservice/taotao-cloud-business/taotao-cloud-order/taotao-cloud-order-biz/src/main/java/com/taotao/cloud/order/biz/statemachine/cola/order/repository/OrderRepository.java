package com.taotao.cloud.order.biz.statemachine.cola.order.repository;


import com.taotao.cloud.order.biz.statemachine.cola.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OrderRepository extends JpaSuperRepository<Order, Integer> {


}
