package com.taotao.cloud.sys.biz.modules.database.service.code.frameworkcode;


import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface GenerateFrameCode {

    void generate(ConfigData configData) throws Exception;
}
