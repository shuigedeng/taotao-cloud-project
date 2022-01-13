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
 * @date 2019-11-15 20:50
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Accessors(chain = true)
public class FundPublic {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(nullable = false)
	private String fundCode;
	
	@Column(nullable = false)
	private String fundName;

}
