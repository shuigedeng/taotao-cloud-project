package com.taotao.cloud.sys.biz.service;

import com.taotao.cloud.sys.api.vo.dept.DeptTreeVO;
import com.taotao.cloud.sys.biz.entity.system.Dept;
import com.taotao.cloud.web.base.service.BaseSuperService;
import java.util.List;

/**
 * 后台部门表服务接口
 *
 * @author shuigedeng
 * @since 2020-10-16 15:54:05
 * @since 1.0
 */
public interface IDeptService extends BaseSuperService<Dept, Long> {

	List<DeptTreeVO> tree();

}
