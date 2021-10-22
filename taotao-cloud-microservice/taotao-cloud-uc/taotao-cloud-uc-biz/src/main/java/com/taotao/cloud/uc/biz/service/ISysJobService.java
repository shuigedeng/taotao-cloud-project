package com.taotao.cloud.uc.biz.service;

import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import com.taotao.cloud.web.base.entity.SuperEntity;
import com.taotao.cloud.web.base.service.BaseSuperService;
import java.io.Serializable;

/**
 * 岗位表服务接口
 *
 * @author shuigedeng
 * @since 2020-10-16 16:23:05
 * @since 1.0
 */
public interface ISysJobService<T extends SuperEntity<T,I>, I extends Serializable> extends
	BaseSuperService<T, I> {

}
