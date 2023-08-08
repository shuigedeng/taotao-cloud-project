package com.taotao.cloud.goods.biz.redisSearch;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.redis.om.spring.search.stream.EntityStream;

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

  // Find people by age range
//  public Iterable<Person> findByAgeBetween(int minAge, int maxAge) {
//    return entityStream //
//            .of(Person.class) //
//            .filter(Person$.AGE.between(minAge, maxAge)) //
//            .sorted(Person$.AGE, SortOrder.ASC) //
//            .collect(Collectors.toList());
//  }

}
