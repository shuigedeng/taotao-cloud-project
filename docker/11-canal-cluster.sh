version: '3'

services:
  canal-server:
    image: canal/canal-server:v1.1.5
    container_name: canal-server
    ports:
      - 11111:11111
    environment:
      - canal.instance.mysql.slaveId=1234
      - canal.auto.scan=false
      - canal.destinations=test
      - canal.instance.master.address=172.16.xx:3306
      # mysql 复制账号
      - canal.instance.dbUsername=canal
      - canal.instance.dbPassword=canal
      - canal.mq.topic=test
      - canal.instance.filter.regex=esen_approval.apt_approval
    volumes:
      - $PWD/conf/:/admin/canal-server/conf/
      - $PWD/logs/:/admin/canal-server/logs/
canal-admin:
    image: canal/canal-admin:v1.1.4
    container_name: canal-admin
    ports:
      - 8089:8089
    environment:
      - server.port=8089
      - canal.adminUser=admin
      - canal.adminPasswd=admin
    volumes:
      - ./canal-admin/logs/:/home/admin/canal-admin/logs
