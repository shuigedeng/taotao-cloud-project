package com.taotao.cloud.order.domain.order.service.impl;

import com.taotao.cloud.order.domain.order.repository.OrderDomainRepository;
import com.taotao.cloud.order.domain.order.service.OrderDomainService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderDomainServiceImpl implements OrderDomainService {

	private OrderDomainRepository deptDomainRepository;

}
