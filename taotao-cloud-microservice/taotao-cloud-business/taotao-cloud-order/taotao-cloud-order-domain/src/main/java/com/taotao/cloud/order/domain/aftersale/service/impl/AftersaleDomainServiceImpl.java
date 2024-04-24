package com.taotao.cloud.order.domain.aftersale.service.impl;

import com.taotao.cloud.order.domain.aftersale.repository.AftersaleDomainRepository;
import com.taotao.cloud.order.domain.aftersale.service.AftersaleDomainService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AftersaleDomainServiceImpl implements AftersaleDomainService {

	private AftersaleDomainRepository deptDomainRepository;

}
