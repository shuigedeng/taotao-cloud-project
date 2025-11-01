/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.sys.biz.utils;
//
//
// import com.alibaba.fastjson.JSONArray;
// import com.alibaba.fastjson.JSONObject;
//
// import java.util.ArrayList;
// import java.util.Comparator;
// import java.util.List;
// import lombok.experimental.UtilityClass;
//
// /**
//  * 系统用户工具类
//  *
//  * @author shuigedeng
//  * @since 2020/4/30 11:06
//  */
// @UtilityClass
// public class UcUtil {
//
//     /**
//      * 遍历菜单
//      *
//      * @param menuList
//      * @param menus
//      * @param menuType
//      * @return void
//      * @author shuigedeng
//      * @since 2020/4/30 11:06
//      */
//     public void findChildren(List<SysMenu> menuList, List<SysMenu> menus, int menuType) {
//         for (SysMenu sysMenu : menuList) {
// //            sysMenu.setKey(sysMenu.getMenuId());
//             List<SysMenu> children = new ArrayList<>();
//             for (SysMenu menu : menus) {
//                 if (menuType == 1 && menu.getType() == 2) {
//                     // 如果是获取类型不需要按钮，且菜单类型是按钮的，直接过滤掉
//                     continue;
//                 }
//                 if (sysMenu.getId() != null && sysMenu.getId().equals(menu.getParentId())) {
// //                    menu.setParentName(sysMenu.getName());
// //                    menu.setLevel(sysMenu.getLevel() + 1);
// //                    menu.setKey(menu.getMenuId());
//                     if (exists(children, menu)) {
//                         children.add(menu);
//                     }
//                 }
//             }
// //            sysMenu.setChildren(children);
//             children.sort(Comparator.comparing(SysMenu::getSort));
//             findChildren(children, menus, menuType);
//         }
//     }
//
//     public void findChildren1(List<MenuTreeVo> menuList, List<SysMenu> menus, int menuType) {
//         for (MenuTreeVo sysMenu : menuList) {
//             List<SysMenu> children = new ArrayList<>();
//             List<MenuTreeVo> children1 = new ArrayList<>();
//             for (SysMenu menu : menus) {
//                 MenuTreeVo menuTreeVo = new MenuTreeVo();
//                 BeanUtil.copyProperties(menu, menuTreeVo);
//                 menuTreeVo.setTitle(menu.getName());
//                 menuTreeVo.setKey(menu.getId());
//                 menuTreeVo.setValue(String.valueOf(menu.getId()));
//                 if (menuType == 1 && menu.getType() == 2) {
//                     // 如果是获取类型不需要按钮，且菜单类型是按钮的，直接过滤掉
//                     continue;
//                 }
//                 if (sysMenu.getMenuId() != null &&
// sysMenu.getMenuId().equals(menu.getParentId())) {
//                     menuTreeVo.setParentName(sysMenu.getName());
//                     menuTreeVo.setLevel(sysMenu.getLevel() + 1);
//                     menuTreeVo.setTitle(menu.getName());
//                     menuTreeVo.setKey(menu.getId());
//                     menuTreeVo.setValue(String.valueOf(menu.getId()));
//                     if (exists(children, menu)) {
//                         children1.add(menuTreeVo);
//                     }
//                 }
//             }
//             sysMenu.setChildren(children1);
//             children.sort(Comparator.comparing(SysMenu::getSort));
//             findChildren1(children1, menus, menuType);
//         }
//     }
//
//     /**
//      * 构建部门tree
//      *
//      * @param sysDepts
//      * @param depts
//      * @return void
//      * @author shuigedeng
//      * @since 2020/4/30 11:06
//      */
//     public void findChildren(List<SysDept> sysDepts, List<SysDept> depts) {
//
//         for (SysDept sysDept : sysDepts) {
//             DeptTreeVo deptTreeVo = new DeptTreeVo();
//             deptTreeVo.setKey(sysDept.getId());
//             deptTreeVo.setValue(String.valueOf(sysDept.getId()));
//             deptTreeVo.setTitle(sysDept.getName());
//             List<SysDept> children = new ArrayList<>();
//             List<DeptTreeVo> children1 = new ArrayList<>();
//             for (SysDept dept : depts) {
//                 if (sysDept.getId() != null && sysDept.getId().equals(dept.getParentId())) {
// //                    dept.setParentName(sysDept.getName());
// //                    dept.setLevel(sysDept.getLevel() + 1);
//                     DeptTreeVo deptTreeVo1 = new DeptTreeVo();
//                     deptTreeVo1.setTitle(dept.getName());
//                     deptTreeVo1.setKey(dept.getId());
//                     deptTreeVo1.setValue(String.valueOf(dept.getId()));
//                     children.add(dept);
//                     children1.add(deptTreeVo1);
//                 }
//             }
// //            sysDept.setChildren(children);
//             deptTreeVo.setChildren(children1);
//             findChildren(children, depts);
//         }
//     }
//
//
//     public void findDeptTreeChildren(List<SysDeptTreeVo> sysDepts, List<SysDept> depts) {
//         for (SysDeptTreeVo sysDept : sysDepts) {
//             sysDept.setKey(sysDept.getDeptId());
//             sysDept.setValue(String.valueOf(sysDept.getDeptId()));
//             sysDept.setTitle(sysDept.getName());
//             List<SysDeptTreeVo> children = new ArrayList<>();
//             for (SysDept dept : depts) {
//                 SysDeptTreeVo sysDeptTreeVo = new SysDeptTreeVo();
//                 BeanUtil.copyProperties(dept, sysDeptTreeVo);
//                 if (sysDept.getDeptId() != null &&
// sysDept.getDeptId().equals(dept.getParentId())) {
//                     sysDeptTreeVo.setParentName(sysDept.getName());
//                     sysDeptTreeVo.setLevel(sysDept.getLevel() + 1);
//
//                     DeptTreeVo deptTreeVo1 = new DeptTreeVo();
//                     deptTreeVo1.setTitle(dept.getName());
//                     deptTreeVo1.setKey(dept.getId());
//                     deptTreeVo1.setValue(String.valueOf(dept.getId()));
//                     children.add(sysDeptTreeVo);
//                 }
//             }
//             sysDept.setChildren(children);
//             findDeptTreeChildren(children, depts);
//         }
//     }
//
//
//     /**
//      * 判断菜单是否存在
//      *
//      * @param sysMenus
//      * @param sysMenu
//      * @return boolean
//      * @author shuigedeng
//      * @since 2020/4/30 11:06
//      */
//     public boolean exists(List<SysMenu> sysMenus, SysMenu sysMenu) {
//         boolean exist = false;
//         for (SysMenu menu : sysMenus) {
//             if (menu.getId().equals(sysMenu.getId())) {
//                 exist = true;
//                 break;
//             }
//         }
//         return !exist;
//     }
//
//
//     /**
//      * 获取菜单JSON数组
//      *
//      * @param jsonArray
//      * @param metaList
//      * @param parentJson
//      * @return void
//      * @author shuigedeng
//      * @since 2020/4/30 11:07
//      */
//     public void getPermissionJsonArray(JSONArray jsonArray, List<SysMenu> metaList, JSONObject
// parentJson) {
//         for (SysMenu permission : metaList) {
//             if (permission.getType() == null) {
//                 continue;
//             }
//             Integer tempPid = permission.getParentId();
//             JSONObject json = getPermissionJsonObject(permission);
//             if (json == null) {
//                 continue;
//             }
//             if (parentJson == null && tempPid.equals(0)) {
//                 jsonArray.add(json);
//                 getPermissionJsonArray(jsonArray, metaList, json);
//             } else if (parentJson != null && tempPid != 0 &&
// tempPid.equals(Integer.parseInt(parentJson.getString("id")))) {
//                 // 类型( 0：一级菜单 1：子菜单 2：按钮 )
//                 if (permission.getType().equals(1) || permission.getType().equals(0)) {
//                     if (parentJson.containsKey("children")) {
//                         parentJson.getJSONArray("children").add(json);
//                     } else {
//                         JSONArray children = new JSONArray();
//                         children.add(json);
//                         parentJson.put("children", children);
//                     }
//                     getPermissionJsonArray(jsonArray, metaList, json);
//                 }
//             }
//
//         }
//     }
//
//
//     public JSONObject getPermissionJsonObject(SysMenu permission) {
//         JSONObject json = new JSONObject();
//         // 类型(0：一级菜单 1：子菜单 2：按钮)
//         if (permission.getType().equals(2)) {
//             return null;
//         } else if (permission.getType().equals(0) || permission.getType().equals(1)) {
//             json.put("id", permission.getId());
//             if (isUrl(permission.getPath())) {
//                 json.put("path", permission.getPath());
//             } else {
//                 json.put("path", permission.getPath());
//             }
//
//             // 重要规则：路由name (通过URL生成路由name,路由name供前端开发，页面跳转使用)
//             if (StrUtil.isNotEmpty(permission.getComponent())) {
//                 json.put("name", urlToRouteName(permission.getName()));
//             }
//
//             // 是否隐藏路由，默认都是显示的
//             if (permission.getHidden()) {
//                 json.put("hidden", true);
//             }
//             // 聚合路由
//             if (permission.getAlwaysShow()) {
//                 json.put("alwaysShow", true);
//             }
//
//             if (permission.getParentId().equals(0)) {
//                 //一级目录需要加斜杠，不然访问 会跳转404页面
//                 json.put("component", StrUtil.isEmpty(permission.getComponent()) ? "Layout" :
// permission.getComponent());
//             } else if (!StrUtil.isEmpty(permission.getComponent())) {
//                 json.put("component", permission.getComponent());
//             }
//
//             JSONObject meta = new JSONObject();
//             // 由用户设置是否缓存页面 用布尔值
//             if (permission.getKeepAlive()) {
//                 meta.put("keepAlive", true);
//             } else {
//                 meta.put("keepAlive", false);
//             }
//
//             meta.put("title", permission.getName());
//             if (permission.getParentId() == 0) {
//                 // 一级菜单跳转地址
//                 json.put("redirect", permission.getRedirect());
//                 if (StrUtil.isNotEmpty(permission.getIcon())) {
//                     meta.put("icon", permission.getIcon());
//                 }
//             } else {
//                 if (StrUtil.isNotEmpty(permission.getIcon())) {
//                     meta.put("icon", permission.getIcon());
//                 }
//             }
//             if (isUrl(permission.getPath())) {
//                 meta.put("url", permission.getPath());
//             }
//             json.put("meta", meta);
//         }
//
//         return json;
//     }
//
//     /**
//      * 判断是否外网URL
//      *
//      * @param url
//      * @return boolean
//      * @author shuigedeng
//      * @since 2020/4/30 11:07
//      */
//     private boolean isUrl(String url) {
//         return url != null && (url.startsWith("http://") || url.startsWith("https://") ||
// url.startsWith("{{"));
//     }
//
//     /**
//      * 路由名称
//      *
//      * @param url
//      * @return java.lang.String
//      * @author shuigedeng
//      * @since 2020/4/30 11:07
//      */
//     private String urlToRouteName(String url) {
//         if (StrUtil.isNotEmpty(url)) {
//             int i = url.lastIndexOf("/");
//             return url.substring(i + 1);
//         } else {
//             return null;
//         }
//     }
// }
