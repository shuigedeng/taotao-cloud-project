###########################################
cd /root/taotao-cloud/logstash_exporter

go get -u github.com/BonnierNews/logstash_exporter
cd $GOPATH/src/github.com/BonnierNews/logstash_exporter
make
./logstash_exporter -exporter.bind_address :1234 -logstash.endpoint http://localhost:1235

http://localhost:9198/metrics

- job_name: 'prometheus'
    # metrics_path defaults to '/metrics'
    # scheme defaults to 'http'.
    static_configs:
      - targets: ['127.0.0.1:9198']

Grafana画图：
https://grafana.com/dashboards/2525






