package com.taotao.cloud.sys.biz.tools.zookeeper.dtos;

/**
 * zookeeper 路径收藏
 */
public class PathFavorite {
    private String name;
    private String path;

    public PathFavorite() {
    }

    public PathFavorite(String name, String path) {
        this.name = name;
        this.path = path;
    }

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
