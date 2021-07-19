/**
 * Project Name: projects
 * Package Name: com.taotao.cloud.reactive.uc.entity
 * Date: 2020/9/10 14:05
 * Author: shuigedeng
 */
package com.taotao.cloud.reactive.uc.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;

/**
 * <br>
 *
 * @author shuigedeng
 * @version v1.0.0
 * @date 2020/9/10 14:05
 */
@Data
@Table(value = "user")
public class User implements Serializable {
    private static final long serialVersionUID = -558043294043707772L;

    @Id
    private String id;
    private String nickname;
    private String phoneNumber;
    private Integer gender;
}
