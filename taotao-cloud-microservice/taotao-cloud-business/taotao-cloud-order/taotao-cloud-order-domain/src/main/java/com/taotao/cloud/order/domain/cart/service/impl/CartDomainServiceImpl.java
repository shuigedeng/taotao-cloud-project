package com.taotao.cloud.order.domain.cart.service.impl;

import com.taotao.cloud.order.domain.cart.repository.CartDomainRepository;
import com.taotao.cloud.order.domain.cart.service.CartDomainService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CartDomainServiceImpl implements CartDomainService {

	private CartDomainRepository deptDomainRepository;

}
