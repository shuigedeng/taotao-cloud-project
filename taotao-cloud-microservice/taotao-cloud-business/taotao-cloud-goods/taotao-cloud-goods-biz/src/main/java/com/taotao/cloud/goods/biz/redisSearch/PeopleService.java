package com.taotao.cloud.goods.biz.redisSearch;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.redis.om.spring.search.stream.EntityStream;

/**
 * PeopleService
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
@Service
public class PeopleService {

    @Autowired
    EntityStream entityStream;

    // Find all people
    public Iterable<Person> findAllPeople() {
        return entityStream //
                .of(Person.class) //
                .collect(Collectors.toList());
    }

}
