/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.data.mybatis.plus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.data.mybatis.plus.entity.MpSuperEntity;
import com.taotao.cloud.data.mybatis.plus.mapper.MpSuperMapper;
import java.io.Serializable;
import java.util.List;
import org.apache.poi.ss.formula.functions.T;

/**
 * 基于MP的 IService 新增了2个方法： saveBatchSomeColumn、updateAllById 其中： 1，updateAllById 执行后，会清除缓存
 * 2，saveBatchSomeColumn 批量插入
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 21:21:51
 */
public interface MpSuperService<T extends MpSuperEntity<I>, I extends Serializable> extends IService<T> {

	/**
	 * 批量保存数据
	 * <p>
	 * 注意：该方法仅仅测试过mysql
	 *
	 * @return boolean
	 * @author shuigedeng
	 * @since 2021-09-02 21:22:06
	 */
	default boolean saveBatchSomeColumn(List<T> entityList) {
		if (entityList.isEmpty()) {
			return true;
		}
		if (entityList.size() > 5000) {
			throw new BusinessException("太多数据啦");
		}
		return SqlHelper.retBool(((MpSuperMapper) getBaseMapper()).insertBatchSomeColumn(entityList));
	}

	/**
	 * 根据id修改 entity 的所有字段
	 *
	 * @param entity entity
	 * @return boolean
	 * @author shuigedeng
	 * @since 2021-09-02 21:22:14
	 */
	boolean updateAllById(T entity);

	boolean deleteById(I id);

}
