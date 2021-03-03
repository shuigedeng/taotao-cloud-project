/**
 * Project Name: projects
 * Package Name: com.taotao.cloud.reactive.uc.controller
 * Date: 2020/9/10 14:13
 * Author: dengtao
 */
package com.taotao.cloud.reactive.uc.controller;

import com.taotao.cloud.reactive.uc.entity.User;
import com.taotao.cloud.reactive.uc.repository.ReactiveUserSortingRepository;
import com.taotao.cloud.reactive.uc.service.ReactiveUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * <br>
 *
 * @author dengtao
 * @version v1.0.0
 * @date 2020/9/10 14:13
 */
@RestController
@RequestMapping("/user")
public class ReactiveUserController {
    @Autowired
    private ReactiveUserService reactiveUserService;

    @GetMapping("/{userId}")
    public Mono<User> findUserById(@PathVariable String userId) {
        return reactiveUserService.findById(userId);
    }

    @PostMapping("")
    public Mono<User> saveUser(@RequestBody User user) {
        return reactiveUserService.saveUser(user);
    }

}
