###########################################
cd /root/taotao-cloud/spark_exporter


wget https://github.com/prometheus/pushgateway/releases/download/v1.4.1/pushgateway-1.4.1.linux-amd64.tar.gz

tar -zxvf  pushgateway-1.0.0.linux-amd64.tar.gz

nohup ./pushgateway &

http://192.168.10.220:9091

- job_name: 'pushgateway'
    static_configs:
      - targets: ['node1:9091']
        labels:
          instance: 'pushgateway'

metrics.reporter.promgateway.class: org.apache.flink.metrics.prometheus.PrometheusPushGatewayReporter
metrics.reporter.promgateway.host: node1
metrics.reporter.promgateway.port: 9091
metrics.reporter.promgateway.jobName: myjob
metrics.reporter.promgateway.randomJobNameSuffix: false
metrics.reporter.promgateway.deleteOnShutdown: true

  在flink-conf.yaml中添加上面的内容，然后再启动flinkjob即可
