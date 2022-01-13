package com.taotao.cloud.sys.biz.springboot.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;

/**
 * @author duhongming
 * @version 1.0
 * @description TODO
 * @date 2019-12-08 11:08
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Accessors(chain = true)
public class AutohomeBrandIcon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 车标
     */
    @Column(nullable = false)
    private String carBrandIconUrl;
    @Column(nullable = false)
    private byte[] carBrandIconbyte;
}
