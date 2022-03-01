package com.taotao.cloud.sys.biz.tools.security.service;

import com.sanri.tools.modules.core.security.dtos.ResourceInfo;
import com.sanri.tools.modules.core.security.entitys.ToolMenu;
import com.sanri.tools.modules.core.security.entitys.ToolResource;
import com.sanri.tools.modules.security.service.dtos.ResourceTree;
import com.sanri.tools.modules.security.service.repository.ResourceRepository;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ResourceService {

    @Autowired
    private ResourceRepository resourceRepository;

    /**
     * 从资源列表中过滤出顶级资源, 在存储的时候不能存储太多
     * @param resources 资源列表
     * @return
     */
    public List<ResourceInfo> filterTopResources(List<ResourceInfo> resourceInfos){
        final Map<String, ResourceInfo> resourceInfoMap = resourceInfos.stream().collect(Collectors.toMap(resourceInfo -> resourceInfo.getToolResource().getResourceId(), Function.identity()));

        do {
            boolean haveTopResourceToFilter = false;

            final Iterator<Map.Entry<String,ResourceInfo>> iterator = resourceInfoMap.entrySet().iterator();
            while (iterator.hasNext()){
                final ResourceInfo value = iterator.next().getValue();
                final String parentResourceId = value.getToolResource().getParentResourceId();
                if (resourceInfoMap.containsKey(parentResourceId)){
                    iterator.remove();
                    haveTopResourceToFilter = true;
                }
            }

            if (!haveTopResourceToFilter){
                break;
            }
        }while (true);

        return new ArrayList<>(resourceInfoMap.values());
    }

    /**
     * 从资源中查询菜单信息, 如果是资源或者子资源需要一直向上找到菜单
     * 然后返回路由列表
     */
    public List<Menu> loadMenusFromNames(Collection<String> resources){
        final List<ToolMenu> toolMenus = resourceRepository.findMenus();
        final Map<String, ToolMenu> menus = toolMenus.stream().collect(Collectors.toMap(ToolResource::getResourceId, Function.identity()));

        Set<String> menuResources = new HashSet<>();
        findMenuResources(menus,resources,menuResources);
        final List<String> routerPaths = menuResources.stream().map(menus::get).map(ToolMenu::getRouteName).filter(Objects::nonNull).collect(Collectors.toList());
        return convertMenus(routerPaths);
    }

    /**
     *
     * @param menuPaths 菜单路径列表
     */
    public List<Menu> convertMenus(List<String> menuPaths) {
        // 映射成 map
        Map<Path,Menu> menuMap = new HashMap<>();
        for (String menuPath : menuPaths) {
            final Path path = Paths.get(menuPath);
            final String lastPathPart = path.getName(path.getNameCount() - 1).toString();
            menuMap.put(path,new Menu(lastPathPart));
        }

        // 查询所有根路径
        Set<Menu> rootMenus = new HashSet<>();
        for (Path path : menuMap.keySet()) {
            final Path superParent = path.getRoot().resolve(path.getName(0));
            final Menu menu = menuMap.get(superParent);
            if (menu == null){
                log.warn("一级路径丢失:{}",superParent);
                continue;
            }
            rootMenus.add(menu);
        }

        // 拼装树状结构
        final Iterator<Path> iterator = menuMap.keySet().iterator();
        while (iterator.hasNext()){
            final Path path = iterator.next();
            if (!path.getParent().equals(Paths.get("/"))){
                final Menu menu = menuMap.get(path.getParent());
                if (menu != null) {
                    menu.children.add(menuMap.get(path));
                }else{
                    log.warn("父级路径丢失:{},子级路径将丢失挂载点:{}",path.getParent(),path);
                }
            }
        }

        return new ArrayList<>(rootMenus);
    }

    /**
     * 资源列表加载所有的子级资源
     * @param canGrantResources
     * @return
     */
    public List<String> loadChildResources(Set<String> canGrantResources) {
        final List<ResourceInfo> resourceInfos = resourceRepository.findResources();
        final Set<String> loadChildResources = loadChildResources(resourceInfos, canGrantResources);
        return new ArrayList<>(loadChildResources);
    }

    /**
     * 加载出所有子资源
     * @param resources
     * @return
     */
    private Set<String> loadChildResources(List<ResourceInfo> resourceInfos,Set<String> resources){
        Set<String> childResources = new HashSet<>();
        for (String resourceId : resources) {
            final Set<String> childResourcesPart = resourceInfos.stream()
                    .filter(resourceInfo -> resourceId.equals(resourceInfo.getToolResource().getParentResourceId()))
                    .map(resourceInfo -> resourceInfo.getToolResource().getResourceId())
                    .collect(Collectors.toSet());
            childResources.addAll(childResourcesPart);

            final Set<String> subChild = loadChildResources(resourceInfos, childResourcesPart);
            childResources.addAll(subChild);
        }

        return childResources;
    }

    /**
     * 将资源列表转成资源树结构
     *
     * @param accessResources
     * @return
     */
    public List<ResourceTree> completionToTree(Collection<String> accessResources) {
        // 补充资源完整信息
        final List<ResourceInfo> resourceInfos = resourceRepository.getResources(accessResources);

        Set<ResourceInfo> addChildResources = new HashSet<>();
        completionChildren(resourceInfos,addChildResources);

        // 补全到顶层
        final Set<ResourceInfo> addResourceInfos = new HashSet<>();
        completionToTop(resourceInfos,addResourceInfos);

        resourceInfos.addAll(addResourceInfos);
        resourceInfos.addAll(addChildResources);

        // 映射成 ResourceTree
        final List<ResourceTree> resourceTrees = resourceInfos.stream().distinct().map(ResourceTree::new).collect(Collectors.toList());

        // 转 map
        final Map<String, ResourceTree> resourceTreeMap = resourceTrees.stream().collect(Collectors.toMap(ResourceTree::getId, Function.identity()));

        // 转树, 同时取顶层节点
        List<ResourceTree> tops = new ArrayList<>();
        for (ResourceTree resourceTree : resourceTrees) {
            if (StringUtils.isBlank(resourceTree.getParentId())){
                tops.add(resourceTree);
                continue;
            }
            final ResourceTree parentTree = resourceTreeMap.get(resourceTree.getParentId());
            if (parentTree != null){
                parentTree.getChildren().add(resourceTree);
            }else{
                log.warn("父级节点数据丢失:{}",resourceTree.getParentId());
            }
        }

        return tops;
    }

    /**
     * 一直往下, 补全到子资源
     * @param resourceInfos
     * @param addChildResources
     */
    private void completionChildren(List<ResourceInfo> resourceInfos, Set<ResourceInfo> addChildResources) {
        for (ResourceInfo resourceInfo : resourceInfos) {
            final List<ResourceInfo> children = resourceRepository.findResources().stream().filter(resource -> resourceInfo.getToolResource().getResourceId().equals(resource.getToolResource().getParentResourceId())).collect(Collectors.toList());
            addChildResources.addAll(children);
        }

        if (CollectionUtils.isNotEmpty(addChildResources)){
            Set<ResourceInfo> subAddChildResources = new HashSet<>();
            completionChildren(new ArrayList<>(addChildResources),subAddChildResources);
            addChildResources.addAll(subAddChildResources);
        }
    }

    /**
     * 一直往上, 补全到顶层
     * @param resourceInfos
     * @param addResourceInfos
     */
    private void completionToTop(List<ResourceInfo> resourceInfos, Set<ResourceInfo> addResourceInfos) {
        final Map<String, ResourceInfo> collect = resourceInfos.stream().collect(Collectors.toMap(resourceInfo -> resourceInfo.getToolResource().getResourceId(), Function.identity()));

        for (ResourceInfo resourceInfo : resourceInfos) {
            final String parentResourceId = resourceInfo.getToolResource().getParentResourceId();
            if (StringUtils.isBlank(parentResourceId)){
                // 本身就是 top
                continue ;
            }
            if (!collect.containsKey(parentResourceId)){
                final ResourceInfo addResource = resourceRepository.getResource(parentResourceId);
                if (addResource == null){
                    log.warn("父级资源节点丢失:{}",parentResourceId);
                    continue;
                }
                addResourceInfos.add(addResource);
            }
        }

        if (CollectionUtils.isNotEmpty(addResourceInfos)){
            Set<ResourceInfo> subAddResourceInfos = new HashSet<>();
            completionToTop(new ArrayList<>(addResourceInfos),subAddResourceInfos);
            addResourceInfos.addAll(subAddResourceInfos);
        }
    }


    @Getter
    public static final class Menu{
        private String name;

        public Menu() {
        }

        public Menu(String name) {
            this.name = name;
        }

        private List<Menu> children = new ArrayList<>();

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Menu menu = (Menu) o;

            if (name != null ? !name.equals(menu.name) : menu.name != null) {
                return false;
            }

            return true;
        }

        @Override
        public int hashCode() {
            return name != null ? name.hashCode() : 0;
        }
    }

    /**
     * 从资源列表中找到所有的菜单资源, 一直往上, 找到所有父级菜单
     * @param resources
     * @param menuResources
     */
    private void findMenuResources(Map<String, ToolMenu> menus,Collection<String> resources, Set<String> menuResources) {
        Set<String> currentNotResourcesParentResources = new HashSet<>();
        for (String resource : resources) {
            if (menus.containsKey(resource)){
                menuResources.add(resource);
            }
            final ResourceInfo resourceInfo = resourceRepository.getResource(resource);
            if (resourceInfo == null){
                log.warn("资源丢失:{}",resource);
                continue;
            }
            final String parentResourceId = resourceInfo.getToolResource().getParentResourceId();
            if (StringUtils.isBlank(parentResourceId)){
                continue;
            }
            currentNotResourcesParentResources.add(parentResourceId);
        }
        if (CollectionUtils.isNotEmpty(currentNotResourcesParentResources)){
            findMenuResources(menus,currentNotResourcesParentResources,menuResources);
        }
    }

}
