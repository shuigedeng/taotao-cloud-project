## processes  进程管理

##ps查看当前系统执行的线程列表，进行瞬间状态，不是连续状态，连续状态需要使用top名称查看  更多常用参数请使用 man ps查看
ps

##显示所有进程详细信息
ps aux

##-u 显示某个用户的进程列表
ps -f -u www-data

## -C 通过名字或者命令搜索进程
ps -C apache2

## --sort  根据进程cpu使用率降序排列，查看前5个进程  -pcpu表示降序  pcpu升序
ps aux --sort=-pcpu | head -5

##-f 用树结构显示进程的层次关系，父子进程情况下
ps -f --forest -C apache2

##显示一个父进程的所有子进程
ps -o pid,uname,comm -C apache2
ps --ppid 2359

##显示一个进程的所有线程  -L 参数
ps -p 3150 -L

##显示进程的执行时间 -o参数
ps -e -o pid,comm,etime

##watch命令可以用来实时捕捉ps显示进程
watch -n 1 'ps -e -o pid,uname,cmd,pmem,pcpu --sort=-pmem,-pcpu | head -15'

##jobs 查看后台运行的进程  jobs命令执行的结果，＋表示是一个当前的作业，减号表是是一个当前作业之后的一个作业，jobs -l选项可显示所有任务的PID,jobs的状态可以是running, stopped, Terminated,但是如果任务被终止了（kill），shell 从当前的shell环境已知的列表中删除任务的进程标识；也就是说，jobs命令显示的是当前shell环境中所起的后台正在运行或者被挂起的任务信息
jobs

##查看后台运营的进程号
jobs -p

##查看现在被终止或者退出的进程号
jobs -n


##kill命令 终止一个前台进程可以使用Ctrl+C键   kill  通过top或者ps获取进程id号  kill [-s 信号 | -p ] [ -a ] 进程号 ...
##发送指定的信号到相应进程。不指定型号将发送SIGTERM（15）终止指定进程。 关闭进程号12的进程
kill 12


##等同于在前台运行PID为123的进程时按下Ctrl+C键
kill -2 123

##如果任无法终止该程序可用“-KILL” 参数，其发送的信号为SIGKILL(9) ，将强制结束进程
kill -9 123

##列出所有信号名称
##HUP    1    终端断线
##INT     2    中断（同 Ctrl + C）
##QUIT    3    退出（同 Ctrl + \）
##TERM   15    终止
##KILL    9    强制终止
##CONT   18    继续（与STOP相反， fg/bg命令）
##STOP    19    暂停（同 Ctrl + Z）
kill -l

##得到指定信号的数值
kill -l KILL

##杀死指定用户所有进程
kill -u peidalinux
kill -9 $(ps -ef | grep peidalinux)

##将后台中的命令调至前台继续运行  将进程123调至前台执行
fg 123

##将一个在后台暂停的命令，变成继续执行
bg  123

##该命令可以在你退出帐户/关闭终端之后继续运行相应的进程。nohup就是不挂起的意思  下面输出被重定向到myout.file文件中
nohup command > myout.file 2>&1 &

##at：计划任务，在特定的时间执行某项工作，在特定的时间执行一次。
## 格式：at HH:MM YYYY-MM-DD //HH（小时）:MM（分钟） YYYY（年）-MM（月份）-DD（日）
##HH[am pm]+D(天) days //HH（小时）[am（上午）pm（下午）]+days（天）
at 12:00（时间） //at命令设定12:00执行一项操作
#at>useradd aaa //在at命令里设定添加用户aaa
#ctrl+d //退出at命令
#tail -f /etc/passwd //查看/etc/passwd文件后十行是否增加了一个用户aaa

##计划任务设定后，在没有执行之前我们可以用atq命令来查看系统没有执行工作任务。
atq

##启动计划任务后，如果不想启动设定好的计划任务可以使用atrm命令删除。
atrm 1 //删除计划任务1

##pstree命令：列出当前的进程，以及它们的树状结构  格式：pstree [选项] [pid|user]
pstree

##nice命令：改变程序执行的优先权等级 应用程序优先权值的范围从-20～19，数字越小，优先权就越高。一般情况下，普通应用程序的优先权值（CPU使用权值）都是0，如果让常用程序拥有较高的优先权等级，自然启动和运行速度都会快些。需要注意的是普通用户只能在0～19之间调整应用程序的优先权值，只有超级用户有权调整更高的优先权值（从-20～19）。
nice [-n <优先等级>][--help][--version][命令]
nice -n 5 ls

##sleep命令：使进程暂停执行一段时间
date;sleep 1m;date


##renice命令 renice命令允许用户修改一个正在运行进程的优先权。 利用renice命令可以在命令执行时调整其优先权。
##其中，参数number与nice命令的number意义相同。（1） 用户只能对自己所有的进程使用renice命令。（2） root用户可以在任何进程上使用renice命令。（3） 只有root用户才能提高进程的优先权
renice -5 -p 5200  #PID为5200的进程nice设为-5

##pmap命令用于显示一个或多个进程的内存状态。其报告进程的地址空间和内存状态信息 #pmap PID
pmap 20367
