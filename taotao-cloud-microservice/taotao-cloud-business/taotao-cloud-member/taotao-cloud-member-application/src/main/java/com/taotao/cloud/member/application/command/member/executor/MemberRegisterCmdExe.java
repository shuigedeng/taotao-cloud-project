

package com.taotao.cloud.member.application.command.member.executor;

import com.taotao.cloud.sys.application.command.dept.dto.DeptUpdateCmd;
import com.taotao.cloud.sys.application.converter.DeptConvert;
import com.taotao.cloud.sys.domain.dept.service.DeptDomainService;
import com.taotao.cloud.sys.infrastructure.persistent.dept.mapper.DeptMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


/**
 * 修改部门执行器.
 *
 *
 */
@Component
@RequiredArgsConstructor
public class MemberRegisterCmdExe {

	private final DeptDomainService deptDomainService;

	private final DeptMapper deptMapper;

	private final DeptConvert memberNoticeConvertor;

	/**
	 * 执行修改部门.
	 *
	 * @param cmd 修改部门参数
	 * @return 执行修改结果
	 */
//	@DS(TENANT)
	public Boolean execute(DeptUpdateCmd cmd) {
//		DeptCO co = cmd.getDeptCO();
//		Long id = co.getId();
//		if (ObjectUtil.isNull(id)) {
//			throw new SystemException(ValidatorUtil.getMessage(SYSTEM_ID_REQUIRE));
//		}
//		long count = deptMapper
//			.selectCount(Wrappers.lambdaQuery(DeptDO.class).eq(DeptDO::getName, co.getName())
//				.ne(DeptDO::getId, id));
//		if (count > 0) {
//			throw new BusinessException("部门已存在，请重新填写");
//		}
//		if (co.getId().equals(co.getPid())) {
//			throw new BusinessException("上级部门不能为当前部门");
//		}
//		return deptDomainService.update(deptConvertor.toEntity(co));
		return false;
	}

}
