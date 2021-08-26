package com.taotao.cloud.prometheus.text;


import com.taotao.cloud.prometheus.pojos.servicemonitor.ServiceCheckNotice;

@FunctionalInterface
public interface ServiceMonitorResolver extends NoticeTextResolver<ServiceCheckNotice> {

}
