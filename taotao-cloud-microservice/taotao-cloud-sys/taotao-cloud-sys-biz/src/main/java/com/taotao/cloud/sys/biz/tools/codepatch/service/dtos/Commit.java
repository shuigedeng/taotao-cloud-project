package com.taotao.cloud.sys.biz.tools.codepatch.service.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;

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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getCommitId() {
		return commitId;
	}

	public void setCommitId(String commitId) {
		this.commitId = commitId;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}
}
