package com.taotao.cloud.sys.api.dubbo;


import com.taotao.cloud.sys.api.bo.resource.ResourceQueryBO;
import java.util.List;

/**
 * 后台部门表服务接口
 *
 * @author shuigedeng
 * @since 2020-10-16 15:54:05
 * @since 1.0
 */
public interface IDubboResourceService {
	List<ResourceQueryBO> queryAllId(Long id);
}
