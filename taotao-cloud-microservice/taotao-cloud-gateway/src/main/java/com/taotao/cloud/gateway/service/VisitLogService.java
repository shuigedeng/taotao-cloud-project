package com.taotao.cloud.gateway.service;



import com.taotao.cloud.gateway.model.AccessRecord;
import org.springframework.stereotype.Service;

import java.util.HashSet;

/**
 * 访问日志Service类
 */
@Service
public class VisitLogService  {

	//todo 需要实现
	public boolean saveBatch(HashSet<AccessRecord> oldCache, int batchSize) {
		return false;
	}
}
