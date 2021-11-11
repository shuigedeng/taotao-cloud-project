package com.taotao.cloud.standalone.system.modules.sys.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.standalone.common.exception.PreBaseException;
import com.taotao.cloud.standalone.security.PreSecurityUser;
import com.taotao.cloud.standalone.system.modules.data.datascope.DataScope;
import com.taotao.cloud.standalone.system.modules.security.social.SocialRedisHelper;
import com.taotao.cloud.standalone.system.modules.security.util.JwtUtil;
import com.taotao.cloud.standalone.system.modules.sys.domain.SysUser;
import com.taotao.cloud.standalone.system.modules.sys.domain.SysUserRole;
import com.taotao.cloud.standalone.system.modules.sys.dto.UserDTO;
import com.taotao.cloud.standalone.system.modules.sys.mapper.SysUserMapper;
import com.taotao.cloud.standalone.system.modules.sys.service.ISysDeptService;
import com.taotao.cloud.standalone.system.modules.sys.service.ISysMenuService;
import com.taotao.cloud.standalone.system.modules.sys.service.ISysUserRoleService;
import com.taotao.cloud.standalone.system.modules.sys.service.ISysUserService;
import com.taotao.cloud.standalone.system.modules.sys.util.PreUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *

 * @since 2019-04-21
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Autowired
    private ISysUserRoleService userRoleService;
    @Autowired
    private ISysDeptService deptService;
    @Autowired
    private ISysMenuService menuService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    private SocialRedisHelper socialRedisHelper;

    @Override
    public IPage<SysUser> getUsersWithRolePage(Page page, UserDTO userDTO) {

        if ( ObjectUtils.anyNotNull(userDTO) && userDTO.getDeptId() != null) {
            userDTO.setDeptList(deptService.selectDeptIds(userDTO.getDeptId()));
        }
        return baseMapper.getUserVosPage(page, userDTO, new DataScope());
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean insertUser(UserDTO userDto) {
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(userDto, sysUser);
        // 默认密码 123456
        sysUser.setPassword(PreUtil.encode("123456"));
        baseMapper.insertUser(sysUser);
        List<SysUserRole> userRoles = userDto.getRoleList().stream().map(item -> {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setRoleId(item);
            sysUserRole.setUserId(sysUser.getUserId());
            return sysUserRole;
        }).collect(Collectors.toList());

        return userRoleService.saveBatch(userRoles);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateUser(UserDTO userDto) {
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(userDto, sysUser);
        baseMapper.updateById(sysUser);
        userRoleService.remove(Wrappers.<SysUserRole>lambdaQuery().eq(SysUserRole::getUserId, sysUser.getUserId()));
        List<SysUserRole> userRoles = userDto.getRoleList().stream().map(item -> {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setRoleId(item);
            sysUserRole.setUserId(sysUser.getUserId());
            return sysUserRole;
        }).collect(Collectors.toList());

        return userRoleService.saveBatch(userRoles);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeUser(Integer userId) {
        baseMapper.deleteById(userId);
        return userRoleService.remove(Wrappers.<SysUserRole>lambdaQuery().eq(SysUserRole::getUserId, userId));
    }

    @Override
    public boolean restPass(Integer userId) {
        return baseMapper.updateById(new SysUser().setPassword("123456").setUserId(userId)) > 0;
    }

    @Override
    public SysUser findByUserInfoName(String username) {
        SysUser sysUser = baseMapper.selectOne(Wrappers.<SysUser>lambdaQuery()
                .select(SysUser::getUserId, SysUser::getUsername, SysUser::getPhone, SysUser::getEmail, SysUser::getPassword, SysUser::getDeptId, SysUser::getJobId, SysUser::getAvatar)
                .eq(SysUser::getUsername, username));
        // 获取部门
        sysUser.setDeptName(deptService.selectDeptNameByDeptId(sysUser.getDeptId()));
        // 获取岗位
//        sysUser.setJobName(jobService.selectJobNameByJobId(sysUser.getJobId()));
        return sysUser;
    }

    @Override
    public Set<String> findPermsByUserId(Integer userId) {
        return menuService.findPermsByUserId(userId).stream().filter(StringUtils::isNotEmpty).collect(Collectors.toSet());
    }

    @Override
    public Set<String> findRoleIdByUserId(Integer userId) {
        return userRoleService
                .selectUserRoleListByUserId(userId)
                .stream()
                .map(sysUserRole -> "ROLE_" + sysUserRole.getRoleId())
                .collect(Collectors.toSet());
    }


    @Override
    public String login(String username, String password) {
        //用户验证
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        //存储认证信息
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //生成token
        PreSecurityUser userDetail = (PreSecurityUser) authentication.getPrincipal();
        return JwtUtil.generateToken(userDetail);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean register(UserDTO userDTO) {
        // 查询用户名是否存在
        SysUser byUserInfoName = findSecurityUser(userDTO.getUsername());
        if (ObjectUtil.isNotNull(byUserInfoName)) {
            throw new PreBaseException("账户名已被注册");
        }
        SysUser securityUser = findSecurityUser(userDTO.getPhone());
        if (ObjectUtil.isNotNull(securityUser)) {
            throw new PreBaseException("手机号已被注册");
        }
        userDTO.setDeptId(6);
        userDTO.setLockFlag("0");
        SysUser sysUser = new SysUser();
        // 对象拷贝
        BeanUtil.copyProperties(userDTO, sysUser);
        // 加密后的密码
        sysUser.setPassword(PreUtil.encode(userDTO.getPassword()));
        baseMapper.insertUser(sysUser);
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setRoleId(14);
        sysUserRole.setUserId(sysUser.getUserId());
        return userRoleService.save(sysUserRole);
    }

    @Override
    public boolean updateUserInfo(SysUser sysUser) {
        return baseMapper.updateById(sysUser) > 0;
    }

    @Override
    public SysUser findSecurityUserByUser(SysUser sysUser) {
        LambdaQueryWrapper<SysUser> select = Wrappers.<SysUser>lambdaQuery()
                .select(SysUser::getUserId, SysUser::getUsername, SysUser::getPassword);
        if (StrUtil.isNotEmpty(sysUser.getUsername())) {
            select.eq(SysUser::getUsername, sysUser.getUsername());
        } else if (StrUtil.isNotEmpty(sysUser.getPhone())) {
            select.eq(SysUser::getPhone, sysUser.getPhone());
        } else if (ObjectUtil.isNotNull(sysUser.getUserId()) && sysUser.getUserId() != 0) {
            select.eq(SysUser::getUserId, sysUser.getUserId());
        }
        return baseMapper.selectOne(select);
    }

    @Override
    public boolean doPostSignUp(SysUser user) {
        // 进行账号校验
        SysUser sysUser = findSecurityUserByUser(new SysUser().setUsername(user.getUsername()));
        if (ObjectUtil.isNull(sysUser)) {
            throw new PreBaseException("账号不存在");
        }
        Integer userId = sysUser.getUserId();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        //将业务系统的用户与社交用户绑定
        socialRedisHelper.doPostSignUp(user.getKey(), userId);
        return true;
    }

    private SysUser findSecurityUser(String userIdOrUserNameOrPhone) {
        LambdaQueryWrapper<SysUser> select = Wrappers.<SysUser>lambdaQuery()
                .select(SysUser::getUserId, SysUser::getUsername, SysUser::getPassword)
                .eq(SysUser::getUsername, userIdOrUserNameOrPhone)
                .or()
                .eq(SysUser::getPhone, userIdOrUserNameOrPhone)
                .or()
                .eq(SysUser::getUserId, userIdOrUserNameOrPhone);
        return baseMapper.selectOne(select);
    }
}
