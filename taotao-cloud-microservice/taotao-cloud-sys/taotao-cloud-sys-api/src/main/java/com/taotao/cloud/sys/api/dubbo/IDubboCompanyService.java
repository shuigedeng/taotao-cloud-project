package com.taotao.cloud.sys.api.dubbo;


import com.taotao.cloud.sys.api.bo.menu.MenuQueryBO;

/**
 * 后台部门表服务接口
 *
 * @author shuigedeng
 * @since 2020-10-16 15:54:05
 * @since 1.0
 */
public interface IDubboCompanyService {
	MenuQueryBO queryAllId(Long id);
}
