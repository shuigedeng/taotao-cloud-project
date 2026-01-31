package com.taotao.cloud.goods.biz.redisSearch;

import com.redis.om.spring.annotations.Document;
import com.redis.om.spring.annotations.Indexed;
import com.redis.om.spring.annotations.Searchable;
import lombok.*;
import lombok.experimental.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.geo.Point;

import java.util.Set;

/**
 * Person
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
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
    @Indexed
    @NonNull
    private String firstName;

    @Indexed
    @NonNull
    private String lastName;

    //Indexed for numeric matches
    @Indexed
    @NonNull
    private Integer age;

    //Indexed for Full Text matches
    @Searchable
    @NonNull
    private String personalStatement;

    //Indexed for Geo Filtering
    @Indexed
    @NonNull
    private Point homeLoc;

    @Indexed
    @NonNull
    private Set<String> skills;
}
