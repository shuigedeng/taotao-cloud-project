package com.taotao.cloud.sys.biz.springboot.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Date;

/**
 * @author duhongming
 * @version 1.0
 * @description TODO
 * @date 2019-12-03 20:30
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Accessors(chain = true)
public class ImookCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String directionCode;

    @Column(nullable = false)
    private String directionName;

    @Column(nullable = false)
    private String classifyCode;

    @Column(nullable = false)
    private String classifyName;

    @Column(nullable = false)
    private String courseDifficulty;

    @Column(nullable = false)
    private String courseTitle;

    @Column(nullable = false)
    private String courseDesc;

    @Column(nullable = false)
    private Integer courseStudents;

    @Column(nullable = false)
    private Integer courseId;

    @Column(nullable = false)
    private String courseUrl;

    @Column(nullable = false)
    private Date cmpTime;

}
