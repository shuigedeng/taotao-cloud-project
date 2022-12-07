#查看当前系统load
uptime

#查看系统状态和每个进程的系统资源使用状况
top

#可视化显示CPU的使用状况
htop

#查看每个CPU的负载信息
mpstat -P ALL 1

#每隔1秒查看磁盘IO的统计信息
iostat -xkdz 1

#每隔一秒查看虚拟内存的使用信息
vmstat 1

#查看内存使用统计信息
free

#查看网络使用信息
nicstat -z 1

#类似vmstat的显示优化的工具
dstat 1

#查看系统活动状态，比如系统分页统计，块设备IO统计等
sar

#网络连接状态查看
netstat -s

#进程资源使用信息查看
pidstat 1
pidstat -d 1

#查看某个进程的系统调用信息 -p后面是进程id，-tttT 进程系统后的系统调用时间
strace -tttT -p 12670
#统计IO设备输入输出的系统调用信息
strace -c dd if=/dev/zero of=/dev/null bs=512 count=1024k


#tcpdump 查看网络数据包
tcpdump -nr /tmp/out.tcpdump

#块设备的读写事件信息统计
btrace /dev/sdb

#iotop查看某个进程的IO操作统计信息
iotop -bod5

#slabtop 查看内核 slab内存分配器的使用信息
slabtop -sc

#系统参数设置
sysctl -a

#系统性能指标统计信息
perf stat gzip file1
#系统cpu活动状态查看
perf record -a -g -F 997 sleep 10
