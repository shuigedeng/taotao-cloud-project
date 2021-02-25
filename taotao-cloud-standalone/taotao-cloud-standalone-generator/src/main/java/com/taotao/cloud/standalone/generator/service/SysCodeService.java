package com.taotao.cloud.standalone.generator.service;


import com.taotao.cloud.standalone.generator.domain.CodeGenConfig;
import com.taotao.cloud.standalone.generator.domain.SysColumnEntity;
import com.taotao.cloud.standalone.generator.domain.SysTableEntity;

import java.util.List;

/**
 * @Classname SysCodeService
 * @Description 代码生成服务类
 * @Author Created by Lihaodong (alias:小东啊) lihaodongmail@163.com
 * @since 2019-08-02 14:21
 * @Version 1.0
 */
public interface SysCodeService {


    List<SysTableEntity> findTableList(String tableSchema);

    List<SysColumnEntity> findColumnList(String tableName, String tableSchema);

    /**
     * 代码生成
     * @param codeGenConfig
     * @return
     */
    boolean generatorCode(CodeGenConfig codeGenConfig);;
}
