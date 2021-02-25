package com.taotao.cloud.uc.biz.mybatis.entity;// package com.taotao.cloud.uc.biz.entity;
//
// import com.baomidou.mybatisplus.annotation.TableName;
// import com.taotao.cloud.common.enums.UserSexTypeEnum;
// import com.taotao.cloud.common.enums.UserTypeEnum;
// import com.taotao.cloud.data.mybatis.plus.mapper.SuperEntity;
// import lombok.*;
// import lombok.experimental.Accessors;
// import lombok.experimental.SuperBuilder;
//
// /**
//  * 用户表
//  *
//  * @author dengtao
//  * @date 2020/6/15 11:00
//  */
// @Data
// @SuperBuilder
// @Accessors(chain= true)
// @EqualsAndHashCode(callSuper = false)
// @ToString(callSuper = true)
// @NoArgsConstructor
// @TableName("sys_user")
// public class SysUser extends SuperEntity{
//
//     /**
//      * 昵称
//      */
//     private String nickname;
//
//     /**
//      * 真实用户名
//      */
//     private String username;
//
//     /**
//      * 手机号
//      */
//     private String mobile;
//
//     /**
//      * 密码
//      */
//     private String password;
//
//     /**
//      * 用户类型 1前端用户 2商户用户 3后台管理用户
//      *
//      * @see UserTypeEnum
//      */
//     private Integer type;
//
//     /**
//      * 性别 1男 2女 0未知
//      *
//      * @see UserSexTypeEnum
//      */
//     private Integer sex;
//
//     /**
//      * 邮箱
//      */
//     private String email;
//
//     /**
//      * 部门ID
//      */
//     private Long deptId;
//
//     /**
//      * 岗位ID
//      */
//     private Long jobId;
//
//     /**
//      * 头像
//      */
//     private String avatar;
//
//     /**
//      * 1-正常，2-锁定
//      */
//     private int lockFlag;
//
// }
