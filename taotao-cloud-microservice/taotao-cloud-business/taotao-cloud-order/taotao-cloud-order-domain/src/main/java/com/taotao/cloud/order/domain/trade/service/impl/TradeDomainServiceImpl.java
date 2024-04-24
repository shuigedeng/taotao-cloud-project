package com.taotao.cloud.order.domain.trade.service.impl;

import com.taotao.cloud.order.domain.trade.repository.TradeDomainRepository;
import com.taotao.cloud.order.domain.trade.service.TradeDomainService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TradeDomainServiceImpl implements TradeDomainService {

	private TradeDomainRepository deptDomainRepository;
}
