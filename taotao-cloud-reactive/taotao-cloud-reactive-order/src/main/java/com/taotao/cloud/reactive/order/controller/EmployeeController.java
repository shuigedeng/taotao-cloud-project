/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.reactive.order.controller;

import com.taotao.cloud.reactive.order.bean.Employee;
import com.taotao.cloud.reactive.order.service.EmployeeServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * EmployeeController
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/03/03 16:56
 */
@RestController
@RequestMapping("/employee")
public class EmployeeController {

	@Autowired
	private EmployeeServiceImpl employeeService;

	@GetMapping(path = "/all")
	public Flux<Employee> findAll() {
		return employeeService.findAll();
	}

//	@GetMapping (path = "/{id}")
//	public Mono<Employee> findById(@PathVariable("id") long id) {
//		return employeeService.findById(id);
//	}
//
//	@GetMapping ("/find")
//	public Flux<Employee> findByName(@RequestParam("name") String name) {
//		return employeeService.findByName(name);
//	}
//
//	@PostMapping
//	@ResponseStatus(HttpStatus.CREATED)
//	public Mono<Employee> save(@RequestBody Employee employee) {
//		return employeeService.save(employee);
//	}
//
//	@PutMapping
//	@ResponseStatus(HttpStatus.NO_CONTENT)
//	public Mono<Void> update (@RequestBody Employee employee) {
//		return employeeService.update(employee);
//	}
//
//	@DeleteMapping
//	@ResponseStatus(HttpStatus.NO_CONTENT)
//	public Mono<Void> delete (@RequestBody Employee employee) {
//		return employeeService.delete(employee);
//	}
//
//	@DeleteMapping(path = "/{id}")
//	@ResponseStatus(HttpStatus.NO_CONTENT)
//	public Mono<Void> deleteById (@PathVariable long id) {
//		return employeeService.deleteById(id);
//	}
}
