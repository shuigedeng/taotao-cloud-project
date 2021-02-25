// package com.taotao.cloud.uc.biz.service.impl;
//
//
// import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
// import com.baomidou.mybatisplus.core.toolkit.Wrappers;
// import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
// import com.taotao.cloud.common.constant.SecurityConstant;
// import com.taotao.cloud.common.enums.ResultEnum;
// import com.taotao.cloud.common.enums.UserSexTypeEnum;
// import com.taotao.cloud.common.enums.UserTypeEnum;
// import com.taotao.cloud.common.model.PageResult;
// import com.taotao.cloud.data.mybatis.plus.service.impl.SuperServiceImpl;
// import com.taotao.cloud.uc.biz.entity.SysGiteeUser;
// import com.taotao.cloud.uc.biz.entity.SysUser;
// import com.taotao.cloud.uc.biz.entity.SysUserSocial;
// import com.taotao.cloud.uc.biz.mapper.SysGiteeUserMapper;
// import com.taotao.cloud.uc.biz.service.ISysGiteeUserService;
// import com.taotao.cloud.uc.biz.service.ISysUserService;
// import com.taotao.cloud.uc.biz.service.ISysUserSocialService;
// import lombok.AllArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;
//
// import java.util.Collections;
// import java.util.Map;
//
// /**
//  * gitee用户表
//  *
//  * @author taotao
//  * @date 2020-05-14 14:36:39
//  */
// @Slf4j
// @Service
// @AllArgsConstructor
// public class SysGiteeUserServiceImpl extends SuperServiceImpl<SysGiteeUserMapper, SysGiteeUser> implements ISysGiteeUserService {
//     private final ISysUserSocialService sysUserSocialService;
//     private final ISysUserService sysUserService;
//
//     /**
//      * 列表
//      *
//      * @param params
//      * @return
//      */
//     @Override
//     public PageResult<SysGiteeUser> findList(Map<String, Object> params) {
// //        Page<SysGiteeUser> page = new Page<>(MapUtils.getInteger(params, "page"), MapUtils.getInteger(params, "limit"));
//         Page<SysGiteeUser> page = new Page<>(10, 20);
//         LambdaQueryWrapper<SysGiteeUser> giteeUserLambdaQueryWrapper = Wrappers.<SysGiteeUser>lambdaQuery();
//         Page<SysGiteeUser> pageResult = baseMapper.selectPage(page, giteeUserLambdaQueryWrapper);
//
//         PageResult result = PageResult.builder().currentPage(page.getCurrent()).total(pageResult.getTotal())
//                 .code(ResultEnum.SUCCESS.getCode())
//                 .pageSize(pageResult.getSize())
//                 .data(Collections.singletonList(pageResult.getRecords()))
//                 .build();
//
//         return result;
//     }
//
//     @Override
//     @Transactional(rollbackFor = Exception.class)
//     public Boolean add(SysGiteeUser sysGiteeUser) {
//         int id = sysGiteeUser.getProviderUserId();
//
//         saveOrUpdate(sysGiteeUser);
//         Boolean exist = sysUserSocialService.selectOne(String.valueOf(id), SecurityConstant.LOGIN_GITEE);
//         if (!exist) {
//             // 不存在第三方用户信息
//             Long userId = saveUser(sysGiteeUser);
//             saveSocialUser(sysGiteeUser, userId);
//         }
//         return true;
//     }
//
//     private Long saveUser(SysGiteeUser sysGiteeUser) {
//         SysUser user = new SysUser();
//         user.setUsername(sysGiteeUser.getLogin());
//         user.setNickname(sysGiteeUser.getName());
//         user.setType(UserTypeEnum.APP.getValue());
//         user.setSex(UserSexTypeEnum.UNKNOWN.getValue());
//         user.setPassword("");
//         user.setMobile("");
//         user.setEmail(sysGiteeUser.getEmail());
//         user.setDeptId(null);
//         user.setJobId(null);
//         user.setAvatar(sysGiteeUser.getAvatarUrl());
// //        sysUserService.save(user);
//         return sysGiteeUser.getId();
//     }
//
//     private void saveSocialUser(SysGiteeUser sysGiteeUser, Long userId) {
//         SysUserSocial social = new SysUserSocial();
// //        social.setUserId(userId);
//         social.setProviderId(SecurityConstant.LOGIN_GITEE);
//         social.setProviderUserId(String.valueOf(sysGiteeUser.getProviderUserId()));
//         social.setDisplayName(sysGiteeUser.getLogin());
//         social.setImageUrl(sysGiteeUser.getAvatarUrl());
// //        sysUserSocialService.save(social);
//     }
// }
