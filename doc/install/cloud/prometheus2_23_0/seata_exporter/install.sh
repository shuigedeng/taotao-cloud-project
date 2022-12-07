###########################################
cd /root/taotao-cloud/seata_exporter

seata 配置 prometheus

/seata/actuator/prometheus 的 endpoint

seata application.properties

metrics.enabled=true       #开启metrics
metrics.registryType=compact
metrics.exporterList=prometheus
metrics.exporterPrometheus-port=9898  #metrics端口默认为9898

http://192.168.119.130:9898/metrics

- job_name: 'seata'
    static_configs:
    - targets: ['192.168.57.22:9898']
      labels:
         instance: seata
         hello: 瓜田李下
