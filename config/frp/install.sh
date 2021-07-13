https://github.com/fatedier/frp/releases

wget https://github.com/fatedier/frp/releases/download/v0.33.0/frp_0.33.0_linux_amd64.tar.gz

mkdir /etc/frp && tar -zxvf frp_0.33.0_linux_amd64.tar.gz -C /etc/frp

vim /etc/frp/frps.ini

[common]
bind_port = 8500
dashboard_port = 8501
dashboard_user = admin
dashboard_pwd = xxxxx
vhost_http_port = 8502
vhost_https_port = 8503
subdomain_host = taotaocloud.top
token = xxxxxx

nohup sh /etc/frp/frps -c /etc/frp/frps.ini &


https://github.com/fatedier/frp/releases/download/v0.33.0/frp_0.33.0_linux_amd64.tar.gz

mkdir /etc/frp && tar -zxvf frp_0.33.0_linux_amd64.tar.gz -C /etc/frp

vim frpc.ini

nohup sh /etc/frp/frpc -c /etc/frp/frpc.ini &
