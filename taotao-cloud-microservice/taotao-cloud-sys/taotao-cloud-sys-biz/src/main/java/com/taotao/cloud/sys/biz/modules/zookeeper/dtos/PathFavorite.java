package com.taotao.cloud.sys.biz.modules.zookeeper.dtos;

import lombok.Data;

/**
 * zookeeper 路径收藏
 */
@Data
public class PathFavorite {
    private String name;
    private String path;

    public PathFavorite() {
    }

    public PathFavorite(String name, String path) {
        this.name = name;
        this.path = path;
    }
}
