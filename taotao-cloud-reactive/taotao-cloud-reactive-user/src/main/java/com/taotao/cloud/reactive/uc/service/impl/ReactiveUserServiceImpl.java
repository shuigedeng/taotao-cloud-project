/**
 * Project Name: projects
 * Package Name: com.taotao.cloud.reactive.uc.service.impl
 * Date: 2020/9/10 14:14
 * Author: dengtao
 */
package com.taotao.cloud.reactive.uc.service.impl;

import com.taotao.cloud.reactive.uc.entity.User;
import com.taotao.cloud.reactive.uc.repository.ReactiveUserSortingRepository;
import com.taotao.cloud.reactive.uc.service.ReactiveUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * <br>
 *
 * @author dengtao
 * @version v1.0.0
 * @date 2020/9/10 14:14
 */
@Service
public class ReactiveUserServiceImpl implements ReactiveUserService {
    @Autowired
    private ReactiveUserSortingRepository reactiveUserSortingRepository;

    @Override
    public Mono<User> findById(String userId) {
        return reactiveUserSortingRepository.findById(userId);
    }

    @Override
    public Mono<User> saveUser(User user) {
        Mono<User> userMono = reactiveUserSortingRepository.save(user)
                .log();
        return userMono;
    }
}
