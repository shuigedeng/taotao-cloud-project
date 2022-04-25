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
package com.taotao.cloud.reactive.order.service;

import com.taotao.cloud.reactive.order.bean.Employee;
import com.taotao.cloud.reactive.order.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * EmployeeServiceImpl
 *
 * @author shuigedeng
 * 
 * @since 2021/03/03 16:56
 */
@Service
public class EmployeeServiceImpl {

	@Autowired
	private EmployeeRepository employeeDao;

	public Flux<Employee> findAll() {
		return employeeDao.findAll();
	}

	public Mono<Employee> findById(long id) {
		return employeeDao.findById(id);
	}

	public Flux<Employee> findByName(String name) {
		return employeeDao.findByName(name);
	}

	public Mono<Employee> save(Employee employee) {
		return employeeDao.save(employee);
	}

	public Mono<Void> update(Employee employee) {
		return findById(employee.getId())
			.map(em -> {
				Employee e = new Employee();
				e.setId(em.getId());
				return e;
			})
			.flatMap(employeeDao::save)
			.then();
	}

	public Mono<Void> delete(Employee employee) {
		return employeeDao.delete(employee);
	}

	public Mono<Void> deleteById(long id) {
		return employeeDao.deleteById(id);
	}
}
