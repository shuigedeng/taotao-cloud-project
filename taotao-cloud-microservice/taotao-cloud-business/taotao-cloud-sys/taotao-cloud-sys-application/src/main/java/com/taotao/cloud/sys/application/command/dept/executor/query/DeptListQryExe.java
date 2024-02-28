
package com.taotao.cloud.sys.application.command.dept.executor.query;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.taotao.cloud.sys.application.command.dept.dto.DeptListQry;
import com.taotao.cloud.sys.application.command.dept.dto.clientobject.DeptCO;
import com.taotao.cloud.sys.common.enums.FindTypeEnums;
import com.taotao.cloud.sys.infrastructure.persistent.dept.mapper.DeptMapper;
import com.taotao.cloud.sys.infrastructure.persistent.dept.po.DeptPO;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.dromara.hutool.core.text.StrUtil;
import org.springframework.stereotype.Component;


/**
 * 查询部门列表执行器.
 */
@Component
@RequiredArgsConstructor
public class DeptListQryExe {

	private final DeptMapper deptMapper;

	/**
	 * 执行查询部门列表.
	 *
	 * @param qry 查询部门列表参数
	 * @return 部门列表
	 */
//	@DS(TENANT)
	public List<DeptCO> execute(DeptListQry qry) {
		return switch (FindTypeEnums.valueOf(qry.getType())) {
			case LIST -> getDeptList(qry).stream().map(this::convert).toList();
			case TREE_LIST -> buildTreeNode(getDeptList(qry).stream().map(this::convert).toList());
			case USER_TREE_LIST -> null;
		};
	}

	private List<DeptCO> buildTreeNode(List<DeptCO> list) {
//		return TreeUtil.buildTreeNode(list, DeptCO.class).getChildren();
		return new ArrayList<>();
	}

	private DeptCO convert(DeptPO deptPO) {
		return DeptCO.builder()
			.id(deptPO.getId())
			.pid(deptPO.getParentId())
			.name(deptPO.getName())
			.sort(deptPO.getSortNum())
			.children(new ArrayList<>(16))
			.build();
	}

	private List<DeptPO> getDeptList(DeptListQry qry) {
		String name = qry.getName();
		LambdaQueryWrapper<DeptPO> wrapper = Wrappers.lambdaQuery(DeptPO.class)
			.like(StrUtil.isNotEmpty(name), DeptPO::getName, name)
			.orderByDesc(DeptPO::getSortNum)
			.select(DeptPO::getId, DeptPO::getParentId, DeptPO::getName, DeptPO::getSortNum);
		return deptMapper.selectList(wrapper);
	}

}
