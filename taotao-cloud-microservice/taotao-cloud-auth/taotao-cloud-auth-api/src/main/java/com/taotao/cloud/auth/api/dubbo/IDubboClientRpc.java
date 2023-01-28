package com.taotao.cloud.auth.api.dubbo;

import com.taotao.cloud.auth.api.dubbo.response.DubboClientRes;

/**
 * 后台部门表服务接口 
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-25 14:22:32
 */
public interface IDubboClientRpc {

	public DubboClientRes findById(Long id);

}
