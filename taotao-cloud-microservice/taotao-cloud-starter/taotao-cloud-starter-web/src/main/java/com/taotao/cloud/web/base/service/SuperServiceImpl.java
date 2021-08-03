package com.taotao.cloud.web.base.service;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.web.base.mapper.SuperMapper;
import java.lang.reflect.ParameterizedType;
import org.springframework.transaction.annotation.Transactional;

/**
 * 不含缓存的Service实现
 * <p>
 * <p>
 * 2，removeById：重写 ServiceImpl 类的方法，删除db 3，removeByIds：重写 ServiceImpl 类的方法，删除db 4，updateAllById：
 * 新增的方法： 修改数据（所有字段） 5，updateById：重写 ServiceImpl 类的方法，修改db后
 *
 * @param <M> Mapper
 * @param <T> 实体
 * @author zuihou¬
 * @date 2020年02月27日18:15:17
 */
public class SuperServiceImpl<M extends SuperMapper<T>, T> extends ServiceImpl<M, T> implements
	SuperService<T> {

	private Class<T> entityClass = null;

	public SuperMapper getSuperMapper() {
		if (baseMapper instanceof SuperMapper) {
			return baseMapper;
		}
		throw new BusinessException("未查询到mapper");
	}

	@Override
	public Class<T> getEntityClass() {
		if (entityClass == null) {
			this.entityClass = (Class) ((ParameterizedType) this.getClass()
				.getGenericSuperclass()).getActualTypeArguments()[1];
		}
		return this.entityClass;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean save(T model) {
		return super.save(model);
	}

	/**
	 * 处理新增相关处理
	 *
	 * @param model 实体
	 * @return 是否成功
	 */
	protected Result<T> handlerSave(T model) {
		return Result.success(model);
	}

	/**
	 * 处理修改相关处理
	 *
	 * @param model 实体
	 * @return 是否成功
	 */
	protected Result<T> handlerUpdateAllById(T model) {
		return Result.success(model);
	}

	/**
	 * 处理修改相关处理
	 *
	 * @param model 实体
	 * @return 是否成功
	 */
	protected Result<T> handlerUpdateById(T model) {
		return Result.success(model);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean updateAllById(T model) {
		return SqlHelper.retBool(getSuperMapper().updateAllById(model));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean updateById(T model) {
		return super.updateById(model);
	}
}
