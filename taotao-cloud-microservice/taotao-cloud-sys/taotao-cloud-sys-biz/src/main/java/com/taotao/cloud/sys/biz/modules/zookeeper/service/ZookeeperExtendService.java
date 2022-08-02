package com.taotao.cloud.sys.biz.modules.zookeeper.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.taotao.cloud.sys.biz.modules.core.service.connect.dtos.ConnectOutput;
import com.taotao.cloud.sys.biz.modules.core.service.connect.events.DeleteSecurityConnectEvent;
import com.taotao.cloud.sys.biz.modules.core.service.file.FileManager;
import com.taotao.cloud.sys.biz.modules.zookeeper.dtos.PathFavorite;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

/**
 * 扩展功能 : 收藏路径,几个重要的路径可以初始化加载
 */
@Service
@Slf4j
public class ZookeeperExtendService implements ApplicationListener<DeleteSecurityConnectEvent> {
    /**
     * 路径收藏  connName ==> Set<PathFavorite>
     */
    private static final Map<String, Set<PathFavorite>> pathFavorites = new HashMap<>();

    @Autowired
    private FileManager fileManager;

    /**
     * 添加收藏 ,前端需要把所有的收藏全拿过来,后端直接覆盖
     */
    public void addFavorite(String connName, PathFavorite pathFavorite){
        Set<PathFavorite> pathFavorites = ZookeeperExtendService.pathFavorites.computeIfAbsent(connName, k -> new LinkedHashSet<>());
        pathFavorites.add(pathFavorite);
        serializer();
    }

    public void removeFavorite(String connName,String name){
        Set<PathFavorite> pathFavorites = ZookeeperExtendService.pathFavorites.computeIfAbsent(connName, k -> new LinkedHashSet<>());
        Iterator<PathFavorite> iterator = pathFavorites.iterator();
        while (iterator.hasNext()){
            PathFavorite next = iterator.next();
            if (next.getName().equals(name)){
                iterator.remove();break;
            }
        }

        serializer();
    }

    /**
     * 列出当前连接关注的路径列表
     * @param connName
     * @return
     */
    public Set<PathFavorite> favorites(String connName){
        Set<PathFavorite> pathFavorites = ZookeeperExtendService.pathFavorites.computeIfAbsent(connName, k -> new LinkedHashSet<PathFavorite>());
        return pathFavorites;
    }

    /**
     * 序列化收藏列表到文件
     */
    private void serializer() {
        try {
            fileManager.writeConfig(ZookeeperService.module,"favorites", JSON.toJSONString(pathFavorites));
        } catch (IOException e) {
            log.error("zookeeper serializer favorites error : {}",e.getMessage(),e);
        }
    }

    @PostConstruct
    void loadFavorites(){
        try {
            String favorites = fileManager.readConfig(ZookeeperService.module, "favorites");
            TypeReference<Map<String,Set<PathFavorite>>> typeReference =  new TypeReference<Map<String,Set<PathFavorite>>>(){};
            final Map<String, Set<PathFavorite>> stringSetMap = JSON.parseObject(favorites, typeReference);
            if (stringSetMap != null) {
                pathFavorites.putAll(stringSetMap);
            }
        } catch (IOException e) {
            log.error("zookeeper load path favorites error : {}",e.getMessage(),e);
        }
    }

    @Override
    public void onApplicationEvent(DeleteSecurityConnectEvent event) {
        final ConnectOutput connectOutput = (ConnectOutput) event.getSource();
        if (ZookeeperService.module.equals(connectOutput.getConnectInput().getModule())){
            final String baseName = connectOutput.getConnectInput().getBaseName();
            log.info("zookeeper 删除连接收藏夹[{}]",baseName);
            pathFavorites.remove(baseName);
        }
    }
}
