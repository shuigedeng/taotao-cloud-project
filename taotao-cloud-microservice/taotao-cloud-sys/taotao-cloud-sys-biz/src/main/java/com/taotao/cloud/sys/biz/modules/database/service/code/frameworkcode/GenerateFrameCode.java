package com.taotao.cloud.sys.biz.modules.database.service.code.frameworkcode;

import com.sanri.tools.modules.database.service.code.dtos.CodeGeneratorParam;
import com.sanri.tools.modules.database.service.code.dtos.ProjectGenerateConfig;
import com.sanri.tools.modules.database.service.dtos.meta.TableMeta;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface GenerateFrameCode {

    void generate(ConfigData configData) throws Exception;
}
