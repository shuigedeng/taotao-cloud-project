package com.taotao.cloud.uc.api.service;

import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import com.taotao.cloud.web.base.entity.SuperEntity;
import com.taotao.cloud.web.base.service.BaseSuperService;
import java.io.Serializable;

/**
 * 后台部门表服务接口
 *
 * @author shuigedeng
 * @since 2020-10-16 15:54:05
 * @since 1.0
 */
public interface ISysCompanyService<T extends SuperEntity<T,I>, I extends Serializable> extends
	BaseSuperService<T, I> {

}
