package com.taotao.cloud.standalone.system.modules.sys.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.standalone.system.modules.sys.domain.SysDept;
import com.taotao.cloud.standalone.system.modules.sys.dto.DeptDTO;
import com.taotao.cloud.standalone.system.modules.sys.mapper.SysDeptMapper;
import com.taotao.cloud.standalone.system.modules.sys.service.ISysDeptService;
import com.taotao.cloud.standalone.system.modules.sys.util.PreUtil;
import com.taotao.cloud.standalone.system.modules.sys.vo.DeptTreeVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 部门管理 服务实现类
 * </p>
 *

 * @since 2019-04-21
 */
@Service
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements ISysDeptService {

	@Override
	public List<SysDept> selectDeptList() {
		List<SysDept> depts = baseMapper.selectList(Wrappers.<SysDept>lambdaQuery().select(SysDept::getDeptId, SysDept::getName, SysDept::getParentId, SysDept::getSort, SysDept::getCreateTime));
		List<SysDept> sysDepts = depts.stream()
			.filter(sysDept -> sysDept.getParentId() == 0 || ObjectUtil.isNull(sysDept.getParentId()))
			.peek(sysDept -> sysDept.setLevel(0))
			.collect(Collectors.toList());
		PreUtil.findChildren(sysDepts, depts);
		return sysDepts;
	}


	@Override
	public boolean updateDeptById(DeptDTO entity) {
		SysDept sysDept = new SysDept();
		BeanUtils.copyProperties(entity, sysDept);
		sysDept.setUpdateTime(LocalDateTime.now());
		return this.updateById(sysDept);
	}


	@Transactional(rollbackFor = Exception.class)
	@Override
	public boolean removeById(Serializable id) {
		// 部门层级删除
		List<Integer> idList = this.list(Wrappers.<SysDept>query().lambda().eq(SysDept::getParentId, id)).stream().map(SysDept::getDeptId).collect(Collectors.toList());
		// 删除自己
		idList.add((Integer) id);
		return super.removeByIds(idList);
	}

	@Override
	public String selectDeptNameByDeptId(int deptId) {
		return baseMapper.selectOne(Wrappers.<SysDept>query().lambda().select(SysDept::getName).eq(SysDept::getDeptId, deptId)).getName();
	}

	@Override
	public List<SysDept> selectDeptListBydeptName(String deptName) {
		return null;
	}

	@Override
	public List<Integer> selectDeptIds(int deptId) {
		SysDept department = this.getDepartment(deptId);
		List<Integer> deptIdList = new ArrayList<>();
		if (department != null) {
			deptIdList.add(department.getDeptId());
			addDeptIdList(deptIdList, department);
		}
		return deptIdList;
	}

	@Override
	public List<DeptTreeVo> getDeptTree() {
		List<SysDept> depts = baseMapper.selectList(Wrappers.<SysDept>query().select("dept_id", "name", "parent_id", "sort", "create_time"));
		List<DeptTreeVo> collect = depts.stream().filter(sysDept -> sysDept.getParentId() == 0 || ObjectUtil.isNull(sysDept.getParentId()))
			.map(sysDept -> {
				DeptTreeVo deptTreeVo = new DeptTreeVo();
				deptTreeVo.setId(sysDept.getDeptId());
				deptTreeVo.setLabel(sysDept.getName());
				return deptTreeVo;

			}).collect(Collectors.toList());

		PreUtil.findChildren1(collect, depts);
		return collect;
	}


	/**
	 * 根据部门ID获取该部门及其下属部门树
	 */
	private SysDept getDepartment(Integer deptId) {
		List<SysDept> departments = baseMapper.selectList(Wrappers.<SysDept>query().select("dept_id", "name", "parent_id", "sort", "create_time"));
		Map<Integer, SysDept> map = departments.stream().collect(
			Collectors.toMap(SysDept::getDeptId, department -> department));

		for (SysDept dept : map.values()) {
			SysDept parent = map.get(dept.getParentId());
			if (parent != null) {
				List<SysDept> children = parent.getChildren() == null ? new ArrayList<>() : parent.getChildren();
				children.add(dept);
				parent.setChildren(children);
			}
		}
		return map.get(deptId);
	}

	private void addDeptIdList(List<Integer> deptIdList, SysDept department) {
		List<SysDept> children = department.getChildren();
		if (children != null) {
			for (SysDept d : children) {
				deptIdList.add(d.getDeptId());
				addDeptIdList(deptIdList, d);
			}
		}
	}


}
