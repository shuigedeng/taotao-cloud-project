package com.taotao.cloud.sys.biz.tools.core.dtos;

import org.springframework.context.ApplicationEvent;

public class UpdateConnectEvent extends ApplicationEvent {

    public UpdateConnectEvent(ConnectInfo connectInfo) {
        super(connectInfo);
    }

    public static class ConnectInfo{
        private String connName;
        private String data;
        private Class<?> clazz;
        private String module;

        public ConnectInfo() {
        }

        public ConnectInfo(String connName, String data, Class<?> clazz) {
            this.connName = connName;
            this.data = data;
            this.clazz = clazz;
        }

        public ConnectInfo(String connName, String data, Class<?> clazz, String module) {
            this.connName = connName;
            this.data = data;
            this.clazz = clazz;
            this.module = module;
        }

	    public String getConnName() {
		    return connName;
	    }

	    public void setConnName(String connName) {
		    this.connName = connName;
	    }

	    public String getData() {
		    return data;
	    }

	    public void setData(String data) {
		    this.data = data;
	    }

	    public Class<?> getClazz() {
		    return clazz;
	    }

	    public void setClazz(Class<?> clazz) {
		    this.clazz = clazz;
	    }

	    public String getModule() {
		    return module;
	    }

	    public void setModule(String module) {
		    this.module = module;
	    }
    }



}
