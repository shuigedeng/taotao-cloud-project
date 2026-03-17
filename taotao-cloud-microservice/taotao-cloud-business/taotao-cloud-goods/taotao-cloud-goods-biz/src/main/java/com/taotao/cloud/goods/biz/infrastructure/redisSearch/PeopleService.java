package com.taotao.cloud.goods.biz.infrastructure.redisSearch;

import com.redis.om.spring.search.stream.EntityStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

/**
 * PeopleService
 *
 * @author shuigedeng
 * @version 2026.04
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
