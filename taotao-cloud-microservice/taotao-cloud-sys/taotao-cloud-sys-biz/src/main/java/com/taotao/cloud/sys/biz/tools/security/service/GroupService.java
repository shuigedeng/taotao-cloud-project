package com.taotao.cloud.sys.biz.tools.security.service;

import com.sanri.tools.modules.core.exception.ToolException;
import com.sanri.tools.modules.core.security.dtos.GroupTree;
import com.sanri.tools.modules.security.service.repository.GroupRepository;
import com.sanri.tools.modules.security.service.repository.ResourceRepository;
import com.sanri.tools.modules.security.service.repository.RoleRepository;
import com.sanri.tools.modules.security.service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ResourceRepository resourceRepository;

    /**
     * 添加一个组织
     * @param parentGroup 父级组织
     * @param childGroup 子组织
     */
    public void addGroup(String parentGroup, String childGroup){
        if (childGroup.startsWith("/")){
            throw new ToolException("子组织不能以 / 开头");
        }
        if (Paths.get(parentGroup).equals(Paths.get("/"))){
            groupRepository.addGroup(Paths.get(childGroup));
            return ;
        }
        groupRepository.addGroup(Paths.get(parentGroup,childGroup));
    }

    /**
     * 删除组织
     * @param path 组织全路径
     */
    public void delGroup(String path){
        groupRepository.deleteGroup(Paths.get(path));
    }

    /**
     * 查找组织挂载的用户
     * @param group 组织路径
     * @param includeChild 是否包含子级
     */
    public Set<String> findGroupUsers(Path group, boolean includeChild) {
        return userRepository.findUsersByGroup(group,includeChild);
    }

    /**
     * 查询组织挂载的角色
     * @param group         组织路径
     * @param includeChild  是否包含子级
     * @return
     */
    public Set<String> findGroupRoles(Path group, boolean includeChild){
        return roleRepository.findRolesByGroup(group,includeChild);
    }

    /**
     * 查询组织挂载的资源信息
     * @param group
     * @param includeChild
     * @return
     */
    public Set<String> findGroupResources(Path group, boolean includeChild){
        return resourceRepository.findResourcesByGroup(group,includeChild);
    }

    /**
     * 查询组织的所有子组织
     * @param group 组织路径
     * @return
     */
    public Set<String> childGroups(String group){
        final List<Path> paths = groupRepository.childGroups(Paths.get(group));
        return paths.stream().map(GroupRepository::convertPathToString).collect(Collectors.toSet());
    }

    /**
     * 过滤出顶层 group 列表
     * @param groups 给定的组织列表
     */
    public Set<Path> filterTopGroups(Set<String> groups){
        Set<Path> topGroups = new HashSet<>();

        A: for (String group : groups) {
            final Path filterOne = Paths.get(group);

            final Iterator<Path> iterator = topGroups.iterator();
            while (iterator.hasNext()){
                final Path next = iterator.next();
                if (next.startsWith(filterOne)){
                    iterator.remove();
                    topGroups.add(filterOne);
                    continue A;
                }
                if (filterOne.startsWith(next)){
                    continue A;
                }
            }
            topGroups.add(filterOne);
        }

        return topGroups;
    }

    /**
     * 将一批路径转成树结构
     * @param groups 分组列表
     * @return
     */
    public static GroupTree convertPathsToGroupTree(List<Path> groups){
        final List<Path> groupPaths = groups.stream().collect(Collectors.toList());

        GroupTree top = new GroupTree("顶层");
        for (Path groupPath : groupPaths) {
            convertToGroupTree(groupPath, top, 0);
        }
        final List<GroupTree> children = top.getChildren();

        // 找出 children 共同的父级组织, 从路径的前面往后找,找到相同起始的路径当做父级,如果没有,则父级为根
        Set<Path> paths = children.stream().map(GroupTree::getPath).map(Paths::get).collect(Collectors.toSet());
        // 找到最短路径
        final Integer minPath = paths.stream().map(Path::getNameCount).min((a, b) -> a - b).get();
        Path topPath = Paths.get("/");
        for (int i = 0; i < minPath; i++) {
            Set<String> partPaths = new HashSet<>();
            for (Path path : paths) {
                final String part = path.getName(i).toString();
                partPaths.add(part);
            }
            if (partPaths.size() == 1){
                topPath.resolve(partPaths.iterator().next());
                continue;
            }
            break;
        }

        // 设置顶层
        if (topPath.equals(Paths.get("/"))){
            top.setPath("/");
            top.setName("/");
        }else{
            top.setPath(GroupRepository.convertPathToString(topPath));
            top.setName(topPath.getName(topPath.getNameCount() - 1).toString() + "_"+ topPath.getNameCount());
        }

        return top;
    }

    private static void convertToGroupTree(Path path, GroupTree root, int deep){
        final int nameCount = path.getNameCount();
        if (deep >= nameCount){
            return ;
        }
        final String pathName = path.getName(deep).toString() + "_"+deep;
        final List<GroupTree> children = root.getChildren();
        for (GroupTree child : children) {
            final String childName = child.getName();
            if (pathName.equals(childName)){
                convertToGroupTree(path,child,++deep);
                return ;
            }
        }

        final GroupTree groupTree = new GroupTree(pathName);
        groupTree.setPath(GroupRepository.convertPathToString(path.subpath(0, deep + 1)));
        root.addChild(groupTree);
        groupTree.setParent(root);
        convertToGroupTree(path,groupTree,++deep);
    }
}
