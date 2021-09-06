package com.taotao.cloud.prometheus.text;


import com.taotao.cloud.prometheus.model.ServiceCheckNotice;

@FunctionalInterface
public interface ServiceMonitorResolver extends NoticeTextResolver<ServiceCheckNotice> {

}
