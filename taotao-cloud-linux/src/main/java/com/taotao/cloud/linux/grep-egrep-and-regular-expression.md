# grep

> 第5天 【grep及正则表达式(02)】

- Linux 上文本处理三剑客
    + grep：文本过滤（模式：pattern）工具；
        * grep, egrep, fgrep
    + sed：stream editor，文本编辑工具；
    + awk：Linux上的实现为gawk，文本报告生成器；

## grep：Global search REgular expression and Print out the line.

作用：文本搜索工具，根据用户指定的“模式”对目标文本逐行进行匹配检查；打印匹配到的行；

模式：由正则表达式字符及文本字符所编写的过滤条件；

REGEXP：由一类特殊字符及文本字符所编写的模式，其中有些字符不表示字符字面意义，而表示控制或统配的功能。

- 分两类：
    + 基本正则表达式：BRE
    + 扩展正则表达式：ERE
        * grep -E, egrep

正则表达式引擎：执行正则表达式的算法程序代码，如 pcre。

grep 命令：

```
grep [OPTIONS] PATTERN [FILE...]

选项：
    --color=auto：对匹配到的文本着色显示；
    -v：显示不能够被pattern匹配到的行；
    -i：忽略字符大小写；
        ~]# grep -i "Root" /etc/passwd
        root:x:0:0:root:/root:/bin/bash
        operator:x:11:0:operator:/root:/sbin/nologin
        dockerroot:x:997:994:Docker User:/var/lib/docker:/sbin/nologin
    -o：仅显示匹配到的字符串；
        ~]# grep -i -o "Root" /etc/passwd
        root
        root
        root
        root
        root
    -q：静默模式，不输出任何信息；
        ~]# grep -q -i -o "Root" /etc/passwd
        ~]# echo $?
    -A #：after，显示匹配到的行及后续#行；
    -B #：before，显示匹配到的行及前面#行；
    -C #：context，显示匹配到的行及前后各#行；

    -E：使用扩展的正则表达式（ERE）；
```

## 基本正则表达式元字符

1、字符匹配

```
.：匹配任意单个字符；
[]：匹配指定范围内的任意单个字符；
[^]：匹配指定范围外的任意单个字符
    [:digit:]、[:lower:]、[:upper:]、[:alpha:]、[:alnum:]、[:punct:]、[:space:]
```

2、匹配次数

用在要指定次数的字符后面，用于指定前面的字符要出现的次数；

```
*：匹配前面的字符任意次；
    例如：grep "x*y"
        abxy
        xay
        xxxxxxy

.*：任意长度的任意字符；
    ~]# grep "a.*y" grep.txt
    abxy
    xay

\?：匹配前面的字符0或1次；即前面的字符可有可无；
    ~]# grep 'x\?y' grep.txt
    abxy
    xay
    xxxxxxy

\+：匹配其前面的字符至少1次；
    ~]# grep 'x\+y' grep.txt
    abxy
    xxxxxxy

\{m\}：匹配前面的字符m次；
     ~]# grep '[[:alpha:]]\{3\}t' /etc/passwd
    root:x:0:0:root:/root:/bin/bash
    shutdown:x:6:0:shutdown:/sbin:/sbin/shutdown
    halt:x:7:0:halt:/sbin:/sbin/halt

\{m,n\}：匹配前面的字符至少m次，至多n次；
    ~]# grep '[[:alpha:]]\{1,3\}t' /etc/passwd
    root:x:0:0:root:/root:/bin/bash
    shutdown:x:6:0:shutdown:/sbin:/sbin/shutdown
    halt:x:7:0:halt:/sbin:/sbin/halt

    \{0,n}：匹配前面的字符至多n次；
    \{m,\}：匹配前面的字符至少m次；
```

3、位置锚定

```
^：行首锚定；
    ^root

    ~]# grep '^[[:alpha:]]\{1,3\}t' /etc/passwd
    root:x:0:0:root:/root:/bin/bash
    shutdown:x:6:0:shutdown:/sbin:/sbin/shutdown
    halt:x:7:0:halt:/sbin:/sbin/halt
    ftp:x:14:50:FTP User:/var/ftp:/sbin/nologin

$：行尾锚定
    root$

    ~]# grep '[[:alpha:]]\{1,3\}t$' /etc/passwd
    halt:x:7:0:halt:/sbin:/sbin/halt

^PATTERN$：用于模式匹配整行；
    ~]# grep '^[[:alpha:]]\{1,3\}t$' /etc/passwd

^$：空行；
    ^[[:space:]]*$：有空格的空行；

\< 或 \b：词首锚定；用于单词模式的左侧

\> 或 \b：词尾没顶；用于单词模式的右侧

\<PATTERN\>：匹配整个单词；
```

4、分组

```
\(\)：将一个或多个字符捆绑在一起，当作一个整体进行处理；
    \(xy\)*ab

~]# cat grep.txt
abxy
xay
xxxxxxy
xyxyxyabcxy

~]# grep "\(xy\)\+" grep.txt
abxy
xxxxxxy
xyxyxyabcxy

Note：分组括号中的模式匹配到的内容会被正则表达式引擎记录于内部的变量中，这些变量的命名方式为：\1, \2, \3, ...
    \1：从左侧起，第一个左括号以及与之匹配右括号之间的模式所匹配到的字符；
        \(ab\+\(xy\)*\)：
            \1：ab\+\(xy\)*
            \2：xy

后项引用：引用前面的分组括号中的模式所匹配的字符，（而非模式本身）
~]# grep '\([[:alpha:]]\{1,3\}t\>\).*\1' /etc/passwd
root:x:0:0:root:/root:/bin/bash
halt:x:7:0:halt:/sbin:/sbin/halt
```

练习：

1、显示/proc/meminfo文件中以大小s开头的行；（要求：使用两种方式）

```
~]# grep "^[sS]" /proc/meminfo
SwapCached:            0 kB
SwapTotal:       2097148 kB
SwapFree:        2097148 kB
Shmem:             98776 kB
Slab:             103616 kB
SReclaimable:      74256 kB
SUnreclaim:        29360 kB

~]# grep -i "^s" /proc/meminfo
SwapCached:            0 kB
SwapTotal:       2097148 kB
SwapFree:        2097148 kB
Shmem:             98776 kB
Slab:             103616 kB
SReclaimable:      74256 kB
SUnreclaim:        29360 kB
```

2、显示/etc/passwd文件中不以/bin/bash结尾的行；

```
~]# grep -v "/bin/bash$" /etc/passwd
bin:x:1:1:bin:/bin:/sbin/nologin
daemon:x:2:2:daemon:/sbin:/sbin/nologin
adm:x:3:4:adm:/var/adm:/sbin/nologin
```

3、显示/etc/passwd文件中ID号最大的用户的用户名；

```
~]# sort -t: -n -k3 /etc/passwd | tail -1 | cut -d: -f1
user1
```

4、如果用户root存在，显示其默认的shell程序；

```
~]# grep "^\<root\>" /etc/passwd | cut -d: -f7
/bin/bash

~]# awk -F: '/^\<root\>/{print $NF}' /etc/passwd
/bin/bash
```

5、找出/etc/passwd中的两位或三位数；

```
~]# grep "\<[[:digit:]]\{2,3\}\>" /etc/passwd
mail:x:8:12:mail:/var/spool/mail:/sbin/nologin
operator:x:11:0:operator:/root:/sbin/nologin
games:x:12:100:games:/usr/games:/sbin/nologin
ftp:x:14:50:FTP User:/var/ftp:/sbin/nologin
```

6、找出/etc/rc.d/rc.sysinit文件中，至少以一个空白字符开头且后面存在非空白字符的行；

```
~]# grep "^[[:space:]]\+[^[:space:]]" /etc/rc.d/rc.sysinit
    . /etc/sysconfig/network
    HOSTNAME=localhost
    mount -n -t proc /proc /proc
    mount -n -t sysfs /sys /sys >/dev/null 2>&1
    modprobe usbcore >/dev/null 2>&1 && mount -n -t u
```

7、找出"netstat -tan"命令的结果中以"LISTEN"后跟0、1或多个空白字符结尾的行；

```
~]# netstat -tan | grep "LISTEN[[:space:]]*$" 
tcp        0      0 0.0.0.0:6783                0.0.0.0:*                   LISTEN      
tcp        0      0 127.0.0.1:9121              0.0.0.0:*                   LISTEN      
tcp        0      0 127.0.0.1:9090              0.0.0.0:*                   LISTEN      
tcp        0      0 0.0.0.0:3938                0.0.0.0:*                   LISTEN      
tcp        0      0 127.0.0.1:9187              0.0.0.0:*                   LISTEN      
tcp        0      0 127.0.0.1:9093              0.0.0.0:*                   LISTEN   
```

8、添加用户bash, testbash, basher以及nologin(其shell为/sbin/nologin)；而后找出/etc/passwd文件中用户名同shell名的行；

```
~]# grep "^\(\<[[:alnum:]]\+\>\).*\1$" /etc/passwd
sync:x:5:0:sync:/sbin:/bin/sync
shutdown:x:6:0:shutdown:/sbin:/sbin/shutdown
halt:x:7:0:halt:/sbin:/sbin/halt
zsh:x:1000:1000::/home/zsh:/bin/zsh
bash:x:1004:1008::/home/bash:/bin/bash
nologin:x:1007:1011::/home/nologin:/sbin/nologin
```

练习2

1、写一个脚本，实现如下功能：如果user1用户存在，就显示其存在，否则添加之；显示添加的用户的id号等信息；

```bash
#!/bin/bash
#

if id user1 &> /dev/null;
then
        echo "user1 already exists."
else
        useradd user1
        id user1
fi
```

2、写一个脚本，完成如下功能：如果root用户登录了当前系统，就显示root用户在线；否则说明其未登录；

```bash
#!/bin/bash
#

who | grep "^root\>" &> /dev/null && echo "root was online" || echo "root is offline"
```

> 第6天 【egrep及bash中的变量(01)】

## egrep及扩展正则表达式

egrep = grep -E

```
egrep [OPTIONS] PATTERN [FILE...]
```

### 扩展正则表达式的原字符：

1、字符匹配：

```
.
[]
[^]
```

2、次数匹配：

```
*
?：0或1次；
+：1次或多次
{m}：匹配m次；
{m,n}：至少m，至多n次；
```

3、锚定：

```
^
$
\<,\b
\>,\b
```

4、分组：

```
()

后项引用：\1, \2, ...
```

5、或者：

```
|：或者
    C|cat：C或cat
    (vmx|svm)：vms或svm
```

练习：

1、显示当前系统root、centos或user1用户的默认shell和UID；

```
~]# egrep "^(root|centos|user1)\>" /etc/passwd | cut -d: -f3,7
0:/bin/bash
1003:/bin/bash
```

2、找出/etc/rc.d/init.d/functions文件(centos6)中某单词后面跟一个小括号的行；

```
~]# egrep -o "^[_[:alpha:]]+\(\)" /etc/rc.d/init.d/functions
checkpid()
__kill_pids_term_kill_checkpids()
__kill_pids_term_kill()
__pids_var_run()
__pids_pidof()
daemon()
killproc()
pidfileofproc()
pidofproc()
status()
echo_success()
echo_failure()
echo_passed()
```

3、使用echo输出一个路径，使用egrep取出其基名；

```
~]# echo "/mnt/sdc/" | egrep -o "[^/]+/?$" | cut -d"/" -f1
sdc

~]# echo "/mnt/sdc/" | egrep -o "[^/]+/?$" | egrep -o  ".*\b"
sdc
```

进一步地：使用egrep取出路径的目录名，类似于dirname命令的结果；

```
~]# echo /etc/rc.d/init.d/functions/ | egrep -o "^/.*/\b" | egrep -o "^/.*\b"
/etc/rc.d/init.d
```

4、找出ifconfig命令结果中1-255之间的数值；

```
]# ifconfig ens3 | egrep -o  "\<([1-9]{1,2}|1[0-9][0-9]|2[0-5][0-5])\>" 
192
168
122
71
255
255
255
192
168
```

5、找出ifconfig命令结果中的IP地址；

```
~]# ifconfig ens3 | egrep  "\<([0-9]{1,2}|1[0-9][0-9]|2[0-5][0-5])\>\.\<([0-9]{1,2}|1[0-9][0-9]|2[0-5][0-5])\>\.\<([0-9]{1,2}|1[0-9][0-9]|2[0-5][0-5])\>\.\<([0-9]{1,2}|1[0-9][0-9]|2[0-5][0-5])\>" 
        inet 192.168.122.71  netmask 255.255.255.0  broadcast 192.168.122.255

~]# ifconfig ens3 | egrep "[0-9]+\.[0-9]+\.[0-9]+\.[0-9]+"
        inet 192.168.122.71  netmask 255.255.255.0  broadcast 192.168.122.255
```

### fgrep：不支持正则表达式搜索；

能够实现快速精确匹配搜索；

```
~]# fgrep "ro.t" /etc/passwd
~]# fgrep "root" /etc/passwd
root:x:0:0:root:/root:/bin/bash
operator:x:11:0:operator:/root:/sbin/nologin
dockerroot:x:997:994:Docker User:/var/lib/docker:/sbin/nologin
```

（完）
