package com.taotao.cloud.sys.biz.manager;

import com.taotao.cloud.sys.biz.mapper.IUserMapper;
import com.taotao.cloud.sys.biz.repository.inf.IUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserManager {

	private final IUserMapper userMapper;
	private final IUserRepository userRepository;

	//@Transactional(rollbackFor = Throwable.class)
	//public void upOrDown(DepartmentEntity departmentEntity ,DepartmentEntity swapEntity){
	//	Long departmentSort = departmentEntity.getSort();
	//	departmentEntity.setSort(swapEntity.getSort());
	//	departmentDao.updateById(departmentEntity);
	//	swapEntity.setSort(departmentSort);
	//	departmentDao.updateById(swapEntity);
	//}
}
