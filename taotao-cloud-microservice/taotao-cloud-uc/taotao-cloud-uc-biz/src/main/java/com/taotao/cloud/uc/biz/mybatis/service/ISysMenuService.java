// package com.taotao.cloud.uc.biz.service;
//
// import com.taotao.cloud.uc.api.dto.MenuDTO;
// import com.taotao.cloud.uc.api.vo.MenuTreeVo;
// import com.taotao.cloud.uc.biz.entity.SysMenu;
//
// import java.io.Serializable;
// import java.util.List;
//
// /**
//  * 菜单权限表 服务类
//  *
//  * @author dengtao
//  * @date 2020/4/30 11:41
//  */
// public interface ISysMenuService {
//
//     /**
//      * 更新菜单信息
//      *
//      * @param entity
//      * @return boolean
//      * @author dengtao
//      * @date 2020/4/30 11:41
//      */
//     boolean updateMenuById(MenuDTO entity);
//
//     /**
//      * 删除菜单信息
//      *
//      * @param id
//      * @return java.lang.Boolean
//      * @author dengtao
//      * @date 2020/4/30 11:41
//      */
//     Boolean removeMenuById(Serializable id);
//
//     /**
//      * 根据用户id查找菜单树
//      *
//      * @param uid
//      * @return java.util.List<com.taotao.cloud.uc.api.entity.SysMenu>
//      * @author dengtao
//      * @date 2020/4/30 11:41
//      */
//     List<SysMenu> selectMenuTree(Integer uid);
//
//     /**
//      * 根据父id查询菜单
//      *
//      * @param parentId
//      * @return com.taotao.cloud.uc.api.entity.SysMenu
//      * @author dengtao
//      * @date 2020/4/30 11:41
//      */
//     SysMenu getMenuById(Integer parentId);
//
//     /**
//      * 根据用户id查询权限
//      *
//      * @param userId
//      * @return java.util.List<java.lang.String>
//      * @author dengtao
//      * @date 2020/4/30 11:42
//      */
//     List<String> findPermsByUserId(Long userId);
//
//     /**
//      * 获取菜单树
//      *
//      * @param
//      * @return java.util.List<com.taotao.cloud.uc.api.vo.MenuTreeVo>
//      * @author dengtao
//      * @date 2020/4/30 11:42
//      */
//     List<MenuTreeVo> menuTree();
// }
