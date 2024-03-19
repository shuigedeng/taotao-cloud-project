#!/bin/bash

wget https://github.com/azkaban/azkaban/archive/3.90.0.tar.gz
mv 3.90.0.tar.gz azkaban3.90.0.tar.gz

tar -zxvf azkaban3.90.0.tar.gz -C /root/github/

cd azkaban-3.90.0

vim build.gradle

maven { url "http://maven.aliyun.com/nexus/content/groups/public/" }

./gradlew clean
./gradlew installDist

cd azkaban-solo-server/build/libs

cp azkaban-solo-server-0.1.0-SNAPSHOT /root/taotao-bigdata

mv azkaban-solo-server-0.1.0-SNAPSHOT azkaban3.90.0

cd conf
vim azkaban-users.xml
# <user password="admin" roles="metrics.admin" username="admin"/>

##################### azkaban.sh #############################
#!/bin/bash

function start_azkaban() {
  /root/taotao-bigdata/azkaban3.90.0/bin/start-solo.sh
  sleep 10
  echo "azkaban started"
}

function stop_azkaban() {
  /root/taotao-bigdata/azkaban3.90.0/bin/shutdown-solo.sh
  sleep 10
  echo "azkaban stoped"
}

case $1 in
"start")
    start_azkaban
    ;;
"stop")
    stop_azkaban
    ;;
"restart")
    stop_azkaban
    sleep 3
    start_azkaban
    ;;
*)
    echo Invalid Args!
    echo 'Usage: '$(basename $0)' start|stop|restart'
    ;;
esac
