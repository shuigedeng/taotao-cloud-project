

package com.taotao.cloud.member.application.command.member.executor;

import com.taotao.cloud.sys.application.command.dept.dto.DeptInsertCmd;
import com.taotao.cloud.sys.application.converter.DeptConvert;
import com.taotao.cloud.sys.domain.dept.service.DeptDomainService;
import com.taotao.cloud.sys.infrastructure.persistent.dept.mapper.DeptMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


/**
 * 新增部门执行器.
 *
 * 
 */
@Component
@RequiredArgsConstructor
public class MemberAddCmdExe {

	private final DeptDomainService deptDomainService;

	private final DeptMapper deptMapper;

	private final DeptConvert memberNoticeConvertor;

	/**
	 * 执行新增部门.
	 * @param cmd 新增部门参数
	 * @return 执行新增结果
	 */
//	@DS(TENANT)
	public Boolean execute(DeptInsertCmd cmd) {
//		DeptCO co = cmd.getDeptCO();
//		long count = deptMapper.selectCount(Wrappers.lambdaQuery(DeptDO.class).eq(DeptDO::getName, co.getName()));
//		if (count > 0) {
//			throw new SystemException("部门已存在，请重新填写");
//		}
//		return deptGateway.insert(deptConvertor.toEntity(co));
		return false;
	}

}
