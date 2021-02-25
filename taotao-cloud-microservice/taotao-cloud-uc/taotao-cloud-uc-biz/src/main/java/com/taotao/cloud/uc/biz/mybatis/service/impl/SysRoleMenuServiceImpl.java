// package com.taotao.cloud.uc.biz.service.impl;
//
// import com.baomidou.mybatisplus.core.toolkit.Wrappers;
// import com.taotao.cloud.data.mybatis.plus.service.impl.SuperServiceImpl;
// import com.taotao.cloud.uc.biz.entity.SysRoleMenu;
// import com.taotao.cloud.uc.biz.mapper.SysRoleMenuMapper;
// import com.taotao.cloud.uc.biz.service.ISysRoleMenuService;
// import org.springframework.stereotype.Service;
//
// import java.util.List;
//
//
// /**
//  * 角色菜单表 服务实现类
//  *
//  * @author dengtao
//  * @date 2020/4/30 11:42
//  */
// @Service
// public class SysRoleMenuServiceImpl extends SuperServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements ISysRoleMenuService {
//
//     @Override
//     public List<Integer> getMenuIdByUserId(Integer userId) {
//         return baseMapper.getMenuIdByUserId(userId);
//     }
//
//     @Override
//     public List<Integer> getMenuIdByRoleId(Integer roleId) {
//         List<SysRoleMenu> sysRoleMenus = baseMapper.selectList(Wrappers.<SysRoleMenu>lambdaQuery().select(SysRoleMenu::getMenuId).eq(SysRoleMenu::getRoleId, roleId));
// //        return sysRoleMenus.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList());
//         return null;
//     }
// }
