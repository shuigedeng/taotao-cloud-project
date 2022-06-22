package com.taotao.cloud.sys.biz.service;

import com.taotao.cloud.sys.api.vo.dept.DeptTreeVO;
import com.taotao.cloud.sys.biz.model.entity.system.Dept;
import com.taotao.cloud.web.base.service.BaseSuperService;
import java.util.List;

/**
 * 后台部门表服务接口
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-25 14:24:48
 */
public interface IDeptService extends BaseSuperService<Dept, Long> {

	/**
	 * 获取部门树
	 *
	 * @return 部门树列表
	 * @since 2022-03-23 08:52:34
	 */
	List<DeptTreeVO> tree();

}
