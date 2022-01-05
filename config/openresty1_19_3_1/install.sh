##################################
dnf install dnf-plugin-config-manager
dnf config-manager --add-repo  https://openresty.org/package/centos/openresty.repo
dnf install openresty

which openresty && file `which openresty`  --> /usr/local/openresty/nginx/sbin/nginx

openresty -V

mkdir /opt/openresty
mkdir -p /opt/openresty/{config/taotao-cloud,taotao-bigdata/lua,pid,logs}

cp /usr/local/openresty/nginx/conf/mime.types /opt/openresty/conf
cp /usr/local/openresty/nginx/conf/nginx.conf /opt/openresty/conf

# lua-resty-kafka
wget https://github.com/doujiang24/lua-resty-kafka/archive/master.zip
unzip master.zip

cp -r ~/lua-resty-kafka-master/lib/resty/kafka /usr/local/openresty/lib/resty/

# nginx-lua-prometheus
wget https://github.com/knyar/nginx-lua-prometheus/archive/0.20201218.zip

unzip 0.20201218.zip
mv nginx-lua-prometheus-0.20201218/ nginx-lua-prometheus
cp nginx-lua-prometheus /usr/local/openresty/lualib/resty/

mv /usr/local/openresty/lualib/resty/nginx-lua-prometheus  /usr/local/openresty/lualib/resty/prometheus

##################################

cd /opt/openresty

openresty -p /opt/openresty -c conf/nginx.conf -t
openresty -p /opt/openresty -c conf/nginx.conf
openresty -p /root/openresty -c conf/nginx.conf -s stop
openresty -p /opt/openresty -c conf/nginx.conf -s reload

openresty -p /opt/openresty -c conf/nginx.conf -t
openresty -p /opt/openresty -c conf/nginx.conf
openresty -p /opt/openresty -c conf/nginx.conf -s stop
openresty -p /opt/openresty -c conf/nginx.conf -s reload

echo "0 23 * * * sh /opt/openresty/script/spilt-access-log.sh >> /opt/openresty/script/logs/spilt-access-log.log 2>&1" > /opt/openresty/script/taotao-cloud-access-log.cron

crontab /opt/openresty/script/taotao-cloud-access-log.cron

crontab -l
crontab -e


################### 安装其他模块 ##################
wget https://openresty.org/download/openresty-1.19.3.1.tar.gz
tar -zxvf openresty-1.19.3.1.tar.gz
cd openresty-1.19.3.1/

# 检测是否支持
./configure --help | grep http_gzip_static_module

git clone https://github.com/google/ngx_brotli.git
cd /soft/ngx_brotli && git submodule update --init && cd /soft/openresty-1.19.3.1/build/nginx-1.19.3

yum -y install pcre pcre-devel
yum -y install openssl openssl-devel
yum -y install lua* --skip-broken

## 编译 不要gmake install
./configure  --add-module=/soft/ngx_brotli --with-http_gzip_static_module -j2
gmake

## 备份
cd /usr/local/openresty/nginx/sbin && cp nginx nginx_back

## 覆盖
cp /soft/openresty-1.19.3.1/build/nginx-1.19.3/objs/nginx /usr/local/openresty/nginx/sbin/

## 测试
./nginx  -V
