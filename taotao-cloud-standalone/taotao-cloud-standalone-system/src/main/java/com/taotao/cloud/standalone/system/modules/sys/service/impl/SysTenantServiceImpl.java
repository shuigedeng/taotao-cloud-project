package com.taotao.cloud.standalone.system.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.standalone.system.modules.data.strategy.DataScopeContext;
import com.taotao.cloud.standalone.system.modules.data.tenant.PreTenantContextHolder;
import com.taotao.cloud.standalone.system.modules.sys.domain.SysDept;
import com.taotao.cloud.standalone.system.modules.sys.domain.SysMenu;
import com.taotao.cloud.standalone.system.modules.sys.domain.SysRole;
import com.taotao.cloud.standalone.system.modules.sys.domain.SysRoleMenu;
import com.taotao.cloud.standalone.system.modules.sys.domain.SysTenant;
import com.taotao.cloud.standalone.system.modules.sys.domain.SysUser;
import com.taotao.cloud.standalone.system.modules.sys.domain.SysUserRole;
import com.taotao.cloud.standalone.system.modules.sys.mapper.SysTenantMapper;
import com.taotao.cloud.standalone.system.modules.sys.service.ISysDeptService;
import com.taotao.cloud.standalone.system.modules.sys.service.ISysMenuService;
import com.taotao.cloud.standalone.system.modules.sys.service.ISysRoleMenuService;
import com.taotao.cloud.standalone.system.modules.sys.service.ISysRoleService;
import com.taotao.cloud.standalone.system.modules.sys.service.ISysTenantService;
import com.taotao.cloud.standalone.system.modules.sys.service.ISysUserRoleService;
import com.taotao.cloud.standalone.system.modules.sys.service.ISysUserService;
import com.taotao.cloud.standalone.system.modules.sys.util.PreUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * <p>
 * 租户表 服务实现类
 * </p>
 *
 * @author lihaodong
 * @since 2019-08-10
 */
@Service
public class SysTenantServiceImpl extends ServiceImpl<SysTenantMapper, SysTenant> implements ISysTenantService {

    @Autowired
    private ISysUserRoleService userRoleService;
    @Autowired
    private ISysDeptService deptService;
    @Autowired
    private ISysMenuService menuService;
    @Autowired
    private ISysUserService userService;
    @Autowired
    private ISysRoleService roleService;
    @Autowired
    private ISysRoleMenuService roleMenuService;
    @Autowired
    private DataScopeContext dataScopeContext;


    /**
     * 一般租户授权时
     * 1.保存租户
     * 2.初始化权限相关表
     *
     * @param sysTenant
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveTenant(SysTenant sysTenant) {

        this.save(sysTenant);
        // 修改租户Id 每次插入都是新的
        PreTenantContextHolder.setCurrentTenantId(Long.valueOf(sysTenant.getId()));
        // 插入部门
        SysDept dept = new SysDept();
        dept.setName("默认部门");
        dept.setParentId(0);
        dept.setSort(0);
        deptService.save(dept);
        // 构造初始化用户
        SysUser user = new SysUser();
        user.setUsername("root");
        // 默认密码
        user.setPassword(PreUtil.encode("123456"));
        user.setDeptId(dept.getDeptId());
        userService.save(user);
        // 构造新角色
        SysRole role = new SysRole();
        role.setRoleCode("ROLE_ADMIN");
        role.setRoleName("默认角色");
        role.setDsType(1);
        // 默认全部权限
        List<Integer> ids = dataScopeContext.getDeptIdsForDataScope(null, 1);
        StringJoiner dsScope = new StringJoiner(",");
        ids.forEach(integer -> {
            dsScope.add(Integer.toString(integer));
        });
        role.setDsScope(dsScope.toString());
        roleService.save(role);
        // 用户角色关系
        SysUserRole userRole = new SysUserRole();
        userRole.setUserId(user.getUserId());
        userRole.setRoleId(role.getRoleId());
        userRoleService.save(userRole);

        List<SysMenu> list = menuService.list();
        List<SysMenu> sysMenuList = new ArrayList<>();
        list.forEach(sysMenu -> {
            if (sysMenu.getType() == 1) {
                if ("代码生成".equals(sysMenu.getName())) {
                    List<SysMenu> sysMenus = treeMenuList(list, sysMenu.getMenuId());
                    sysMenus.add(sysMenu);
                    sysMenuList.addAll(sysMenus);
                }
                if ("租户管理".equals(sysMenu.getName())) {
                    List<SysMenu> sysMenus = treeMenuList(list, sysMenu.getMenuId());
                    sysMenus.add(sysMenu);
                    sysMenuList.addAll(sysMenus);
                }

            }
        });
        sysMenuList.forEach(sysMenu -> {
            list.removeIf(next -> next.getMenuId().equals(sysMenu.getMenuId()));
        });
        // 查询全部菜单,构造角色菜单关系
        List<SysRoleMenu> collect = list
                .stream().map(menu -> {
                    SysRoleMenu roleMenu = new SysRoleMenu();
                    roleMenu.setRoleId(role.getRoleId());
                    roleMenu.setMenuId(menu.getMenuId());
                    return roleMenu;
                }).collect(Collectors.toList());
        return roleMenuService.saveBatch(collect);
    }

    @Override
    public List<SysTenant> getNormalTenant() {
        return list(Wrappers.<SysTenant>lambdaQuery()
                // 状态为0的
                .eq(SysTenant::getStatus, 0)
                // 开始时间小于等于现在的时间
                .le(SysTenant::getStartTime, LocalDateTime.now())
                // 结束时间大于等于现在的时间
                .ge(SysTenant::getEndTime, LocalDateTime.now()));
    }


    /**
     * @param str
     * @return
     * @Title
     * @Description 将带有纳秒的时间字符串转换成LocalDateTime
     */
    public static LocalDateTime strToLocalDateTime(String str) {
        return LocalDateTime.parse(str, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }


    private List<SysMenu> treeMenuList(List<SysMenu> menuList, int pid) {
        List<SysMenu> childMenu = new ArrayList<>();
        for (SysMenu mu : menuList) {
            //遍历出父id等于参数的id，add进子节点集合
            if (mu.getParentId() == pid) {
                //递归遍历下一级
                treeMenuList(menuList, mu.getMenuId());
                childMenu.add(mu);
            }
        }
        return childMenu;
    }


}
