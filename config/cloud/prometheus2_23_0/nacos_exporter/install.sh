###########################################
cd /root/taotao-cloud/nacos_exporter

nacos 配置 prometheus

/nacos/actuator/prometheus 的 endpoint

nacos application.properties

management.endpoints.web.exposure.include=*

http://192.168.119.130:8848/nacos/actuator/prometheus

 # 任务名称
- job_name: 'nacos-cluster'
    scrape_interval: 60s
    metrics_path: '/nacos/actuator/prometheus'
    static_configs:
      - targets:
         - 110.60.40.42:8848
         - 110.90.5.47:8848
         - 110.9.50.15:8848

导入Nacos grafana监控模版

下载地址：https://github.com/nacos-group/nacos-template

Nacos监控分为三个模块:

nacos monitor展示核心监控项
nacos detail展示指标的变化曲线
nacos alert为告警项

和Nacos监控一样，Nacos-Sync也提供了监控模版，导入监控模版
Nacos-Sync监控同样也分为三个模块:
https://github.com/nacos-group/nacos-template/blob/master/nacos-sync-grafana
