package com.taotao.cloud.sys.biz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.taotao.cloud.sys.api.vo.dept.DeptTreeVO;
import com.taotao.cloud.sys.biz.entity.system.Dept;
import com.taotao.cloud.sys.biz.mapper.IDeptMapper;
import com.taotao.cloud.sys.biz.mapstruct.IDeptMapStruct;
import com.taotao.cloud.sys.biz.repository.cls.DeptRepository;
import com.taotao.cloud.sys.biz.repository.inf.IDeptRepository;
import com.taotao.cloud.sys.biz.service.IDeptService;
import com.taotao.cloud.web.base.service.BaseSuperServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * DeptServiceImpl
 *
 * @author shuigedeng
 * @since 2020-10-16 15:54:05
 * @since 1.0
 */
@Service
public class DeptServiceImpl extends
	BaseSuperServiceImpl<IDeptMapper, Dept, DeptRepository, IDeptRepository, Long>
	implements IDeptService {

	@Override
	public List<DeptTreeVO> tree() {
		LambdaQueryWrapper<Dept> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.orderByDesc(Dept::getSortNum);
		List<Dept> list = list(queryWrapper);

		return IDeptMapStruct.INSTANCE.deptListToVoList(list)
			.stream()
			.filter(Objects::nonNull)
			.peek(e -> {
				e.setKey(e.getId());
				e.setValue(e.getId());
				e.setTitle(e.getName());
			})
			.collect(Collectors.toList());
	}
}
