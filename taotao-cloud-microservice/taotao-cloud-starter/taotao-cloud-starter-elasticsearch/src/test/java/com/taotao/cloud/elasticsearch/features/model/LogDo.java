package com.taotao.cloud.elasticsearch.features.model;

import java.util.Date;

public class LogDo {
    public long log_id;
    public String logger;
    public String trace_id;
    public int level;
    public String tag;
    public String tag1;
    public String tag2;
    public String tag3;
    public String summary;
    public String content;
    public String class_name;
    public String thread_name;
    public String from;
    public int log_date;
    public Date log_fulltime;

    public Double _score;

    @Override
    public String toString() {
        return "LogDo{" +
                "log_id=" + log_id +
                ", logger='" + logger + '\'' +
                ", trace_id='" + trace_id + '\'' +
                ", level=" + level +
                ", tag='" + tag + '\'' +
                ", tag1='" + tag1 + '\'' +
                ", tag2='" + tag2 + '\'' +
                ", tag3='" + tag3 + '\'' +
                ", summary='" + summary + '\'' +
                ", content='" + content + '\'' +
                ", class_name='" + class_name + '\'' +
                ", thread_name='" + thread_name + '\'' +
                ", from='" + from + '\'' +
                ", log_date=" + log_date +
                ", log_fulltime=" + log_fulltime +
                ", _score=" + _score +
                '}';
    }


}
