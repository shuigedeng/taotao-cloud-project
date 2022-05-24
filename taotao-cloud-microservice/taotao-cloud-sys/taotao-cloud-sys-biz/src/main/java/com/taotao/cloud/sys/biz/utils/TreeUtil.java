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

import com.taotao.cloud.common.tree.TreeNode;
import com.taotao.cloud.common.utils.common.OrikaUtil;
import com.taotao.cloud.sys.api.bo.menu.MenuBO;
import com.taotao.cloud.sys.api.enums.MenuTypeEnum;
import com.taotao.cloud.sys.api.vo.menu.MenuMetaVO;
import com.taotao.cloud.sys.api.vo.menu.MenuTreeVO;
import com.taotao.cloud.sys.biz.entity.system.Menu;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * TreeUtil
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/10/21 11:20
 */
public class TreeUtil {

	/**
	 * 两层循环实现建树
	 *
	 * @param treeNodes 传入的树节点列表
	 * @param parentId  父节点
	 * @return 树节点数据
	 * @since 2020/10/21 11:21
	 */
	public static <T extends TreeNode> List<T> build(List<T> treeNodes, Long parentId) {
		List<T> trees = new ArrayList<>();
		for (T treeNode : treeNodes) {
			if (parentId.equals(treeNode.getParentId())) {
				trees.add(treeNode);
			}

			for (T it : treeNodes) {
				if (it.getParentId().equals(treeNode.getId())) {
					if (treeNode.getChildren().size() != 0) {
						treeNode.setHasChildren(true);
					}
					trees.add(it);
				}
			}
		}
		return trees;
	}


	/**
	 * 使用递归方法建树
	 *
	 * @param treeNodes 传入的树节点列表
	 * @param parentId  父节点
	 * @return 树节点数据
	 * @since 2020/10/21 11:22
	 */
	public static <T extends TreeNode> List<T> recursiveBuild(List<T> treeNodes, Long parentId) {
		List<T> trees = new ArrayList<T>();
		for (T treeNode : treeNodes) {
			if (parentId.equals(treeNode.getParentId())) {
				trees.add(findChildren(treeNode, treeNodes));
			}
		}
		return trees;
	}

	/**
	 * 递归查找子节点
	 *
	 * @param treeNode  节点
	 * @param treeNodes 子节点列表
	 * @return 树节点数据
	 * @since 2020/10/21 11:23
	 */
	public static <T extends TreeNode> T findChildren(T treeNode, List<T> treeNodes) {
		for (T it : treeNodes) {
			if (treeNode.getId().equals(it.getParentId())) {
				if (treeNode.getChildren().size() != 0) {
					treeNode.setHasChildren(true);
				}
				treeNodes.add(findChildren(it, treeNodes));
			}
		}
		return treeNode;
	}

	/**
	 * 通过SysMenu创建树形节点
	 *
	 * @param parentId 父id
	 * @return 菜单树形节点
	 * @since 2020/10/21 11:23
	 */
	public static List<MenuTreeVO> buildTree(List<MenuBO> menus, Long parentId) {
		List<MenuTreeVO> trees = new ArrayList<>();
		MenuTreeVO node;
		for (MenuBO menu : menus) {
			node = MenuTreeVO.builder()
				.name(menu.name())
				.type(menu.type())
				.keepAlive(menu.keepAlive())
				.path(menu.path())
				.perms(menu.perms())
				.label(menu.name())
				.icon(menu.icon())
				.build();
			node.setId(menu.id());
			node.setParentId(menu.parentId());
			node.setChildren(new ArrayList<>());
			node.setHasChildren(false);
			node.setSort(menu.sortNum());
			trees.add(node);
		}
		return TreeUtil.build(trees, parentId);
	}

	/**
	 * 对象转树节点
	 *
	 * @param menus 系统菜单
	 * @return 菜单树形节点
	 * @since 2020/10/21 11:23
	 */
	public static List<MenuTreeVO> buildTree(List<Menu> menus) {
		return menus.stream()
			.filter(Objects::nonNull)
			.map(sysMenu -> {
				MenuTreeVO tree = OrikaUtil.convert(sysMenu, MenuTreeVO.class);
				tree.setHidden(sysMenu.getHidden());

				MenuMetaVO meta = new MenuMetaVO();
				meta.setIcon(sysMenu.getIcon());
				meta.setTitle(sysMenu.getName());
				tree.setMeta(meta);

				// 只有当菜单类型为目录的时候，如果是顶级，则强制修改为Layout
				if (sysMenu.getParentId() == -1L && MenuTypeEnum.DIR.getCode()
					.equals(sysMenu.getType())) {
					tree.setComponent("Layout");
					tree.setRedirect("noRedirect");
					tree.setAlwaysShow(true);
				}
				tree.setSort(sysMenu.getSortNum());

				if (MenuTypeEnum.DIR.getCode().equals(sysMenu.getType())) {
					tree.setTypeName(MenuTypeEnum.DIR.getMessage());
				} else if (MenuTypeEnum.MENU.getCode().equals(sysMenu.getType())) {
					tree.setTypeName(MenuTypeEnum.MENU.getMessage());
				} else if (MenuTypeEnum.BUTTON.getCode().equals(sysMenu.getType())) {
					tree.setTypeName(MenuTypeEnum.BUTTON.getMessage());
				}
				return tree;
			}).toList();
	}

	/**
	 * list转树形方法
	 *
	 * @param treeList treeList
	 * @param parentId parentId
	 * @return tree
	 * @since 2022-04-17 10:29:02
	 */
	public static <E extends TreeNode> List<E> streamToTree(List<E> treeList, Long parentId) {
		return treeList.stream()
			.filter(Objects::nonNull)
			// 过滤父节点
			.filter(parent -> parent.getParentId().equals(parentId))
			// 把父节点children递归赋值成为子节点
			.peek(child -> {
				List<E> list = streamToTree(treeList, child.getId());
				list.sort(Comparator.comparing(E::getSort));
				child.setChildren(list);
			})
			.collect(Collectors.toList());
	}

	//public static void main(String[] args) {
	//	Connection conn = null;
	//	Statement stmt = null;
	//	try {
	//		//1、注册驱动的第二种方法，告诉java我要连接mysql
	//		Driver driver = new com.mysql.cj.jdbc.Driver();
	//		DriverManager.registerDriver(driver);
	//		//2、获取连接，告诉java我要连接哪台机器的mysql，并写入用户和密码
	//		//127.0.0.1和localhost都是本地ip地址
	//		String url = "jdbc:mysql://192.168.10.200:3306/taotao-cloud-sys?useSSL=false";
	//		String user = "root";
	//		String password = "123456";
	//		conn = DriverManager.getConnection(url, user, password);
	//		System.out.println(conn);
	//
	//		//3、获取数据库操作对象（statement专门执行sql语句）
	//		stmt = conn.createStatement();
	//
	//		//String strings = FileUtil.readString("/Users/shuigdeng/projects/taotao-projects/sensitive-word/src/main/resources/dict.json",
	//		//	StandardCharsets.UTF_8);
	//		//List<String> strings = JsonUtil.readList(str, String.class);
	//
	//		List<String> strings = FileUtil.readLines("/Users/shuigeng/projects/taotao-projects/sensitive-word/src/main/resources/dict.txt",
	//			StandardCharsets.UTF_8);
	//
	//		String sql = "insert into tt_sys_sensitive_words(sensitive_word)values(?)";
	//		PreparedStatement ps = conn.prepareStatement(sql);
	//		final String UTFI_BOM="\uFEFF";
	//		for (String str1 : strings) {
	//			//4、执行sql
	//			//专门执行DML语句（insert、delete、update）
	//			//返回值是“影响数据库中的记录条数”
	//			if (str1.startsWith(UTFI_BOM)) {
	//				str1 = str1.substring(1);
	//			}
	//			ps.setObject(1, str1.replace("\"", "").trim());
	//			ps.execute();
	//		}
	//		//5、处理查询结果集
	//		//插入语句，暂时不需要查询
	//	} catch (SQLException e) {
	//		e.printStackTrace();
	//	} finally {
	//		//6、释放资源
	//		//为了保证资源一定释放，在finally语句块中关闭资源
	//		//分别要遵循从小到大依次关闭
	//		if (stmt != null) {
	//			try {
	//				stmt.close();
	//			} catch (SQLException throwables) {
	//				throwables.printStackTrace();
	//			}
	//		}
	//		if (conn != null) {
	//			try {
	//				conn.close();
	//			} catch (SQLException throwables) {
	//				throwables.printStackTrace();
	//			}
	//		}
	//	}
	//}
}

