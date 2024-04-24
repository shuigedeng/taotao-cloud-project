package com.taotao.cloud.order.domain.purchase.service.impl;

import com.taotao.cloud.order.domain.purchase.repository.PurchaseDomainRepository;
import com.taotao.cloud.order.domain.purchase.service.PurchaseDomainService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PurchaseDomainServiceImpl implements PurchaseDomainService {

	private PurchaseDomainRepository deptDomainRepository;
}
