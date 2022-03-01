package com.taotao.cloud.sys.biz.tools.security.service.repository;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import com.sanri.tools.modules.core.aspect.SerializerToFile;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Repository;

import com.sanri.tools.modules.core.service.file.FileManager;

import lombok.extern.slf4j.Slf4j;

/**
 * 分组管理
 */
@Repository
@Slf4j
public class GroupRepository implements InitializingBean {
    private final FileManager fileManager;

    /**
     * 所有组织信息
     */
    private static final List<Path> groups = new ArrayList<>();

    public GroupRepository(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    /**
     * 查询所有组织列表
     */
    public List<Path> findGroups(){
        return groups;
    }

    /**
     * 查询所有的子组织
     * @param path 组织路径
     * @return
     */
    public List<Path> childGroups(Path path){
        return groups.stream().filter(group -> group.startsWith(path)).collect(Collectors.toList());
    }

    /**
     * 是否存在组织
     * @param path 组织路径
     * @return
     */
    public boolean existGroup(Path path){
        for (Path group : groups) {
            if (path.startsWith(group)){
                return true;
            }
        }
        return false;
    }

    /**
     * 添加组织
     * @param path 组织路径
     */
    @SerializerToFile
    public void addGroup(Path path){
        if (existGroup(path)){
            return;
        }
        groups.add(path);
    }

    /**
     * 删除一个组织
     * @param path 组织路径
     */
    @SerializerToFile
    public void deleteGroup(Path path){
        final Iterator<Path> iterator = groups.iterator();
        while (iterator.hasNext()){
            final Path group = iterator.next();
            if (group.startsWith(path)){
                iterator.remove();
            }
        }

        // 上面会把本身也删除,需要重新添加回来
        groups.add(path);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 读取分组信息
        final String readConfig = fileManager.readConfig("security", "groups");
        if (StringUtils.isNotBlank(readConfig)) {
            final String[] lines = StringUtils.split(readConfig, '\n');
            final List<Path> groups = Arrays.stream(lines).map(Paths::get).collect(Collectors.toList());
            GroupRepository.groups.addAll(groups);
        }
    }
    /**
     * 分组信息序列化到文件
     */
    public void serializer() throws IOException {
        final List<String> paths = groups.stream().map(GroupRepository::convertPathToString).collect(Collectors.toList());
        fileManager.writeConfig("security","groups",StringUtils.join(paths,'\n'));
    }

    /**
     * 将 path 转成路径方式表示
     * @param path
     * @return
     */
    public static String convertPathToString(Path path){
        final int nameCount = path.getNameCount();
        if (nameCount == 0){
            // is root
            return "/";
        }
        StringBuffer toString = new StringBuffer();
        for (int i = 0; i < nameCount; i++) {
            toString.append("/").append(path.getName(i));
        }
        return toString.toString();
    }
}
