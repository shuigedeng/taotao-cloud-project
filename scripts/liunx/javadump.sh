#!/bin/sh

DUMP_PIDS=`ps  --no-heading -C java -f --width 1000 |awk '{print $2}'`
if [ -z "$DUMP_PIDS" ]; then
    echo "The server $HOST_NAME is not started!"
    exit 1;
fi

DUMP_ROOT=~/dump
if [ ! -d $DUMP_ROOT ]; then
	mkdir $DUMP_ROOT
fi

DUMP_DATE=`date +%Y%m%d%H%M%S`
DUMP_DIR=$DUMP_ROOT/dump-$DUMP_DATE
if [ ! -d $DUMP_DIR ]; then
	mkdir $DUMP_DIR
fi

for PID in $DUMP_PIDS ; do
#Full thread dump 用来查线程占用，死锁等问题
	$JAVA_HOME/bin/jstack $PID > $DUMP_DIR/jstack-$PID.dump 2>&1
	echo -e ".\c"
#打印出一个给定的Java进程、Java core文件或远程Debug服务器的Java配置信息，具体包括Java系统属性和JVM命令行参数。
	$JAVA_HOME/bin/jinfo $PID > $DUMP_DIR/jinfo-$PID.dump 2>&1
	echo -e ".\c"
#jstat能够动态打印jvm(Java Virtual Machine Statistics Monitoring Tool)的相关统计信息。如young gc执行的次数、full gc执行的次数，各个内存分区的空间大小和可使用量等信息。
	$JAVA_HOME/bin/jstat -gcutil $PID > $DUMP_DIR/jstat-gcutil-$PID.dump 2>&1
	echo -e ".\c"
	$JAVA_HOME/bin/jstat -gccapacity $PID > $DUMP_DIR/jstat-gccapacity-$PID.dump 2>&1
	echo -e ".\c"
#未指定选项时，jmap打印共享对象的映射。对每个目标VM加载的共享对象，其起始地址、映射大小及共享对象文件的完整路径将被打印出来，
	$JAVA_HOME/bin/jmap $PID > $DUMP_DIR/jmap-$PID.dump 2>&1
	echo -e ".\c"
#-heap打印堆情况的概要信息，包括堆配置，各堆空间的容量、已使用和空闲情况
	$JAVA_HOME/bin/jmap -heap $PID > $DUMP_DIR/jmap-heap-$PID.dump 2>&1
	echo -e ".\c"
#-dump将jvm的堆中内存信息输出到一个文件中,然后可以通过eclipse memory analyzer进行分析
#注意：这个jmap使用的时候jvm是处在假死状态的，只能在服务瘫痪的时候为了解决问题来使用，否则会造成服务中断。
	$JAVA_HOME/bin/jmap -dump:format=b,file=$DUMP_DIR/jmap-dump-$PID.dump $PID 2>&1
	echo -e ".\c"
#显示被进程打开的文件信息
if [ -r /usr/sbin/lsof ]; then
	/usr/sbin/lsof -p $PID > $DUMP_DIR/lsof-$PID.dump
	echo -e ".\c"
	fi
done
#主要负责收集、汇报与存储系统运行信息的。
if [ -r /usr/bin/sar ]; then
/usr/bin/sar > $DUMP_DIR/sar.dump
echo -e ".\c"
fi
#主要负责收集、汇报与存储系统运行信息的。
if [ -r /usr/bin/uptime ]; then
/usr/bin/uptime > $DUMP_DIR/uptime.dump
echo -e ".\c"
fi
#内存查看
if [ -r /usr/bin/free ]; then
/usr/bin/free -t > $DUMP_DIR/free.dump
echo -e ".\c"
fi
#可以得到关于进程、内存、内存分页、堵塞IO、traps及CPU活动的信息。
if [ -r /usr/bin/vmstat ]; then
/usr/bin/vmstat > $DUMP_DIR/vmstat.dump
echo -e ".\c"
fi
#报告与CPU相关的一些统计信息
if [ -r /usr/bin/mpstat ]; then
/usr/bin/mpstat > $DUMP_DIR/mpstat.dump
echo -e ".\c"
fi
#报告与IO相关的一些统计信息
if [ -r /usr/bin/iostat ]; then
/usr/bin/iostat > $DUMP_DIR/iostat.dump
echo -e ".\c"
fi
#报告与网络相关的一些统计信息
if [ -r /bin/netstat ]; then
/bin/netstat > $DUMP_DIR/netstat.dump
echo -e ".\c"
fi
echo "OK!"
