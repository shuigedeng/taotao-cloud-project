##################################
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

cp -r ~/lua-resty-kafka-master/lib/resty/kafka /usr/local/openresty/lualib/resty/

# nginx-lua-prometheus
wget https://github.com/knyar/nginx-lua-prometheus/archive/0.20201218.zip

unzip 0.20201218.zip
mv nginx-lua-prometheus-0.20201218/ nginx-lua-prometheus
cp nginx-lua-prometheus /usr/local/openresty/lualib/resty/

mv /usr/local/openresty/lualib/resty/nginx-lua-prometheus  /usr/local/openresty/lualib/resty/prometheus


##################################
