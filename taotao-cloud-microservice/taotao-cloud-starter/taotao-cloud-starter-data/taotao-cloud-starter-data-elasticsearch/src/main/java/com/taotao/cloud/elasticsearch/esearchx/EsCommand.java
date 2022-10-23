package com.taotao.cloud.elasticsearch.esearchx;

import java.io.Serializable;

/**
 * ElasticSearch 可执行命令
 *
 * @author shuigedeng
 * @version 2022.05
 * @since 2022-05-16 16:08:50
 */
public class EsCommand implements Serializable {
    public String method;
    public String path;
    public String dsl;
    public String dslType;

    public transient PriHttpTimeout timeout;

    public EsCommand() {
        //设置默认值
        dslType = PriWw.mime_json;
    }
}
