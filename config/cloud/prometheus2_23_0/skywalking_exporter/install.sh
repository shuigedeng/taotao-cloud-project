###########################################
官方默认支持, 所以只需要在prometheus配置抓取地址即可

telemetry:
  selector: ${SW_TELEMETRY:prometheus}
  none:
  prometheus:
    host: ${SW_TELEMETRY_PROMETHEUS_HOST:0.0.0.0}
    port: ${SW_TELEMETRY_PROMETHEUS_PORT:1234}
    sslEnabled: ${SW_TELEMETRY_PROMETHEUS_SSL_ENABLED:false}
    sslKeyPath: ${SW_TELEMETRY_PROMETHEUS_SSL_KEY_PATH:""}
    sslCertChainPath: ${SW_TELEMETRY_PROMETHEUS_SSL_CERT_CHAIN_PATH:""}

prometheus-fetcher:
  selector: ${SW_PROMETHEUS_FETCHER:default}
  default:
    active: ${SW_PROMETHEUS_FETCHER_ACTIVE:true}

重启 OAP ，让修改的配置文件生效。

在 UI 中选择 SelfObservability ，然后在服务列表中选择 oap-server
