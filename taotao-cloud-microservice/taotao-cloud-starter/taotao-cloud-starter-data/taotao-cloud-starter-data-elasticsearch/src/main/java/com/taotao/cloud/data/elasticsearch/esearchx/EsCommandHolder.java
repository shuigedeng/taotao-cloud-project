package com.taotao.cloud.data.elasticsearch.esearchx;

/**
 * es命令持有人
 *
 * @author shuigedeng
 * @version 2022.05
 * @since 2022-05-16 16:08:58
 */
public class EsCommandHolder {
    private final EsContext context;
    private final EsCommand command;

    private long timespan;

    public EsCommandHolder (EsContext context,EsCommand command){
        this.context = context;
        this.command = command;
    }

    public void setTimespan(long timespan) {
        this.timespan = timespan;
    }

    public long getTimespan() {
        return timespan;
    }

    public EsContext getContext() {
        return context;
    }

    public String getMethod() {
        return command.method;
    }

    public String getPath() {
        return command.path;
    }

    public String getDsl() {
        return command.dsl;
    }

    public String getDslType() {
        return command.dslType;
    }
}
