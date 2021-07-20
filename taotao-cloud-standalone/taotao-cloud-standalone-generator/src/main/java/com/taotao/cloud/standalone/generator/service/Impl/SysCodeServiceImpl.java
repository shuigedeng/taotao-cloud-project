package com.taotao.cloud.standalone.generator.service.Impl;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.taotao.cloud.standalone.generator.domain.CodeGenConfig;
import com.taotao.cloud.standalone.generator.domain.SysColumnEntity;
import com.taotao.cloud.standalone.generator.domain.SysTableEntity;
import com.taotao.cloud.standalone.generator.mapper.SysCodeMapper;
import com.taotao.cloud.standalone.generator.service.SysCodeService;
import com.taotao.cloud.standalone.generator.util.CodeGenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Classname SysCodeServiceImpl
 * @Description 代码生成服务实现类
 * @Author shuigedeng
 * @since 2019-08-02 14:21
 * @Version 1.0
 */
@Service
public class SysCodeServiceImpl implements SysCodeService {

    @Autowired
    private SysCodeMapper sysCodeMapper;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.driver-class-name}")
    private String driverName;


    @Override
    public List<SysTableEntity> findTableList(String tableSchema) {
        return sysCodeMapper.findTableList(tableSchema);
    }

    @Override
    public List<SysColumnEntity> findColumnList(String tableName, String tableSchema) {
        return sysCodeMapper.findColumnList(tableName, tableSchema);
    }

    @Override
    public boolean generatorCode(CodeGenConfig codeGenConfig) {
        DataSourceConfig dataSourceConfig = new DataSourceConfig()
                .setDbType(DbType.MYSQL)
                .setUrl(url)
                .setUsername(username)
                .setPassword(password)
                .setDriverName(driverName);
        CodeGenUtil codeGenUtil = new CodeGenUtil();
        codeGenUtil.generateByTables(dataSourceConfig, codeGenConfig.getPackageName(), codeGenConfig.getAuthor(), codeGenConfig.getModuleName(), codeGenConfig.getTableName());
        return true;
    }
}
