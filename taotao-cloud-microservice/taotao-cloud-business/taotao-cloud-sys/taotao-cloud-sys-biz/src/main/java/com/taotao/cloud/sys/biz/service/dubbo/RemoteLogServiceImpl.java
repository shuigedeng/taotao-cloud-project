package com.taotao.cloud.sys.biz.service.dubbo;

import com.taotao.cloud.sys.api.dubbo.RemoteLogService;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

/**
 * 操作日志记录
 */
@RequiredArgsConstructor
@Service
@DubboService
public class RemoteLogServiceImpl implements RemoteLogService {

	//private final ISysOperLogService operLogService;
	//private final ISysLogininforService logininforService;
	//
	//@Override
	//public Boolean saveLog(SysOperLog sysOperLog) {
	//	return operLogService.insertOperlog(sysOperLog) > 0;
	//}
	//
	//@Override
	//public Boolean saveLogininfor(SysLogininfor sysLogininfor) {
	//	return logininforService.insertLogininfor(sysLogininfor) > 0;
	//}
}
