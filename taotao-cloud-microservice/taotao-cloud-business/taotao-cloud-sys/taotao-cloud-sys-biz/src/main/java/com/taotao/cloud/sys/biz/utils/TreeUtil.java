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
import com.taotao.cloud.common.utils.bean.BeanUtils;
import com.taotao.cloud.common.utils.common.OrikaUtils;
import com.taotao.cloud.sys.api.enums.ResourceTypeEnum;
import com.taotao.cloud.sys.biz.model.vo.menu.MenuMetaVO;
import com.taotao.cloud.sys.biz.model.vo.menu.MenuTreeVO;
import com.taotao.cloud.sys.biz.model.bo.MenuBO;
import com.taotao.cloud.sys.biz.model.entity.region.Region;
import com.taotao.cloud.sys.biz.model.entity.system.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
     * @param parentId 父节点
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
     * @param parentId 父节点
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
     * @param treeNode 节点
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
     * @param resources 系统菜单
     * @return 菜单树形节点
     * @since 2020/10/21 11:23
     */
    public static List<MenuTreeVO> buildTree(List<Resource> resources) {
        return resources
			.stream()
                .filter(Objects::nonNull)
                .map(sysMenu -> {
                    MenuTreeVO tree = OrikaUtils.convert(sysMenu, MenuTreeVO.class);
                    tree.setHidden(sysMenu.getHidden());

                    MenuMetaVO meta = new MenuMetaVO();
                    meta.setIcon(sysMenu.getIcon());
                    meta.setTitle(sysMenu.getName());
                    tree.setMeta(meta);

                    // 只有当菜单类型为目录的时候，如果是顶级，则强制修改为Layout
                    if (sysMenu.getParentId() == -1L
                            && ResourceTypeEnum.DIR.getCode().equals(sysMenu.getType())) {
                        tree.setComponent("Layout");
                        tree.setRedirect("noRedirect");
                        tree.setAlwaysShow(true);
                    }
                    tree.setSort(sysMenu.getSortNum());

                    if (ResourceTypeEnum.DIR.getCode().equals(sysMenu.getType())) {
                        tree.setTypeName(ResourceTypeEnum.DIR.getMessage());
                    } else if (ResourceTypeEnum.MENU.getCode().equals(sysMenu.getType())) {
                        tree.setTypeName(ResourceTypeEnum.MENU.getMessage());
                    } else if (ResourceTypeEnum.RESOURCE.getCode().equals(sysMenu.getType())) {
                        tree.setTypeName(ResourceTypeEnum.RESOURCE.getMessage());
                    }
                    return tree;
                })
                .toList();
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
        return treeList
			.stream()
                .filter(Objects::nonNull)
                // 过滤父节点
                .filter(parent -> parent.getParentId().equals(parentId))
                // 把父节点children递归赋值成为子节点
                .peek(child -> {
                    List<E> list = streamToTree(treeList, child.getId());
                    list.sort(Comparator.comparing(E::getSort));
                    child.setChildren(list);
                })
                .toList();
    }

    // public static void main(String[] args) {
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
    //		LogUtils.info(conn);
    //
    //		//3、获取数据库操作对象（statement专门执行sql语句）
    //		stmt = conn.createStatement();
    //
    //		//String strings =
    // FileUtil.readString("/Users/shuigdeng/projects/taotao-projects/sensitive-word/src/main/resources/dict.json",
    //		//	StandardCharsets.UTF_8);
    //		//List<String> strings = JsonUtil.readList(str, String.class);
    //
    //		List<String> strings =
    // FileUtil.readLines("/Users/shuigeng/projects/taotao-projects/sensitive-word/src/main/resources/dict.txt",
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
    //		LogUtils.error(e);
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
    // }

    /**
     * java8使用stream流将数据处理成树状结构（非递归）
     *
     * <pre class="code">
     * "tree": [
     *        {
     * 	    "id": 1,
     * 	    "code": "110000000000",
     * 	    "name": "北京市",
     * 	    "pcode": "0",
     * 	    "pname": "中国",
     * 	    "abbreviate": "北京",
     * 	    "type": 1,
     * 	    "state": true,
     * 	    "sort": 0,
     * 	    "children": [
     *            {
     * 	            "id": 2,
     * 	            "code": "110100000000",
     * 	            "name": "市辖区",
     * 	            "pcode": "110000000000",
     * 	            "pname": "北京市",
     * 	            "abbreviate": "北京",
     * 	            "type": 2,
     * 	            "state": true,
     * 	            "sort": 0,
     * 	            "children": [
     *                    {
     * 	                    "id": 3,
     * 	                    "code": "110102000000",
     * 	                    "name": "东城区",
     * 	                    "pcode": "110100000000",
     * 	                    "pname": "市辖区",
     * 	                    "abbreviate": "东城",
     * 	                    "type": 3,
     * 	                    "state": true,
     * 	                    "sort": 0,
     * 	                    "children": null
     *                    },
     *                    {
     * 	                    "id": 4,
     * 	                    "code": "110101000000",
     * 	                    "name": "西城区",
     * 	                    "pcode": "110100000000",
     * 	                    "pname": "市辖区",
     * 	                    "abbreviate": "西城",
     * 	                    "type": 3,
     * 	                    "state": true,
     * 	                    "sort": 0,
     * 	                    "children": null
     *                    },
     *                    {
     * 	                    "id": 5,
     * 	                    "code": "110105000000",
     * 	                    "name": "朝阳区",
     * 	                    "pcode": "110100000000",
     * 	                    "pname": "市辖区",
     * 	                    "abbreviate": "朝阳",
     * 	                    "type": 3,
     * 	                    "state": true,
     * 	                    "sort": 0,
     * 	                    "children": null
     *                    },
     * 	            ]
     *            }
     * 	    ]
     *    },
     *    {
     * 	    "id": 19,
     * 	    "code": "120000000000",
     * 	    "name": "天津市",
     * 	    "pcode": "0",
     * 	    "pname": "中国",
     * 	    "abbreviate": "天津",
     * 	    "type": 1,
     * 	    "state": true,
     * 	    "sort": 0,
     * 	    "children": [
     *            {
     * 	            "id": 20,
     * 	            "code": "120100000000",
     * 	            "name": "市辖区",
     * 	            "pcode": "120000000000",
     * 	            "pname": "天津市",
     * 	            "abbreviate": "天津",
     * 	            "type": 2,
     * 	            "state": true,
     * 	            "sort": 0,
     * 	            "children": [
     *                    {
     * 	                    "id": 21,
     * 	                    "code": "120101000000",
     * 	                    "name": "和平区",
     * 	                    "pcode": "120100000000",
     * 	                    "pname": "市辖区",
     * 	                    "abbreviate": "和平",
     * 	                    "type": 3,
     * 	                    "state": true,
     * 	                    "sort": 0,
     * 	                    "children": null
     *                    },
     *                    {
     * 	                    "id": 22,
     * 	                    "code": "120102000000",
     * 	                    "name": "河东区",
     * 	                    "pcode": "120100000000",
     * 	                    "pname": "市辖区",
     * 	                    "abbreviate": "河东",
     * 	                    "type": 3,
     * 	                    "state": true,
     * 	                    "sort": 0,
     * 	                    "children": null
     *                    }
     * 	            ]
     *            }
     * 	    ]
     *    },
     * ]
     * </pre>
     *
     * @return {@link Map }<{@link String }, {@link List }<{@link Region }>>
     * @since 2022-10-25 09:05:07
     */
    public Map<String, List<Region>> test2() {
        Map<String, List<Region>> map = new HashMap<>();
        // List<Region> regionList = list();
        List<Region> regionList = new ArrayList<>();
        List<Region> emptyList = new ArrayList<>();

        // 将数组数据转为map结构，pcode为key
        Map<String, List<Region>> regionMap = regionList
			.stream()
                .map(item -> {
                    Region region = new Region();
                    BeanUtils.copyProperties(item, region);
                    return region;
                })
                .collect(Collectors.groupingBy(Region::getCode, Collectors.toList()));
        // 上面的Collectors.groupingBy将数据按Pcode分组，方便下面操作

        // 封装树形结构并塞进emptyList数组中
        regionMap.forEach((pcode, collect) -> {
            if (pcode.equals("0")) {
                emptyList.addAll(collect);
            }
            collect.forEach(item -> {
                // item.setCodeTree(regionMap.get(item.getCode()));
                // 因为上面根据pcode分组了，所以这里的collect是以pcode为key的map对象
                // ,item则是当前遍历的pcode底下的children
            });
        });
        map.put("tree", emptyList);
        return map;
    }

    /**
     * java8使用stream流将数据处理成树状结构（非递归） 如果数据是两张表A B，B表的parentId对应A表的id这种形式，可以用下面的方法：
     *
     * <pre class="code">
     *  "data":
     *  [
     *       {
     *         "id": "1181729226915577857",
     *         "title": "第七章：I/O流",
     *         "children": [
     *           {
     *             "id": "1189471423678939138",
     *             "title": "test",
     *             "free": null
     *           },
     *           {
     *             "id": "1189476403626409986",
     *             "title": "22",
     *             "free": null
     *           }
     *         ]
     *       },
     *       {
     *         "id": "15",
     *         "title": "第一章：Java入门",
     *         "children": [
     *           {
     *             "id": "17",
     *             "title": "第一节：Java简介",
     *             "free": null
     *           },
     *           {
     *             "id": "18",
     *             "title": "第二节：表达式和赋值语句",
     *             "free": null
     *           },
     *           {
     *             "id": "19",
     *             "title": "第三节：String类",
     *             "free": null
     *           },
     *           {
     *             "id": "20",
     *             "title": "第四节：程序风格",
     *             "free": null
     *           }
     *         ]
     *       }
     * ]
     * </pre>
     *
     * @return {@link Map }<{@link String }, {@link List }<{@link Region }>>
     * @since 2022-10-25 09:05:07
     */
    public List<Object> getChapterVideoByCourseId() {
        // 	// 章节信息
        // 	List<EduChapter> eduChapterList = eduChapterService.list(null);
        //
        // 	// 小节信息
        // 	List<EduVideo> eduVideoList = eduVideoService.list(null);
        //
        // 	List<ChapterVo> chapterVoList = eduChapterList.stream().map(item1 -> {
        // 		ChapterVo chapterVo = new ChapterVo();
        // 		BeanUtils.copyProperties(item1, chapterVo);
        // 		List<VideoVo> videoVoList = eduVideoList.stream()
        // 			.filter(item2 -> item1.getId().equals(item2.getChapterId()))
        // 			.map(item3 -> {
        // 				VideoVo videoVo = new VideoVo();
        // 				BeanUtils.copyProperties(item3, videoVo);
        // 				return videoVo;
        // 			}).toList();
        // 		chapterVo.setChildren(videoVoList);
        // 		return chapterVo;
        // 	}).toList();
        //
        // 	return chapterVoList;
        return null;
    }
}
