package com.taotao.cloud.standalone.system.modules.sys.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.standalone.common.constant.MenuConstant;
import com.taotao.cloud.standalone.common.exception.PreBaseException;
import com.taotao.cloud.standalone.common.utils.R;
import com.taotao.cloud.standalone.system.modules.sys.domain.SysMenu;
import com.taotao.cloud.standalone.system.modules.sys.dto.MenuDTO;
import com.taotao.cloud.standalone.system.modules.sys.mapper.SysMenuMapper;
import com.taotao.cloud.standalone.system.modules.sys.service.ISysMenuService;
import com.taotao.cloud.standalone.system.modules.sys.service.ISysRoleMenuService;
import com.taotao.cloud.standalone.system.modules.sys.util.PreUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单权限表 服务实现类
 * </p>
 *
 * @author lihaodong
 * @since 2019-04-21
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    @Autowired
    private ISysRoleMenuService roleMenuService;

    @Override
    public boolean save(SysMenu sysMenu) {
        // 菜单校验
        verifyForm(sysMenu);
        return super.save(sysMenu);
    }

    @Override
    public boolean updateMenuById(MenuDTO entity) {
        SysMenu sysMenu = new SysMenu();
        BeanUtils.copyProperties(entity, sysMenu);
        // 菜单校验
        verifyForm(sysMenu);
        return this.updateById(sysMenu);
    }

    @Override
    public List<SysMenu> selectMenuTree(Integer uid) {

        LambdaQueryWrapper<SysMenu> sysMenuLambdaQueryWrapper = Wrappers.<SysMenu>query().lambda();
        sysMenuLambdaQueryWrapper.select(SysMenu::getMenuId, SysMenu::getName, SysMenu::getPerms, SysMenu::getPath, SysMenu::getParentId, SysMenu::getComponent, SysMenu::getIsFrame, SysMenu::getIcon, SysMenu::getSort, SysMenu::getType, SysMenu::getDelFlag);
        // 所有人有权限看到 只是没有权限操作而已 暂定这样
        if (uid != 0) {
            List<Integer> menuIdList = roleMenuService.getMenuIdByUserId(uid);
            sysMenuLambdaQueryWrapper.in(SysMenu::getMenuId, menuIdList);
        }
        List<SysMenu> sysMenus = new ArrayList<>();
        List<SysMenu> menus = baseMapper.selectList(sysMenuLambdaQueryWrapper);
        menus.forEach(menu -> {
            if (menu.getParentId() == null || menu.getParentId() == 0) {
                menu.setLevel(0);
                if (PreUtil.exists(sysMenus, menu)) {
                    sysMenus.add(menu);
                }
            }
        });
        sysMenus.sort((o1, o2) -> o1.getSort().compareTo(o2.getSort()));
        PreUtil.findChildren(sysMenus, menus, 0);
        return sysMenus;
    }

    @Override
    public SysMenu getMenuById(Integer parentId) {
        return baseMapper.selectOne(Wrappers.<SysMenu>lambdaQuery().select(SysMenu::getType).eq(SysMenu::getMenuId, parentId));
    }

    @Override
    public List<String> findPermsByUserId(Integer userId) {
        return baseMapper.findPermsByUserId(userId);
    }

    @Override
    public R removeMenuById(Serializable id) {
        List<Integer> idList =
                this.list(Wrappers.<SysMenu>query().lambda().eq(SysMenu::getParentId, id)).stream().map(SysMenu::getMenuId).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(idList)) {
            return R.error("菜单含有下级不能删除");
        }
        return R.ok(this.removeById(id));
    }

    /**
     * 验证菜单参数是否正确
     */
    private void verifyForm(SysMenu menu) {
        //上级菜单类型
        int parentType = MenuConstant.MenuType.CATALOG.getValue();
        if (menu.getParentId() != 0) {
            SysMenu parentMenu = getMenuById(menu.getParentId());
            parentType = parentMenu.getType();
        }
        //目录、菜单
        if (menu.getType() == MenuConstant.MenuType.CATALOG.getValue() ||
                menu.getType() == MenuConstant.MenuType.MENU.getValue()) {
            if (parentType != MenuConstant.MenuType.CATALOG.getValue()) {
                throw new PreBaseException("上级菜单只能为目录类型");
            }
            return;
        }
        //按钮
        if (menu.getType() == MenuConstant.MenuType.BUTTON.getValue()) {
            if (parentType != MenuConstant.MenuType.MENU.getValue()) {
                throw new PreBaseException("上级菜单只能为菜单类型");
            }
        }
    }
}
