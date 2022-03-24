package com.taotao.cloud.sys.api.dubbo;


import com.taotao.cloud.sys.api.bo.menu.MenuQueryBO;
import java.util.List;

/**
 * 后台部门表服务接口
 *
 * @author shuigedeng
 * @since 2020-10-16 15:54:05
 */
public interface IDubboMenuService {

	List<MenuQueryBO> queryAllId(Long id);
}
