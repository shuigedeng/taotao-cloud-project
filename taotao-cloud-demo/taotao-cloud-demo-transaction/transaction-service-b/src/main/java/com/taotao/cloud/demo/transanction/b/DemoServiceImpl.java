package com.taotao.cloud.demo.transanction.b;

import com.codingapi.txlcn.common.util.Transactions;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.codingapi.txlcn.tracing.TracingContext;
import com.taotao.cloud.demo.transaction.common.db.domain.Demo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Description:
 * Date: 2018/12/25
 *
 * @author ujued
 */
@Service
@Slf4j
public class DemoServiceImpl implements DemoService {
	@Resource
	private DemoMapper demoMapper;

	@Override
	@LcnTransaction
	@Transactional(rollbackFor = Exception.class)
	public String rpc(String value) {
		Demo demo = new Demo();
		demo.setGroupId(TracingContext.tracing().groupId());
		demo.setDemoField(value);
		demo.setAppName(Transactions.getApplicationId());
		demo.setCreateTime(new Date());
		demoMapper.save(demo);
		return "ok-service-b";
	}
}
