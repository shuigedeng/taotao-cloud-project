package com.taotao.cloud.sys.biz.tools.core.controller.dtos;


public class ListFileInfo {
    private String name;
    private String path;
    private long size;
    private boolean directory;
    private long lastUpdateTime;

    public ListFileInfo() {
    }

    public ListFileInfo(String name,long size, boolean directory, long lastUpdateTime) {
        this.name = name;
        this.size = size;
        this.directory = directory;
        this.lastUpdateTime = lastUpdateTime;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public boolean isDirectory() {
		return directory;
	}

	public void setDirectory(boolean directory) {
		this.directory = directory;
	}

	public long getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
}
