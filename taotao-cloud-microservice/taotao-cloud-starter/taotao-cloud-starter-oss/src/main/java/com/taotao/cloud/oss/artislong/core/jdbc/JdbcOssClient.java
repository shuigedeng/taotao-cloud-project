package com.taotao.cloud.oss.artislong.core.jdbc;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.oss.artislong.core.StandardOssClient;
import com.taotao.cloud.oss.artislong.core.jdbc.constant.JdbcOssConstant;
import com.taotao.cloud.oss.artislong.core.jdbc.model.JdbcOssConfig;
import com.taotao.cloud.oss.artislong.core.jdbc.model.JdbcOssInfo;
import com.taotao.cloud.oss.artislong.exception.OssException;
import com.taotao.cloud.oss.artislong.model.DirectoryOssInfo;
import com.taotao.cloud.oss.artislong.model.FileOssInfo;
import com.taotao.cloud.oss.artislong.model.OssInfo;
import com.taotao.cloud.oss.artislong.utils.OssPathUtil;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class JdbcOssClient implements StandardOssClient {

    public static final String JDBC_OBJECT_NAME = "jdbcTemplate";

    private JdbcTemplate jdbcTemplate;
    private JdbcOssConfig jdbcOssConfig;

	public JdbcOssClient(JdbcTemplate jdbcTemplate, JdbcOssConfig jdbcOssConfig) {
		this.jdbcTemplate = jdbcTemplate;
		this.jdbcOssConfig = jdbcOssConfig;
	}

	@Override
    public OssInfo upLoad(InputStream is, String targetName, Boolean isOverride) {
        try {
            String dateId = saveOssData(is);
            String key = getKey(targetName, true);
            Long size = Convert.toLong(is.available());
            JdbcOssInfo jdbcOssInfo;
            if (isExist(targetName) && isOverride) {
                jdbcOssInfo = getOssInfo(key);
                updateOssData(jdbcOssInfo.getDataId(), is);
                updateOssInfo(jdbcOssInfo.getId(), key, size, jdbcOssInfo.getParentId());
                jdbcOssInfo.setSize(size);
                jdbcOssInfo.setLastUpdateTime(new Date());
            } else {
                String parentPath = OssPathUtil.convertPath(Paths.get(key).getParent().toString(), true);
                String parentId = mkdir(parentPath);
                jdbcOssInfo = saveOssInfo(key, size, parentId, JdbcOssConstant.OSS_TYPE.FILE, dateId);
            }
            OssInfo ossInfo = jdbcOssInfo.convertOssInfo(getBasePath());
            ossInfo.setName(StrUtil.equals(targetName, StrUtil.SLASH) ? targetName : FileNameUtil.getName(targetName));
            ossInfo.setPath(OssPathUtil.replaceKey(targetName, ossInfo.getName(), true));
            return ossInfo;
        } catch (Exception e) {
            throw new OssException(e);
        }
    }

    @Override
    public OssInfo upLoadCheckPoint(File file, String targetName) {
        LogUtil.warn("Jdbc存储不支持断点续传上传，将使用普通上传");
        return upLoad(FileUtil.getInputStream(file), targetName);
    }

    @Override
    public void downLoad(OutputStream os, String targetName) {
        JdbcOssInfo jdbcOssInfo = getOssInfo(getKey(targetName, true));
        try {
            InputStream inputStream = getOssData(jdbcOssInfo.getDataId());
            IoUtil.copy(inputStream, os);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void downLoadCheckPoint(File localFile, String targetName) {
	    LogUtil.warn("Jdbc存储不支持断点续传下载，将使用普通下载");
        downLoad(FileUtil.getOutputStream(localFile), targetName);
    }

    @Override
    public void delete(String targetName) {
        String key = getKey(targetName, true);
        JdbcOssInfo jdbcOssInfo = getOssInfo(key);
        deleteOssData(jdbcOssInfo.getDataId());
        deleteOssInfo(jdbcOssInfo.getId());
    }

    @Override
    public void copy(String sourceName, String targetName, Boolean isOverride) {
        if (isOverride || !isExist(targetName)) {
            String sourceKey = getKey(sourceName, true);
            String targetKey = getKey(targetName, true);
            JdbcOssInfo jdbcOssInfo = getOssInfo(sourceKey);
            String targetDataId = copyOssData(jdbcOssInfo.getDataId());
            copyOssInfo(jdbcOssInfo.getId(), targetKey, targetDataId);
        }
    }

    @Override
    public void move(String sourceName, String targetName, Boolean isOverride) {
        if (isOverride || !isExist(targetName)) {
            String sourceKey = getKey(sourceName, true);
            String targetKey = getKey(targetName, true);
            JdbcOssInfo jdbcOssInfo = getOssInfo(sourceKey);
            String targetParentId = mkdir(targetKey);
            updateOssInfo(jdbcOssInfo.getId(), targetKey, jdbcOssInfo.getSize(), targetParentId);
        }
    }

    @Override
    public void rename(String sourceName, String targetName, Boolean isOverride) {
        if (isOverride || !isExist(targetName)) {
            String sourceKey = getKey(sourceName, true);
            String targetKey = getKey(targetName, true);
            JdbcOssInfo jdbcOssInfo = getOssInfo(sourceKey);
            String parentPath = OssPathUtil.convertPath(Paths.get(targetKey).getParent().toString(), true);
            String targetParentId = mkdir(parentPath);
            updateOssInfo(jdbcOssInfo.getId(), targetKey, jdbcOssInfo.getSize(), targetParentId);
        }
    }

    @Override
    public Boolean isExist(String targetName) {
        JdbcOssInfo jdbcOssInfo = getOssInfo(getKey(targetName, true));
        return ObjectUtil.isNotEmpty(jdbcOssInfo) && jdbcOssInfo.getSize() > 0;
    }

    @Override
    public Boolean isFile(String targetName) {
        JdbcOssInfo jdbcOssInfo = getOssInfo(getKey(targetName, true));
        return JdbcOssConstant.OSS_TYPE.FILE.equals(jdbcOssInfo.getType());
    }

    @Override
    public Boolean isDirectory(String targetName) {
        JdbcOssInfo jdbcOssInfo = getOssInfo(getKey(targetName, true));
        return JdbcOssConstant.OSS_TYPE.DIRECTORY.equals(jdbcOssInfo.getType());
    }

    @Override
    public OssInfo getInfo(String targetName, Boolean isRecursion) {
        String key = getKey(targetName, true);
        JdbcOssInfo baseJdbcOssInfo = getOssInfo(key);

        OssInfo ossInfo = baseJdbcOssInfo.convertOssInfo(getBasePath());
        ossInfo.setName(StrUtil.equals(targetName, StrUtil.SLASH) ? targetName : FileNameUtil.getName(targetName));
        ossInfo.setPath(OssPathUtil.replaceKey(targetName, ossInfo.getName(), true));

        if (JdbcOssConstant.OSS_TYPE.DIRECTORY.equals(baseJdbcOssInfo.getType()) && isRecursion) {
            String basePath = "";
            if ("0".equals(baseJdbcOssInfo.getParentId())) {
                basePath = baseJdbcOssInfo.getPath();
            } else {
                basePath = baseJdbcOssInfo.getPath() + baseJdbcOssInfo.getName();
            }
            List<JdbcOssInfo> jdbcOssInfos = getOssInfos(basePath);
            Map<String, List<JdbcOssInfo>> jdbcOssInfoMaps = jdbcOssInfos.stream().collect(Collectors.groupingBy(JdbcOssInfo::getParentId));
            ossInfo = getChildren(jdbcOssInfoMaps, baseJdbcOssInfo.getId(), ossInfo);
        }
        return ossInfo;
    }

    public OssInfo getChildren(Map<String, List<JdbcOssInfo>> jdbcOssInfoMaps, String parentId, OssInfo ossInfo) {
        List<OssInfo> fileOssInfos = new ArrayList<>();
        List<OssInfo> directoryInfos = new ArrayList<>();

        List<JdbcOssInfo> children = jdbcOssInfoMaps.get(parentId);
        for (JdbcOssInfo child : children) {
            if (JdbcOssConstant.OSS_TYPE.DIRECTORY.equals(child.getType())) {
                OssInfo directoryOssInfo = child.convertOssInfo(getBasePath());
                directoryInfos.add(getChildren(jdbcOssInfoMaps, child.getId(), directoryOssInfo));
            } else {
                OssInfo fileOssInfo = child.convertOssInfo(getBasePath());
                fileOssInfos.add(fileOssInfo);
            }

            if (ObjectUtil.isNotEmpty(fileOssInfos) && fileOssInfos.get(0) instanceof FileOssInfo) {
                ReflectUtil.setFieldValue(ossInfo, "fileInfos", fileOssInfos);
            }
            if (ObjectUtil.isNotEmpty(directoryInfos) && directoryInfos.get(0) instanceof DirectoryOssInfo) {
                ReflectUtil.setFieldValue(ossInfo, "directoryInfos", directoryInfos);
            }
        }
        return ossInfo;
    }

    @Override
    public Map<String, Object> getClientObject() {
        return new HashMap<String, Object>() {
            {
                put(JDBC_OBJECT_NAME, getJdbcTemplate());
            }
        };
    }

    @Override
    public String getBasePath() {
        return jdbcOssConfig.getBasePath();
    }

    public String mkdir(String path) {
        List<String> paths = StrUtil.split(path, StrUtil.SLASH, false, false);
        StringBuilder fullPath = new StringBuilder();
        JdbcOssInfo parentOssInfo = null;
        for (int i = 0; i < paths.size(); i++) {
            String pathName = StrUtil.SLASH + paths.get(i);
            if (i != 0) {
                fullPath.append(pathName);
            }

            String key = ObjectUtil.isEmpty(fullPath.toString()) ? StrUtil.SLASH : fullPath.toString();

            JdbcOssInfo jdbcOssInfo = getOssInfo(key);
            if (jdbcOssInfo == null) {
                String parentId = parentOssInfo == null ? "0" : parentOssInfo.getId();
                jdbcOssInfo = saveOssInfo(key, 0L, parentId, JdbcOssConstant.OSS_TYPE.DIRECTORY, "");
            }
            parentOssInfo = jdbcOssInfo;
        }
        return parentOssInfo.getId();
    }

    public JdbcOssInfo saveOssInfo(String key, Long size, String parentId, String type, String dateId) {
        String name = StrUtil.equals(key, StrUtil.SLASH) ? key : FileNameUtil.getName(key);
        DateTime now = DateUtil.date();

        JdbcOssInfo jdbcOssInfo = new JdbcOssInfo();
        jdbcOssInfo.setId(IdUtil.fastSimpleUUID());
        jdbcOssInfo.setName(name);
        jdbcOssInfo.setPath(OssPathUtil.replaceKey(key, name, true));
        jdbcOssInfo.setSize(size);
        jdbcOssInfo.setCreateTime(now.toJdkDate());
        jdbcOssInfo.setLastUpdateTime(now.toJdkDate());
        jdbcOssInfo.setParentId(parentId);
        jdbcOssInfo.setType(type);
        jdbcOssInfo.setDataId(dateId);

        jdbcTemplate.update("INSERT INTO OSS_STORE (ID, NAME, PATH, LENGTH, CREATE_TIME, LAST_UPDATE_TIME, PARENT_ID, TYPE, DATA_ID)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)", ps -> {
            ps.setString(1, jdbcOssInfo.getId());
            ps.setString(2, jdbcOssInfo.getName());
            ps.setString(3, jdbcOssInfo.getPath());
            ps.setLong(4, jdbcOssInfo.getSize());
            ps.setDate(5, now.toSqlDate());
            ps.setDate(6, now.toSqlDate());
            ps.setString(7, jdbcOssInfo.getParentId());
            ps.setString(8, jdbcOssInfo.getType());
            ps.setString(9, jdbcOssInfo.getDataId());
        });
        return jdbcOssInfo;
    }

    public void deleteOssInfo(String id) {
        jdbcTemplate.update("DELETE FROM OSS_STORE T WHERE T.ID = ?", id);
    }

    public void updateOssInfo(String id, String key, Long size, String parentId) {
        String name = StrUtil.equals(key, StrUtil.SLASH) ? key : FileNameUtil.getName(key);
        DateTime now = DateUtil.date();
        String path = OssPathUtil.replaceKey(key, name, true);
        jdbcTemplate.update("UPDATE OSS_STORE T SET T.NAME = ?, T.PATH = ?, T.LENGTH = ?, T.LAST_UPDATE_TIME = ?, T.PARENT_ID = ? WHERE T.ID = ?", ps -> {
            ps.setString(1, name);
            ps.setString(2, path);
            ps.setLong(3, size);
            ps.setDate(4, now.toSqlDate());
            ps.setString(5, parentId);
            ps.setString(6, id);
        });
    }

    public JdbcOssInfo getOssInfo(String key) {
        String name = StrUtil.equals(key, StrUtil.SLASH) ? key : FileNameUtil.getName(key);
        String path = OssPathUtil.replaceKey(key, name, true);
        List<JdbcOssInfo> jdbcOssInfos = jdbcTemplate.query("SELECT * FROM OSS_STORE T WHERE T.NAME = ? AND T.PATH = ?", BeanPropertyRowMapper.newInstance(JdbcOssInfo.class), name, path);
        if (ObjectUtil.isNotEmpty(jdbcOssInfos) && jdbcOssInfos.size() == 1) {
            return jdbcOssInfos.get(0);
        } else {
            return null;
        }
    }

    public List<JdbcOssInfo> getOssInfos(String path) {
        return jdbcTemplate.query("SELECT * FROM OSS_STORE T WHERE T.PATH LIKE concat('', ?, '%')", BeanPropertyRowMapper.newInstance(JdbcOssInfo.class), path);
    }

    public String copyOssInfo(String sourceId, String targetKey, String targetDataId) {
        String targetId = IdUtil.fastSimpleUUID();
        String name = StrUtil.equals(targetKey, StrUtil.SLASH) ? targetKey : FileNameUtil.getName(targetKey);
        String targetPath = OssPathUtil.replaceKey(targetKey, name, true);
        DateTime now = DateUtil.date();
        jdbcTemplate.update("INSERT INTO OSS_STORE (ID, NAME, PATH, LENGTH, CREATE_TIME, LAST_UPDATE_TIME, PARENT_ID, TYPE, DATA_ID) " +
                "SELECT ?, ?, ?, SIZE, ?, ?, PARENT_ID, TYPE, ? FROM OSS_STORE T WHERE T.ID = ?",
                targetId, name, targetPath, now.toSqlDate(), now.toSqlDate(), targetDataId, sourceId);
        return targetId;
    }

    public String saveOssData(InputStream inputStream) {
        String dataId = IdUtil.fastSimpleUUID();
        jdbcTemplate.update("INSERT INTO OSS_DATA(ID, DATA) VALUES(?, ?)", ps -> {
            ps.setString(1, dataId);
            ps.setBlob(2, inputStream);
        });
        return dataId;
    }

    public void deleteOssData(String id) {
        jdbcTemplate.update("DELETE FROM OSS_DATA T WHERE T.ID = ?", id);
    }

    public void updateOssData(String id, InputStream inputStream) {
        jdbcTemplate.update("UPDATE OSS_DATA T SET T.DATA = ? WHERE T.ID = ?", ps -> {
            ps.setBlob(1, inputStream);
            ps.setString(2, id);
        });
    }

    public InputStream getOssData(String id) throws SQLException {
        return jdbcTemplate.queryForObject("SELECT DATA FROM OSS_DATA T WHERE T.ID = ?", Blob.class, id).getBinaryStream();
    }

    public String copyOssData(String sourceDataId) {
        String targetDataId = IdUtil.fastSimpleUUID();
        jdbcTemplate.update("INSERT INTO OSS_DATA (ID, DATA) SELECT ?, DATA FROM OSS_DATA T WHERE T.ID = ?", targetDataId, sourceDataId);
        return targetDataId;
    }

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public JdbcOssConfig getJdbcOssConfig() {
		return jdbcOssConfig;
	}

	public void setJdbcOssConfig(JdbcOssConfig jdbcOssConfig) {
		this.jdbcOssConfig = jdbcOssConfig;
	}
}
