package com.taotao.cloud.sys.biz.tools.security.service.repository;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import com.sanri.tools.modules.core.security.dtos.RoleInfo;
import com.sanri.tools.modules.core.security.entitys.ToolMenu;
import com.sanri.tools.modules.security.configs.UrlSecurityPermsLoad;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import com.sanri.tools.modules.core.security.dtos.ResourceInfo;
import com.sanri.tools.modules.core.security.entitys.ToolResource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * 实现方式
 * 菜单
 *  子菜单
 *    资源
 *      子资源
 */
@Repository
@Slf4j
public class ResourceRepository implements InitializingBean {

    /**
     * 所有资源信息
     */
    private static final Map<String,ResourceInfo> resourceInfos = new HashMap<>();

    /**
     * 菜单列表  资源名 => 菜单资源
     */
    private static final Map<String,ToolMenu> menus = new HashMap<>();

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 获取菜单列表
     * @return
     */
    public List<ToolMenu> findMenus(){
        return new ArrayList<>(ResourceRepository.menus.values());
    }

    /**
     * 获取资源列表
     * @return
     */
    public List<ResourceInfo> findResources(){
        return new ArrayList<>(resourceInfos.values());
    }

    /**
     * 获取资源映射列表
     * @return
     */
    public Map<String,ResourceInfo> findResourcesMap(){
        return resourceInfos;
    }

    /**
     * 获取资源信息
     * @param resourceId 资源名称
     */
    public ResourceInfo getResource(String resourceId){
        return resourceInfos.get(resourceId);
    }

    /**
     * 批量获取资源信息
     * @param resourceIds 资源名称列表
     * @return
     */
    public List<ResourceInfo> getResources(Collection<String> resourceIds){
        return resourceIds.stream().map(resourceInfos::get).filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
//        final ClassLoader classLoader = UrlSecurityPermsLoad.class.getClassLoader();

        // 加载所有的资源信息
//        final Enumeration<URL> resources = classLoader.getResources("resources.conf");
        final Resource[] resources = applicationContext.getResources("classpath*:*.resources.conf");
        for (Resource resource : resources) {
            loadResource(resource);
        }
        // 加载所有的菜单信息
//        final Enumeration<URL> menusConf = classLoader.getResources("menus.conf");
        final Resource[] menusResources = applicationContext.getResources("classpath*:*.menus.conf");
        for (Resource menusResource : menusResources) {
            loadResource(menusResource);
        }

        // 把菜单列表过滤出来
        for (ResourceInfo value : resourceInfos.values()) {
            final ToolResource toolResource = value.getToolResource();
            if (toolResource instanceof ToolMenu) {
                menus.put(toolResource.getResourceId(), (ToolMenu) toolResource);
            }
        }

        // 资源读取完成, 需要添加分组
        final Set<String> collect = resourceInfos.values().stream().flatMap(resourceInfo -> resourceInfo.getGroups().stream()).collect(Collectors.toSet());
        for (String group : collect) {
            groupRepository.addGroup(Paths.get(group));
        }
    }

    private void loadResource(Resource resourceFile) throws IOException {
        try(final InputStream inputStream = resourceFile.getInputStream();){
            final List<String> lines = IOUtils.readLines(inputStream, StandardCharsets.UTF_8);
            for (String line : lines) {
                if (StringUtils.isBlank(line) || line.startsWith("#")){
                    // 忽略注释和空行
                    continue;
                }
                // 去两端空格
                line = StringUtils.trim(line);

                final String[] splitLine = StringUtils.splitPreserveAllTokens(line, ":");
                if (splitLine.length < 6) {
                    log.warn("错误的资源权限配置:{}",line);
                    continue;
                }
                final ToolResource toolResource = new ToolResource(splitLine[0], splitLine[1], splitLine[2], splitLine[3], splitLine[4]);
                final ResourceInfo resource = new ResourceInfo(toolResource);
                if (StringUtils.isNotBlank(splitLine[5])){
                    final String[] groupArray = StringUtils.split(splitLine[5], ',');
                    resource.setGroups(Arrays.asList(groupArray));
                }

                if (splitLine.length > 6){
                    final String[] split = StringUtils.split(resourceFile.getFilename(), ".");
                    final String pluginName = split[0];
                    final ToolMenu toolMenu = new ToolMenu(resource.getToolResource());
                    resource.setToolResource(toolMenu);
                    // 第 7 个配置是路由配置
                    toolMenu.setRouteName(splitLine[6]);
                    toolMenu.setPluginName(pluginName);
                }
                resourceInfos.put(splitLine[0],resource);
            }
        }
    }

    /**
     * 查询组织包含的资源
     * @param path          组织路径
     * @param includeChild 是否包含子组织
     * @return
     */
    public Set<String> findResourcesByGroup(Path findPath, boolean includeChild) {
        Set<String> resourceIds = new HashSet<>();
        A: for (ResourceInfo value : resourceInfos.values()) {
            final List<String> groups = value.getGroups();
            for (String group : groups) {
                final Path resourceGroupPath = Paths.get(group);
                if (!includeChild && resourceGroupPath.equals(findPath)){
                    resourceIds.add(value.getToolResource().getResourceId());
                    continue A;
                }else if (includeChild && resourceGroupPath.startsWith(findPath)){
                    resourceIds.add(value.getToolResource().getResourceId());
                    continue A;
                }
            }
        }
        return resourceIds;
    }
}
