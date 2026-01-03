package com.taotao.cloud.goods.biz.redisSearch;

import com.redis.om.spring.annotations.Document;
import com.redis.om.spring.annotations.Indexed;
import com.redis.om.spring.annotations.Searchable;
import lombok.*;
import lombok.experimental.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.geo.Point;

import java.util.HashSet;
import java.util.Set;

/**
 * Company
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor(staticName = "of")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Document
public class Company {

    //@Id：声明主键，RedisOM将会通过全类名:ID这样的键来存储数据；
    @Id
    private String id;
    //@Searchable：声明可以搜索的索引，通常用在文本类型上。
    @Searchable
    private String name;
    //@Indexed：声明索引，通常用在非文本类型上；
    @Indexed
    private Point location;
    @Indexed
    private Set<String> tags = new HashSet<>();
    @Indexed
    private Integer numberOfEmployees;
    @Indexed
    private Integer yearFounded;
    private String url;
    private boolean publiclyListed;

}
