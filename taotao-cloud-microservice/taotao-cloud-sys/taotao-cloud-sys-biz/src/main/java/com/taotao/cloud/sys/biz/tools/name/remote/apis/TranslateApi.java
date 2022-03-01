package com.taotao.cloud.sys.biz.tools.name.remote.apis;

import com.dtflys.forest.annotation.DataObject;
import com.dtflys.forest.annotation.Request;
import com.dtflys.forest.callback.OnSuccess;
import com.taotao.cloud.sys.biz.tools.name.remote.dtos.BaiduTranslateRequest;
import com.taotao.cloud.sys.biz.tools.name.remote.dtos.BaiduTranslateResponse;
import com.taotao.cloud.sys.biz.tools.name.remote.dtos.YoudaoTranslateRequest;
import com.taotao.cloud.sys.biz.tools.name.remote.dtos.YoudaoTranslateResponse;
import org.apache.http.entity.ContentType;

import java.util.Set;

public interface TranslateApi {
    static final String YOUDAO_URL = "http://openapi.youdao.com/api";
    static final String BAIDU_URL = "http://api.fanyi.baidu.com/api/trans/vip/translate";

    static final ContentType contentType = ContentType.APPLICATION_FORM_URLENCODED.withCharset("utf-8");

    @Request(
            url = YOUDAO_URL,
            type = "post",
            dataType = "json"
    )
    YoudaoTranslateResponse youdaoTranslate(@DataObject YoudaoTranslateRequest youdaoTranslateRequest);

    @Request(
            url = BAIDU_URL,
            dataType = "json"
    )
    BaiduTranslateResponse baiduTranslate(@DataObject BaiduTranslateRequest baiduTranslateRequest);
}
