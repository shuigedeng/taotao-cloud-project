################################################
wget https://mirror.bit.edu.cn/apache/sqoop/1.4.7/sqoop-1.4.7.bin__hadoop-2.6.0.tar.gz
tar -zxvf sqoop-1.4.7.bin__hadoop-2.6.0.tar.gz -C /root/taotao-bigdata/

mv sqoop-1.4.7.bin__hadoop-2.6.0 sqoop1.4.7

export SQOOP_HOME="/root/taotao-bigdata/sqoop1.4.7"
export PATH=$PATH:$SQOOP_HOME/bin

# 加入mysql驱动包
cp ~/soft/mysql-connector-java-8.0.17.jar /root/taotao-bigdata/sqoop1.4.7/lib

cd /root/taotao-bigdata/sqoop1.4.7/conf
cp sqoop-env-template.sh sqoop-env.sh
vim sqoop-env.sh
# export HADOOP_COMMON_HOME=/root/taotao-bigdata/hadoop3.3.0
# export HADOOP_MAPRED_HOME=/root/taotao-bigdata/hadoop3.3.0
# export HIVE_HOME=/root/taotao-bigdata/hive3.1.2
# export ZOOCFGDIR=/root/taotao-bigdata/zookeeper3.6.2
# export HBASE_HOME=/root/taotao-bigdata/hbase2.3.3

################################################
#!/bin/bash
import            //使用import工具
--connect         //指定连接的目标数据库
jdbc:mysql://node1:3306/environment
--username        //数据库用户名
root
--password        //数据库密码
123456
--table           //要导入的表名
vehicle_owner
--columns         //要导入的列
id,address,email
--where           //查询条件
id>0

#导入到HDFS的路径，mycluster是hadoop下/etc/hadoop/hdfs-site.xml配置的dfs.nameservices，
#如果不是配置hadoop高可用的话可以直接写namenode所在主机的ip或主机映射名
--target-dir
hdfs://mycluster/sqoop1
--delete-target-dir    //若目标目录已存在则删除
-m
1
--as-textfile          //导入的数据以文本格式存放在HDFS上

//如果只导入到hdfs可以不用下面内容
--hive-import        //向hive中导入数据
--hive-overwrite     //数据存在则覆盖
--create-hive-table  //创建Hive表
--hive-table         //指定表名
t_user
--hive-partition-key //指定分区字段
dt
--hive-partition-value  指定分区名
'2020-10-10'
