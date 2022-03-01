package com.taotao.cloud.sys.biz.tools.zookeeper.dtos;


import java.util.List;

public class PathFavoriteParam {
    private String connName;
    private List<PathFavorite> favorites;

	public String getConnName() {
		return connName;
	}

	public void setConnName(String connName) {
		this.connName = connName;
	}

	public List<PathFavorite> getFavorites() {
		return favorites;
	}

	public void setFavorites(
		List<PathFavorite> favorites) {
		this.favorites = favorites;
	}
}
