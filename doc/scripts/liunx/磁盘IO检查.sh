##iostat是查看磁盘活动统计情况

##显示所有设备负载情况 r/s:  每秒完成的读 I/O 设备次数。即 rio/s；w/s:  每秒完成的写 I/O 设备次数。即 wio/s等
iostat

##每隔2秒刷新磁盘IO信息，并且每次显示3次
iostat 2 3

#显示某个磁盘的IO信息
iostat -d sda1

##显示tty和cpu信息
iostat -t

##以M为单位显示磁盘IO信息
iostat -m

##查看TPS和吞吐量信息  kB_read/s：每秒从设备（drive expressed）读取的数据量；kB_wrtn/s：每秒向设备（drive expressed）写入的数据量；kB_read：读取的总数据量；kB_wrtn：写入的总数量数据量；
iostat -d -k 1 1

#查看设备使用率（%util）、响应时间（await）
iostat -d -x -k 1 1

#查看CPU状态
iostat -c 1 3

#统计进程(pid)的stat,进程的stat自然包括进程的IO状况
pidstat

#只显示IO
pidstat -d  1

#-d IO 信息,-r 缺页及内存信息-u CPU使用率-t 以线程为统计单位1  1秒统计一次
pidstat -u -r -d -t 1

#文件级IO分析,查看当前文件由哪些进程打开
lsof
ls /proc/pid/fd

#利用 sar 报告磁盘 I/O 信息DEV 正在监视的块设备 tps 每秒钟物理设备的 I/O 传输总量 rd_sec/s 每秒从设备读取的扇区数量 wr_sec/s 每秒向设备写入的扇区数量 avgrq-sz I/O 请求的平均扇区数
#avgqu-sz I/O 请求的平均队列长度 await I/O 请求的平均等待时间，单位为毫秒 svctm I/O 请求的平均服务时间，单位为毫秒 %util I/O 请求所占用的时间的百分比，即设备利用率
sar -pd 10 3

#iotop  top的io版
iotop

#查看页面缓存信息 其中的Cached 指用于pagecache的内存大小（diskcache-SwapCache）。随着写入缓存页，Dirty 的值会增加 一旦开始把缓存页写入硬盘,Writeback的值会增加直到写入结束。
cat /proc/meminfo

#查看有多少个pdflush进程 Linux 用pdflush进程把数据从缓存页写入硬盘
#pdflush的行为受/proc/sys/vm中的参数的控制/proc/sys/vm/dirty_writeback_centisecs (default 500): 1/100秒, 多长时间唤醒pdflush将缓存页数据写入硬盘。默认5秒唤醒2个（更多个）线程。如果wrteback的时间长于dirty_writeback_centisecs的时间，可能会出问题
cat /proc/sys/vm/nr_pdflush_threads

#查看I/O 调度器
#调度算法
#noop anticipatory deadline [cfq]
#deadline :    deadline 算法保证对既定的IO请求以最小的延迟时间。
#anticipatory：    有个IO发生后，如果又有进程请求IO，则产生一个默认6ms猜测时间，猜测下一个进程请求IO是干什么。这对于随机读取会造成较大的延时。对数据库应用很糟糕，而对于Web Server等则会表现不错。
#cfq:        对每个进程维护一个IO队列，各个进程发来的IO请求会被cfq以轮循方式处理，对每一个IO请求都是公平。适合离散读的应用。
#noop:        对所有IO请求都用FIFO队列形式处理。默认IO不会存在性能问题。
cat /sys/block/[disk]/queue/scheduler


#改变IO调度器
$ echo deadline > /sys/block/sdX/queue/scheduler
#提高调度器请求队列的
$ echo 4096 > /sys/block/sdX/queue/nr_requests
