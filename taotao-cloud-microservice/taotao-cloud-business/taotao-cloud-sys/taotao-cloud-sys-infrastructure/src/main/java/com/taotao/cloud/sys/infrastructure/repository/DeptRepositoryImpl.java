package com.taotao.cloud.sys.infrastructure.repository;

import com.taotao.cloud.sys.domain.dept.entity.DeptEntity;
import com.taotao.cloud.sys.domain.dept.repository.DeptDomainRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeptRepositoryImpl implements DeptDomainRepository {

	@Override
	public void create(DeptEntity dept) {

	}

	@Override
	public void modify(DeptEntity dept) {

	}

	@Override
	public void remove(Long[] ids) {

	}
//
//	private final DeptMapper deptMapper;
//	private final DeptRepository deptRepository;
//	private final IDeptRepository deptRepositoryInterface;
//
//	private final TransactionalUtil transactionalUtil;
//
//	private final DeptConvertor deptConvertor;
//
//	private final MybatisUtil mybatisUtil;
//
//	/**
//	 * 新增部门.
//	 *
//	 * @param dept 部门对象
//	 */
//	@Override
//	public void create(DeptEntity dept) {
//		long count = deptMapper.selectCount(
//			Wrappers.lambdaQuery(DeptPO.class).eq(DeptPO::getName, dept.getName()));
//		dept.checkName(count);
//		DeptPO deptDO = deptConvertor.convert(dept);
//		// 修改新path
//		deptDO.setPath(getNewPath(deptDO));
//		create(deptDO);
//	}
//
//	/**
//	 * 修改部门.
//	 *
//	 * @param dept 部门对象
//	 */
//	@Override
//	public void modify(DeptEntity dept) {
//		dept.checkNullId();
//		long count = deptMapper.selectCount(
//			Wrappers.lambdaQuery(DeptPO.class).eq(DeptPO::getName, dept.getName())
//				.ne(DeptPO::getId, dept.getId()));
//		dept.checkName(count);
//		dept.checkIdAndPid();
//		DeptPO deptDO = deptConvertor.convert(dept);
//		// 修改新path
//		deptDO.setPath(getNewPath(deptDO));
//		String oldPath = deptMapper.selectPathById(deptDO.getId());
//		// 根据oldPath获取所有子节点
//		List<DeptPO> children = deptMapper.selectListByPath(oldPath);
//		// 版本号
//		deptDO.setVersion(deptMapper.selectVersion(deptDO.getId()));
//		modify(deptDO, oldPath, deptDO.getPath(), children);
//	}
//
//	/**
//	 * 根据ID删除部门.
//	 *
//	 * @param ids IDS
//	 */
//	@Override
//	public void remove(Long[] ids) {
//		transactionalUtil.defaultExecuteWithoutResult(r -> {
//			try {
//				deptMapper.deleteBatchIds(Arrays.asList(ids));
//			}
//			catch (Exception e) {
//				LogUtil.error("错误信息：{}，详情见日志", e.getMessage(), e);
//				r.setRollbackOnly();
//				throw new BusinessException(e.getMessage());
//			}
//		});
//	}
//
//	/**
//	 * 修改部门.
//	 *
//	 * @param deptDO   部门数据模型
//	 * @param oldPath  旧部门PATH
//	 * @param newPath  新部门PATH
//	 * @param children 部门子节点列表
//	 */
//	public void modify(DeptPO deptDO, String oldPath, String newPath, List<DeptPO> children) {
//		transactionalUtil.defaultExecuteWithoutResult(r -> {
//			try {
//				deptMapper.updateById(deptDO);
//				// 修改PATH
//				modifyPath(oldPath, newPath, children);
//			}
//			catch (Exception e) {
//				LogUtil.error("错误信息：{}，详情见日志", e.getMessage(), e);
//				r.setRollbackOnly();
//				throw new BusinessException(e.getMessage());
//			}
//		});
//	}
//
//	/**
//	 * 新增部门.
//	 *
//	 * @param deptDO 部门数据模型
//	 */
//	private void create(DeptPO deptDO) {
//		transactionalUtil.defaultExecuteWithoutResult(r -> {
//			try {
//				deptMapper.insert(deptDO);
//			}
//			catch (Exception e) {
//				LogUtil.error("错误信息：{}，详情见日志", e.getMessage(), e);
//				r.setRollbackOnly();
//				throw new BusinessException(e.getMessage());
//			}
//		});
//	}
//
//	/**
//	 * 修改部门子节点PATH.
//	 *
//	 * @param oldPath  旧部门PATH
//	 * @param newPath  新部门PATH
//	 * @param children 部门子节点列表
//	 */
//	private void modifyPath(String oldPath, String newPath, List<DeptPO> children) {
//		if (CollUtil.isNotEmpty(children)) {
//			children.parallelStream()
//				.forEach(item -> item.setPath(item.getPath().replace(oldPath, newPath)));
//			mybatisUtil.batch(children, DeptMapper.class, DynamicDataSourceContextHolder.peek(),
//				DeptMapper::updateById);
//		}
//	}
//
//	/**
//	 * 查看部门PATH.
//	 *
//	 * @param deptDO 部门对象
//	 * @return 部门PATH
//	 */
//	private String getNewPath(DeptPO deptDO) {
//		Long id = deptDO.getId();
//		Long pid = deptDO.getParentId();
//		String path = deptMapper.selectPathById(pid);
////		return StrUtil.isNotEmpty(path) ? path + COMMA + id : DEFAULT + COMMA + id;
//		return "";
//	}

}
