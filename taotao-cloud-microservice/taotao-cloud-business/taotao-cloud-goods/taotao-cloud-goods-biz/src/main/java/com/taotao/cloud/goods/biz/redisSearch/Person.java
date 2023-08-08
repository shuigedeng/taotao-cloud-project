package com.taotao.cloud.goods.biz.redisSearch;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.geo.Point;
import com.redis.om.spring.annotations.Document;
import com.redis.om.spring.annotations.Indexed;
import com.redis.om.spring.annotations.Searchable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "of")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Data
@Document
public class Person {
  // Id Field, also indexed
  @Id
  @Indexed
  private String id;

  // Indexed for exact text matching
  @Indexed @NonNull
  private String firstName;

  @Indexed @NonNull
  private String lastName;

  //Indexed for numeric matches
  @Indexed @NonNull
  private Integer age;

  //Indexed for Full Text matches
  @Searchable @NonNull
  private String personalStatement;

  //Indexed for Geo Filtering
  @Indexed @NonNull
  private Point homeLoc;

  // Nest indexed object
  @Indexed @NonNull
  private Address address;

  @Indexed @NonNull
  private Set<String> skills;
}
