package com.taotao.cloud.sys.biz.api.controller.tools.zookeeper.dtos;

import lombok.Data;

import java.util.List;

@Data
public class PathFavoriteParam {
    private String connName;
    private List<PathFavorite> favorites;
}
