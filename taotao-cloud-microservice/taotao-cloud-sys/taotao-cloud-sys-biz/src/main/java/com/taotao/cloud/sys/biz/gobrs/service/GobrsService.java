package com.taotao.cloud.sys.biz.gobrs.service;

import com.gobrs.async.GobrsAsync;
import com.gobrs.async.domain.AsyncResult;
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
		if (asyncResult.getExpCode().equals(100)) {
			// 业务一
		} else if (asyncResult.getExpCode().equals(200)) {
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
