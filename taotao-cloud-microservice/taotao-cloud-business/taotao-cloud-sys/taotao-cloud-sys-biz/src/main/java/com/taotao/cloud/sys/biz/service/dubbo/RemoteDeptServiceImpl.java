package com.taotao.cloud.sys.biz.service.dubbo;

import com.taotao.cloud.sys.api.dubbo.RemoteDeptService;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

/**
 * 部门服务
 */
@RequiredArgsConstructor
@Service
@DubboService
public class RemoteDeptServiceImpl implements RemoteDeptService {

	@Override
	public String selectDeptNameByIds(String deptIds) {
		return null;
	}

	//private final ISysDeptService sysDeptService;
	//
	//@Override
	//public String selectDeptNameByIds(String deptIds) {
	//	return sysDeptService.selectDeptNameByIds(deptIds);
	//}
}
