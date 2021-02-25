package com.taotao.cloud.uc.biz.mybatis.entity;// package com.taotao.cloud.uc.biz.entity;
//
// import com.baomidou.mybatisplus.annotation.TableName;
// import com.taotao.cloud.data.mybatis.plus.mapper.SuperEntity;
// import lombok.Data;
// import lombok.EqualsAndHashCode;
// import lombok.NoArgsConstructor;
// import lombok.ToString;
// import lombok.experimental.Accessors;
// import lombok.experimental.SuperBuilder;
//
// /**
//  * 官方文档中get_user_info接口的用户信息返回实体
//  *
//  * @author dengtao
//  * @date 2020/4/29 21:02
//  */
// @Data
// @SuperBuilder
// @Accessors(chain= true)
// @EqualsAndHashCode(callSuper = false)
// @ToString(callSuper = true)
// @NoArgsConstructor
// @TableName(value = "sys_qq_user")
// public class SysQqUser  extends SuperEntity {
//
//     private String openId;
//
//     /**
//      * 返回码
//      */
//     private String ret;
//
//     /**
//      * 如果ret<0，会有相应的错误信息提示，返回数据全部用UTF-8编码。
//      */
//     private String msg;
//
//     /**
//      * 不知道什么东西，文档上没写，但是实际api返回里有。
//      */
//     private String isLost;
//     /**
//      * 省(直辖市)
//      */
//     private String province;
//     /**
//      * 市(直辖市区)
//      */
//     private String city;
//     /**
//      * 出生年月
//      */
//     private String year;
//     /**
//      * 用户在QQ空间的昵称。
//      */
//     private String nickname;
//     /**
//      * 大小为30×30像素的QQ空间头像URL。
//      */
//     private String figureUrl;
//     /**
//      * 大小为50×50像素的QQ空间头像URL。
//      */
//     private String figureUrl1;
//     /**
//      * 大小为100×100像素的QQ空间头像URL。
//      */
//     private String figureUrl2;
//     /**
//      * 大小为40×40像素的QQ头像URL。
//      */
//     private String figureUrlQq1;
//     /**
//      * 大小为100×100像素的QQ头像URL。需要注意，不是所有的用户都拥有QQ的100×100的头像，但40×40像素则是一定会有。
//      */
//     private String figureUrlQq2;
//     /**
//      * 性别。 如果获取不到则默认返回”男”
//      */
//     private String gender;
//     /**
//      * 标识用户是否为黄钻用户（0：不是；1：是）。
//      */
//     private String isYellowVip;
//     /**
//      * 标识用户是否为黄钻用户（0：不是；1：是）
//      */
//     private String vip;
//     /**
//      * 黄钻等级
//      */
//     private String yellowVipLevel;
//     /**
//      * 黄钻等级
//      */
//     private String level;
//     /**
//      * 标识是否为年费黄钻用户（0：不是； 1：是）
//      */
//     private String isYellowYearVip;
//
// }
