package com.taotao.cloud.uc.biz.service;

import com.taotao.cloud.web.base.entity.SuperEntity;
import com.taotao.cloud.web.base.service.BaseSuperService;
import java.io.Serializable;

/**
 * 角色-资源服务类
 *
 * @since 2020/4/30 13:20
 */
public interface ISysRoleDeptService<T extends SuperEntity<T,I>, I extends Serializable> extends
	BaseSuperService<T, I> {
}
