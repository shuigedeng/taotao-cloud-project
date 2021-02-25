// package com.taotao.cloud.uc.biz.service.impl;
//
// import cn.hutool.core.bean.BeanUtil;
// import cn.hutool.core.collection.CollectionUtil;
// import cn.hutool.core.util.ObjectUtil;
// import cn.hutool.core.util.StrUtil;
// import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
// import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
// import com.baomidou.mybatisplus.core.metadata.IPage;
// import com.baomidou.mybatisplus.core.toolkit.Wrappers;
// import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
// import com.taotao.cloud.common.enums.UserTypeEnum;
// import com.taotao.cloud.common.exception.BaseException;
// import com.taotao.cloud.core.model.SecurityUser;
// import com.taotao.cloud.data.mybatis.plus.datascope.DataScope;
// import com.taotao.cloud.data.mybatis.plus.service.impl.SuperServiceImpl;
// import com.taotao.cloud.uc.api.dto.RepeatCheckDTO;
// import com.taotao.cloud.uc.api.dto.UserAddDTO;
// import com.taotao.cloud.uc.api.dto.UserUpdateDTO;
// import com.taotao.cloud.uc.api.query.UserListQuery;
// import com.taotao.cloud.uc.api.vo.UserAddVO;
// import com.taotao.cloud.uc.biz.entity.SysUser;
// import com.taotao.cloud.uc.biz.entity.SysUserRole;
// import com.taotao.cloud.uc.biz.mapper.SysUserMapper;
// import com.taotao.cloud.uc.biz.service.*;
// import lombok.extern.slf4j.Slf4j;
// import org.apache.commons.lang3.StringUtils;
// import org.springframework.beans.BeanUtils;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;
//
// import java.util.Optional;
// import java.util.Set;
// import java.util.stream.Collectors;
//
// /**
//  * 用户表 服务实现类
//  *
//  * @author dengtao
//  * @date 2020/4/30 13:22
//  */
// @Service
// @Slf4j
// public class SysUserServiceImpl extends SuperServiceImpl<SysUserMapper, SysUser> implements ISysUserService {
//     @Autowired
//     private ISysUserRoleService userRoleService;
//     @Autowired
//     private ISysDeptService deptService;
//     @Autowired
//     private ISysJobService jobService;
//     @Autowired
//     private ISysMenuService menuService;
//     @Autowired
//     private ISysRoleService roleService;
//
//     private static final String ROLE_PREFIX = "ROLE_";
//
//     @Override
//     public IPage<SysUser> getUsersWithRolePage(UserListQuery userListQuery) {
//         Page<SysUser> page = new Page<>(userListQuery.getCurrentPage(), userListQuery.getPageSize());
//         QueryWrapper<SysUser> query = Wrappers.query();
//         Optional.ofNullable(userListQuery.getUserId())
//                 .ifPresent(userId -> query.eq("user.id", userId));
//         Optional.ofNullable(userListQuery.getNickname())
//                 .ifPresent(nickname -> query.like("user.nickname", nickname));
//         Optional.ofNullable(userListQuery.getUsername())
//                 .ifPresent(username -> query.like("user.nickname", username));
//         Optional.ofNullable(userListQuery.getDeptId())
//                 .ifPresent(deptId -> query.eq("user.dept_id", deptId));
//         Optional.ofNullable(userListQuery.getJobId())
//                 .ifPresent(jobId -> query.eq("user.job_id", jobId));
//         Optional.ofNullable(userListQuery.getEmail())
//                 .ifPresent(email -> query.eq("user.email", email));
//         Optional.ofNullable(userListQuery.getMobile())
//                 .ifPresent(mobile -> query.eq("user.mobile", mobile));
//
//         IPage<SysUser> userList = baseMapper.getUserVoListPage(page, query, new DataScope());
// //        userList.getRecords().forEach(user -> user.setRoleList(roleService.findRolesByUserId(user.getId())));
//         return userList;
//     }
//
//     @Transactional(rollbackFor = Exception.class)
//     @Override
//     public UserAddVO insertUser(UserAddDTO userAddDto) {
//         String avatar = userAddDto.getAvatar();
//         if (StrUtil.isBlank(avatar)) {
//             userAddDto.setAvatar("");
//         }
//         SysUser sysUser = new SysUser();
//         BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//         BeanUtils.copyProperties(userAddDto, sysUser);
//         sysUser.setPassword(passwordEncoder.encode("123456"));
//         save(sysUser);
//
//         return UserAddVO.builder()
//                 .username(userAddDto.getUsername())
//                 .mobile(userAddDto.getMobile())
//                 .password("123456")
//                 .build();
//     }
//
//     @Transactional(rollbackFor = Exception.class)
//     public boolean removeUser(Long userId) {
// //        baseMapper.updateById(new SysUser().setId(userId)
// //                .setDelFlag(DelFlagEnum.DELETE.getValue()));
// //        return userRoleService.remove(Wrappers.<SysUserRole>lambdaQuery().eq(SysUserRole::getId, userId));
//         return true;
//     }
//
//     @Transactional(rollbackFor = Exception.class)
//     @Override
//     public boolean updateUser(UserUpdateDTO userUpdateDTO) {
//         SysUser sysUser = new SysUser();
//         BeanUtils.copyProperties(userUpdateDTO, sysUser);
//         baseMapper.updateById(sysUser);
// //        userRoleService.remove(Wrappers.<SysUserRole>lambdaQuery().eq(SysUserRole::getId, sysUser.getId()));
// //        List<SysUserRole> userRoles = userAddDto.getRoleList().stream().map(item -> {
// //            SysUserRole sysUserRole = new SysUserRole();
// //            sysUserRole.setRoleId(item);
// //            sysUserRole.setUserId(sysUser.getId());
// //            return sysUserRole;
// //        }).collect(Collectors.toList());
//
// //        return userRoleService.saveBatch(userRoles);
//         return false;
//     }
//
//     @Override
//     public boolean removeUser(Integer userId) {
//         return false;
//     }
//
//     @Override
//     public boolean restPass(Integer userId, String password) {
// //        return this.updateById(new SysUser().setPassword(password).setId(userId));
//         return true;
//     }
//
//     @Override
//     public SecurityUser findUserInfo(SysUser sysUser) {
//         // 权限集合
//         Set<String> permissions = findPermsByUserId(sysUser.getId());
//         // 角色集合
//         Set<String> roles = findRoleIdByUserId(sysUser.getId());
//         SecurityUser securityUser = new SecurityUser(sysUser.getId(), sysUser.getUsername(),
//                 sysUser.getPassword(), permissions, roles);
//
//         BeanUtil.copyProperties(sysUser, securityUser);
//         return securityUser;
//     }
//
//     @Override
//     public SysUser findUserInByName(String username) {
//         return baseMapper.selectOne(Wrappers.<SysUser>lambdaQuery()
//                 .select(SysUser::getId, SysUser::getUsername, SysUser::getMobile, SysUser::getEmail, SysUser::getPassword, SysUser::getDeptId, SysUser::getJobId, SysUser::getAvatar)
//                 .eq(SysUser::getUsername, username));
//     }
//
//     @Override
//     public Set<String> findPermsByUserId(Long userId) {
//         return menuService.findPermsByUserId(userId).stream().filter(StringUtils::isNotEmpty).collect(Collectors.toSet());
//     }
//
//     @Override
//     public Set<String> findRoleIdByUserId(Long userId) {
//         return userRoleService
//                 .selectUserRoleListByUserId(userId)
//                 .stream()
//                 .map(item -> item.getRoleId().toString())
//                 .collect(Collectors.toSet());
//     }
//
//     @Transactional(rollbackFor = Exception.class)
//     @Override
//     public boolean register(UserAddDTO userAddDTO) {
//         // 查询用户名是否存在
//         SysUser byUserInfoName = findUserByUserIdOrUserNameOrMobile(userAddDTO.getUsername());
//         if (ObjectUtil.isNotNull(byUserInfoName)) {
//             throw new BaseException("账户名已被注册");
//         }
// //        SysUser securityUser = findUserByUserIdOrUserNameOrMobile(userAddDTO.getPhone());
// //        if (ObjectUtil.isNotNull(securityUser)) {
// //            throw new BaseException("手机号已被注册");
// //        }
// //        userAddDTO.setDeptId(6);
// //        userAddDTO.setJobId(4);
// //        userAddDTO.setLockFlag("0");
//         SysUser sysUser = new SysUser();
//         // 对象拷贝
//         BeanUtil.copyProperties(userAddDTO, sysUser);
//         // 加密后的密码
// //        sysUser.setPassword(UcUtil.encode(userDTO.getPassword()));
// //        baseMapper.insertUser(sysUser);
//         SysUserRole sysUserRole = new SysUserRole();
// //        sysUserRole.setRoleId(14);
//         sysUserRole.setUserId(sysUser.getId());
// //        return userRoleService.save(sysUserRole);
//         return true;
//     }
//
//     @Override
//     public boolean updateUserInfo(SysUser sysUser) {
//         return baseMapper.updateById(sysUser) > 0;
//     }
//
//     @Override
//     public SysUser findUserByUserIdOrUserNameOrMobile(String userIdOrUserNameOrMobileOrEmail) {
//         LambdaQueryWrapper<SysUser> select = Wrappers.<SysUser>lambdaQuery()
//                 .eq(SysUser::getUsername, userIdOrUserNameOrMobileOrEmail)
//                 .or()
//                 .eq(SysUser::getMobile, userIdOrUserNameOrMobileOrEmail)
//                 .or()
//                 .eq(SysUser::getId, userIdOrUserNameOrMobileOrEmail)
//                 .or()
//                 .eq(SysUser::getEmail, userIdOrUserNameOrMobileOrEmail)
//                 .in(SysUser::getType, CollectionUtil.newArrayList(UserTypeEnum.COMPANY.getValue(), UserTypeEnum.BACKEND.getValue()));
//         return baseMapper.selectOne(select);
//     }
//
//     @Override
//     public boolean repeatCheck(RepeatCheckDTO repeatCheckDTO) {
//         String fieldVal = repeatCheckDTO.getFieldVal();
//         Integer dataId = repeatCheckDTO.getDataId();
//         LambdaQueryWrapper<SysUser> lambdaQueryWrapper = Wrappers.<SysUser>lambdaQuery()
//                 .eq(SysUser::getUsername, fieldVal)
//                 .or()
//                 .eq(SysUser::getMobile, fieldVal)
//                 .or()
//                 .eq(SysUser::getEmail, fieldVal);
//         // 不为空说明是编辑情况
//         if (ObjectUtil.isNotNull(dataId)) {
//             lambdaQueryWrapper.eq(SysUser::getId, dataId);
//             return baseMapper.selectCount(lambdaQueryWrapper) < 0;
//         }
//         return baseMapper.selectCount(lambdaQueryWrapper) > 0;
//     }
//
//     @Override
//     public SysUser getUserBySocial(String providerId, int providerUserId) {
//         return baseMapper.getUserBySocial(providerId, providerUserId);
//     }
//
// //    @Override
// //    public SysUser findSecurityUserByUser(SysUser sysUser) {
// //        LambdaQueryWrapper<SysUser> select = Wrappers.<SysUser>lambdaQuery()
// //                .select(SysUser::getId, SysUser::getUsername, SysUser::getPassword);
// //        if (StrUtil.isNotEmpty(sysUser.getUsername())) {
// //            select.eq(SysUser::getUsername, sysUser.getUsername());
// //        } else if (StrUtil.isNotEmpty(sysUser.getPhone())) {
// //            select.eq(SysUser::getPhone, sysUser.getPhone());
// //        } else if (ObjectUtil.isNotNull(sysUser.getId()) && sysUser.getId() != 0) {
// //            select.eq(SysUser::getId, sysUser.getId());
// //        }
// //
// //
// //        return baseMapper.selectOne(select);
// //    }
//
// //    @Override
// //    public boolean doPostSignUp(SysUser user) {
//     // 进行账号校验
// //        SysUser sysUser = findSecurityUserByUser(new SysUser().setUsername(user.getUsername()));
// //        if (ObjectUtil.isNull(sysUser)) {
// //            throw new BaseException("账号不存在");
// //        }
// //        Integer userId = sysUser.getId();
// //        try {
// //            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername()去验证用户名和密码，
// //            // 如果正确，则存储该用户名密码到security 的 context中
// //            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
// //        } catch (Exception e) {
// //            if (e instanceof BadCredentialsException) {
// //                throw new BaseException("用户名或密码错误", 402);
// //            } else if (e instanceof DisabledException) {
// //                throw new BaseException("账户被禁用", 402);
// //            } else if (e instanceof AccountExpiredException) {
// //                throw new BaseException("账户过期无法验证", 402);
// //            } else {
// //                throw new BaseException("账户被锁定,无法登录", 402);
// //            }
// //        }
// //        //将业务系统的用户与社交用户绑定
// //        socialRedisHelper.doPostSignUp(user.getKey(), userId);
// //        return true;
// //    }
//
// }
