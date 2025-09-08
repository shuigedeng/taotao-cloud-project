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

package com.taotao.cloud.sys.biz.service.feign.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.querydsl.core.types.Predicate;
import com.taotao.boot.cache.redis.model.CacheKey;
import com.taotao.boot.lock.support.DistributedLock;
import com.taotao.boot.webagg.mapper.BaseSuperMapper;
import com.taotao.boot.webagg.repository.BaseClassSuperRepository;
import com.taotao.cloud.sys.biz.mapper.IUserMapper;
import com.taotao.cloud.sys.biz.model.entity.system.User;
import com.taotao.cloud.sys.biz.repository.cls.UserRepository;
import com.taotao.cloud.sys.biz.repository.inf.IUserRepository;
import com.taotao.cloud.sys.biz.service.business.IUserService;
import com.taotao.cloud.sys.biz.service.feign.IFeignUserService;
import com.taotao.boot.webagg.service.impl.BaseSuperServiceImpl;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import lombok.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

/**
 * DictServiceImpl
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 20:26:36
 */
@Service
@AllArgsConstructor
public class FeignUserServiceImpl
        implements IFeignUserService {

    private final IUserService userService;

    @Override
    public <T> T test123(T t) {
        return t;
    }

	@Override
	public BaseSuperMapper<User, Long> im() {
		return null;
	}

	@Override
	public BaseClassSuperRepository<User, Long> cr() {
		return null;
	}

	@Override
	public JpaRepository<User, Long> ir() {
		return null;
	}

	@Override
	public JdbcTemplate jdbcTemplate() {
		return null;
	}

	@Override
	public JdbcClient jdbcClient() {
		return null;
	}

	@Override
	public void refreshCache() {

	}

	@Override
	public void clearCache() {

	}

	@Override
	public User getByIdCache(Long id) {
		return null;
	}

	@Override
	public User getByKey(CacheKey key, Function<CacheKey, Object> loader) {
		return null;
	}

	@Override
	public List<User> findByIds(Collection<? extends Serializable> ids,
		Function<Collection<? extends Serializable>, Collection<User>> loader) {
		return List.of();
	}

	@Override
	public boolean saveIdempotency(User entity, DistributedLock lock, String lockKey, Predicate predicate,
		Wrapper<User> countWrapper, String msg) {
		return false;
	}

	@Override
	public boolean saveIdempotency(User entity, DistributedLock lock, String lockKey, Predicate predicate,
		Wrapper<User> countWrapper) {
		return false;
	}

	@Override
	public boolean saveOrUpdateIdempotency(User entity, DistributedLock lock, String lockKey, Predicate predicate,
		Wrapper<User> countWrapper, String msg) {
		return false;
	}

	@Override
	public boolean saveOrUpdateIdempotency(User entity, DistributedLock lock, String lockKey, Predicate predicate,
		Wrapper<User> countWrapper) {
		return false;
	}

	@Override
	public String getColumnName(SFunction<User, ?> function) {
		return "";
	}

	@Override
	public boolean deleteByFields(SFunction<User, ?> field, Collection<?> fieldValues) {
		return false;
	}

	@Override
	public boolean deleteByField(SFunction<User, ?> field, Object fieldValue) {
		return false;
	}

	@Override
	public boolean deleteByIds(Collection<? extends Serializable> idList) {
		return false;
	}

	@Override
	public boolean deleteById(Serializable id) {
		return false;
	}

	@Override
	public Long countByField(SFunction<User, ?> field, Object fieldValue) {
		return 0L;
	}

	@Override
	public boolean existedByField(SFunction<User, ?> field, Object fieldValue, Serializable id) {
		return false;
	}

	@Override
	public boolean existedByField(SFunction<User, ?> field, Object fieldValue) {
		return false;
	}

	@Override
	public boolean existedById(Serializable id) {
		return false;
	}

	@Override
	public List<User> findAllByFields(SFunction<User, ?> field, Collection<? extends Serializable> fieldValues) {
		return List.of();
	}

	@Override
	public List<User> findAllByField(SFunction<User, ?> field, Object fieldValue) {
		return List.of();
	}

	@Override
	public List<User> findAllByIds(Collection<? extends Serializable> idList) {
		return List.of();
	}

	@Override
	public Optional<User> findByField(SFunction<User, ?> field, Object fieldValue) {
		return Optional.empty();
	}

	@Override
	public Optional<User> findById(Serializable id) {
		return Optional.empty();
	}

	@Override
	public List<User> findAll() {
		return List.of();
	}

	@Override
	public boolean updateByField(User user, SFunction<User, ?> field, Object fieldValue) {
		return false;
	}

	@Override
	public boolean updateAllById(Collection<User> entityList) {
		return false;
	}

	@Override
	public List<User> saveAll(List<User> list) {
		return List.of();
	}

	@Override
	public boolean saveBatch(Collection<User> entityList, int batchSize) {
		return false;
	}

	@Override
	public boolean saveOrUpdateBatch(Collection<User> entityList, int batchSize) {
		return false;
	}

	@Override
	public boolean updateBatchById(Collection<User> entityList, int batchSize) {
		return false;
	}

	@Override
	public boolean saveOrUpdate(User entity) {
		return false;
	}

	@Override
	public User getOne(Wrapper<User> queryWrapper, boolean throwEx) {
		return null;
	}

	@Override
	public Optional<User> getOneOpt(Wrapper<User> queryWrapper, boolean throwEx) {
		return Optional.empty();
	}

	@Override
	public Map<String, Object> getMap(Wrapper<User> queryWrapper) {
		return Map.of();
	}

	@Override
	public <V> V getObj(Wrapper<User> queryWrapper, Function<? super Object, V> mapper) {
		return null;
	}

	@Override
	public BaseMapper<User> getBaseMapper() {
		return null;
	}

	@Override
	public Class<User> getEntityClass() {
		return null;
	}
}
