package com.taotao.cloud.oss.artislong.core.jdbc.model;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.oss.artislong.core.jdbc.constant.JdbcOssConstant;
import com.taotao.cloud.oss.artislong.exception.NotSupportException;
import com.taotao.cloud.oss.artislong.model.DirectoryOssInfo;
import com.taotao.cloud.oss.artislong.model.FileOssInfo;
import com.taotao.cloud.oss.artislong.model.OssInfo;
import com.taotao.cloud.oss.artislong.utils.OssPathUtil;

import java.util.Date;

/**
 * jdbc操作系统信息
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:40:55
 */
public class JdbcOssInfo {
    private String id;
    private String name;
    private String path;
    private Long size = 0L;
    private Date createTime;
    private Date lastUpdateTime;
    private String parentId;
    private String type;
    private String dataId;

    public OssInfo convertOssInfo(String basePath) {
        if (basePath.endsWith(StrUtil.SLASH)) {
            basePath = basePath.substring(0, basePath.length() - 1);
        }

        String key = this.getPath() + this.getName();
        String path = OssPathUtil.replaceKey(this.getPath(), basePath, true);

        OssInfo ossInfo;
        switch (this.getType()) {
            case JdbcOssConstant.OSS_TYPE.FILE:
                ossInfo = new FileOssInfo();
                ossInfo.setPath(path);
                ossInfo.setLength(this.getSize());
                break;
            case JdbcOssConstant.OSS_TYPE.DIRECTORY:
                ossInfo = new DirectoryOssInfo();
                if (key.equals(basePath)) {
                    ossInfo.setPath(StrUtil.SLASH);
                } else {
                    ossInfo.setPath(path);
                }
                break;
            default:
                throw new NotSupportException("不支持的对象类型");
        }

        ossInfo.setName(this.getName());
        ossInfo.setCreateTime(DateUtil.date(this.getCreateTime()).toString(DatePattern.NORM_DATETIME_PATTERN));
        ossInfo.setLastUpdateTime(DateUtil.date(this.getLastUpdateTime()).toString(DatePattern.NORM_DATETIME_PATTERN));
        return ossInfo;
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDataId() {
		return dataId;
	}

	public void setDataId(String dataId) {
		this.dataId = dataId;
	}
}
