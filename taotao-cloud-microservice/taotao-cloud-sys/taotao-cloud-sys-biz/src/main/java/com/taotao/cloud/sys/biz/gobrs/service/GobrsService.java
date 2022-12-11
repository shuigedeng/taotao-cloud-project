package com.taotao.cloud.sys.biz.gobrs.service;

import com.gobrs.async.core.GobrsAsync;
import com.gobrs.async.core.common.domain.AsyncResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The type Gobrs service.
 *
 * @program: gobrs -async-core
 * @ClassName GobrsService
 * @description:
 * @author: sizegang
 * @create: 2022 -03-28
 */
@Service
public class GobrsService {


	@Autowired(required = false)
	private GobrsAsync gobrsAsync;


	/**
	 * Gobrs async.
	 */
	public void gobrsAsync() {
		AsyncResult asyncResult = gobrsAsync.go("test", Object::new);
		if (asyncResult.getExecuteCode().equals(100)) {
			// 业务一
		} else if (asyncResult.getExecuteCode().equals(200)) {
			// 业务二
		}
	}

	/**
	 * Future.
	 */

	/**
	 * Update rule.
	 *
	 * @param rule the rule
	 */

}
