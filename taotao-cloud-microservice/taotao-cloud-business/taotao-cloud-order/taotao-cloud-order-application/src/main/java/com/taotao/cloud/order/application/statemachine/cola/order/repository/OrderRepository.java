package com.taotao.cloud.order.application.statemachine.cola.order.repository;


import com.taotao.cloud.order.application.statemachine.cola.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {


}
