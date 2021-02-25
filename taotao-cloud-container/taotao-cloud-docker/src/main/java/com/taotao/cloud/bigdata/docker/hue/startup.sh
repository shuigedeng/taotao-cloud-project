#!/bin/sh

cp /etc/apt/sources.list /etc/apt/sources.list.bak
rm -f /etc/apt/sources.list

echo "nameserver 8.8.8.8" >> /etc/resolv.conf

# 配置 国内 apt-get 更新源
echo "deb http://mirrors.aliyun.com/ubuntu/ bionic main restricted universe multiverse" >> /etc/apt/sources.list
echo "deb http://mirrors.aliyun.com/ubuntu/ bionic-updates main restricted universe multiverse" >> /etc/apt/sources.list
echo "deb http://mirrors.aliyun.com/ubuntu/ bionic-proposed main restricted universe multiverse" >> /etc/apt/sources.list
echo "deb http://mirrors.aliyun.com/ubuntu/ bionic-backports main restricted universe multiverse" >> /etc/apt/sources.list
echo "deb-src http://mirrors.aliyun.com/ubuntu/ bionic main restricted universe multiverse" >> /etc/apt/sources.list
echo "deb-src http://mirrors.aliyun.com/ubuntu/ bionic-security main restricted universe multiverse" >> /etc/apt/sources.list
echo "deb-src http://mirrors.aliyun.com/ubuntu/ bionic-updates main restricted universe multiverse" >> /etc/apt/sources.list
echo "deb-src http://mirrors.aliyun.com/ubuntu/ bionic-proposed main restricted universe multiverse" >> /etc/apt/sources.list
echo "deb-src http://mirrors.aliyun.com/ubuntu/ bionic-backports main restricted universe multiverse" >> /etc/apt/sources.list

apt-get update -y

apt install -y mysql-client
./build/env/bin/pip install --upgrade SQLAlchemy -i https://mirrors.aliyun.com/pypi/simple/

maxcounter=45
 
counter=1
while ! mysql -h "database" -u "user" -p"secret" -e "show databases;" > /dev/null 2>&1; 
do
	echo "waiting database..."
    sleep 10
    counter=`expr $counter + 1`
    if [ $counter -gt $maxcounter ]; then
        >&2 echo "We have been waiting for MySQL too long already; failing."
        exit 1
    fi;
done

./build/env/bin/hue syncdb --noinput
./build/env/bin/hue migrate
./build/env/bin/supervisor
