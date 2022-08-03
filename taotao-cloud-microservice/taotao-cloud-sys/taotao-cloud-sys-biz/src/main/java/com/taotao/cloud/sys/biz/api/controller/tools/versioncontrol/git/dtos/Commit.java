package com.taotao.cloud.sys.biz.api.controller.tools.versioncontrol.git.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;

@Data
public class Commit {
    private String message;
    private String author;
    private String commitId;
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;

    public Commit() {
    }

    public Commit(String message, String author, String commitId, Date time) {
        this.message = message;
        this.author = author;
        this.commitId = commitId;
        this.time = time;
    }

    /**
     * 转可识别字符串
     * @return
     */
    public String toInfo(){
        return commitId + "\t" + message + "\t" + author + "\t" + DateFormatUtils.ISO_8601_EXTENDED_DATETIME_FORMAT.format(time);
    }
}
