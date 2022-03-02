package com.taotao.cloud.sys.api.dto.zookeeper;

import java.util.Objects;

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

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		PathFavorite that = (PathFavorite) o;
		return Objects.equals(name, that.name) && Objects.equals(path, that.path);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, path);
	}
}
