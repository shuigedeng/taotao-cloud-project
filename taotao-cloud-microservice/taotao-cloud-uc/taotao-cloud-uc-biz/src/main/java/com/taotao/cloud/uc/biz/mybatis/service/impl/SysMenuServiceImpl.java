// package com.taotao.cloud.uc.biz.service.impl;
//
// import cn.hutool.core.bean.BeanUtil;
// import cn.hutool.core.collection.CollUtil;
// import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
// import com.baomidou.mybatisplus.core.toolkit.Wrappers;
// import com.taotao.cloud.common.enums.MenuTypeEnum;
// import com.taotao.cloud.common.exception.BaseException;
// import com.taotao.cloud.data.mybatis.plus.service.impl.SuperServiceImpl;
// import com.taotao.cloud.uc.api.dto.MenuDTO;
// import com.taotao.cloud.uc.api.vo.MenuTreeVo;
// import com.taotao.cloud.uc.biz.entity.SysMenu;
// import com.taotao.cloud.uc.biz.mapper.SysMenuMapper;
// import com.taotao.cloud.uc.biz.service.ISysMenuService;
// import com.taotao.cloud.uc.biz.service.ISysRoleMenuService;
// import com.taotao.cloud.uc.biz.utils.UcUtil;
// import org.springframework.beans.BeanUtils;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;
//
// import java.io.Serializable;
// import java.util.ArrayList;
// import java.util.Comparator;
// import java.util.List;
//
// /**
//  * 菜单权限表 服务实现类
//  *
//  * @author dengtao
//  * @date 2020/4/30 11:42
//  */
// @Service
// public class SysMenuServiceImpl extends SuperServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {
//
//     @Autowired
//     private ISysRoleMenuService roleMenuService;
//
//     @Override
//     public boolean save(SysMenu sysMenu) {
//         // 菜单校验
//         verifyForm(sysMenu);
//         return super.save(sysMenu);
//     }
//
//     @Override
//     public boolean updateMenuById(MenuDTO entity) {
//         SysMenu sysMenu = new SysMenu();
//         BeanUtils.copyProperties(entity, sysMenu);
//         // 菜单校验
//         verifyForm(sysMenu);
//         return this.updateById(sysMenu);
//     }
//
//     @Override
//     public List<SysMenu> selectMenuTree(Integer uid) {
//         LambdaQueryWrapper<SysMenu> sysMenuLambdaQueryWrapper = Wrappers.<SysMenu>query().lambda();
//         sysMenuLambdaQueryWrapper.
//                 select(SysMenu::getId, SysMenu::getName, SysMenu::getPerms, SysMenu::getPath, SysMenu::getParentId, SysMenu::getComponent, SysMenu::getIsFrame, SysMenu::getIcon, SysMenu::getSort, SysMenu::getType, SysMenu::getDelFlag, SysMenu::getAlwaysShow, SysMenu::getHidden, SysMenu::getRedirect, SysMenu::getKeepAlive, SysMenu::getRedirect);
//         // 所有人有权限看到 只是没有权限操作而已 暂定这样
//         if (uid != 0) {
//             List<Integer> menuIdList = roleMenuService.getMenuIdByUserId(uid);
//             sysMenuLambdaQueryWrapper.in(SysMenu::getId, menuIdList);
//         }
//
//         List<SysMenu> sysMenus = new ArrayList<>();
//         List<SysMenu> menus = baseMapper.selectList(sysMenuLambdaQueryWrapper);
//
//         menus.forEach(menu -> {
//             if (menu.getParentId() == null || menu.getParentId() == 0) {
// //                menu.setLevel(0);
// //                menu.setKey(menu.getMenuId());
//                 if (UcUtil.exists(sysMenus, menu)) {
//                     sysMenus.add(menu);
//                 }
//             }
//         });
//         sysMenus.sort(Comparator.comparing(SysMenu::getSort));
//         UcUtil.findChildren(sysMenus, menus, 0);
//         return sysMenus;
//     }
//
//     @Override
//     public SysMenu getMenuById(Integer parentId) {
//         return baseMapper.selectOne(Wrappers.<SysMenu>lambdaQuery().select(SysMenu::getType).eq(SysMenu::getId, parentId));
//     }
//
//     @Override
//     public List<String> findPermsByUserId(Long userId) {
//         return baseMapper.findPermsByUserId(userId);
//     }
//
//     @Override
//     public Boolean removeMenuById(Serializable id) {
// //        List<Integer> idList = this.list(Wrappers.<SysMenu>query().lambda().eq(SysMenu::getParentId, id))
// //                .stream().map(SysMenu::getId).collect(Collectors.toList());
//         List<Integer> idList = new ArrayList<>();
//         if (CollUtil.isNotEmpty(idList)) {
//             return false;
//         }
//         SysMenu sysMenu = (SysMenu) new SysMenu().setId((Long) id).setDelFlag(1);
//
//         return this.updateById(sysMenu);
//     }
//
//     /**
//      * 验证菜单参数是否正确
//      *
//      * @param menu
//      * @return void
//      * @author dengtao
//      * @date 2020/4/30 11:43
//      */
//     private void verifyForm(SysMenu menu) {
//         //上级菜单类型
//         int parentType = MenuTypeEnum.CATALOG.getValue();
//         if (menu.getParentId() != 0) {
//             SysMenu parentMenu = getMenuById(menu.getParentId());
//             parentType = parentMenu.getType();
//         }
//         //目录、菜单
//         if (menu.getType() == MenuTypeEnum.CATALOG.getValue() ||
//                 menu.getType() == MenuTypeEnum.MENU.getValue()) {
//             if (parentType != MenuTypeEnum.CATALOG.getValue()) {
//                 throw new BaseException("上级菜单只能为目录类型");
//             }
//             return;
//         }
//         //按钮
//         if (menu.getType() == MenuTypeEnum.BUTTON.getValue()) {
//             if (parentType != MenuTypeEnum.MENU.getValue()) {
//                 throw new BaseException("上级菜单只能为菜单类型");
//             }
//         }
//     }
//
//     // TODO 优化
//     @Override
//     public List<MenuTreeVo> menuTree() {
//         LambdaQueryWrapper<SysMenu> queryWrapper = Wrappers.<SysMenu>query().lambda();
// //        queryWrapper.select(SysMenu::getMenuId, SysMenu::getName, SysMenu::getParentId, SysMenu::getType, SysMenu::getSort);
//         List<SysMenu> sysMenus = new ArrayList<>();
//         List<MenuTreeVo> menuTreeVoList = new ArrayList<>();
//         List<SysMenu> menus = baseMapper.selectList(queryWrapper);
//
//         menus.forEach(menu -> {
//             MenuTreeVo menuTreeVo = new MenuTreeVo();
//             BeanUtil.copyProperties(menu, menuTreeVo);
//
//             if (menu.getParentId() == null || menu.getParentId() == 0) {
//                 menuTreeVo.setLevel(0);
//                 menuTreeVo.setTitle(menu.getName());
// //                menuTreeVo.setKey(menu.getMenuId());
// //                menuTreeVo.setValue(String.valueOf(menu.getMenuId()));
//                 if (UcUtil.exists(sysMenus, menu)) {
//                     sysMenus.add(menu);
//                     menuTreeVoList.add(menuTreeVo);
//                 }
//             }
//         });
//         menuTreeVoList.sort(Comparator.comparing(MenuTreeVo::getSort));
//         UcUtil.findChildren1(menuTreeVoList, menus, 0);
//         return menuTreeVoList;
//     }
//
// }
