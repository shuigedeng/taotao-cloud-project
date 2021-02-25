## Linux 基础命令

	第 02 天 Linux 基础命令（04）

### 基础命令
---
- `date`:
```
NAME
        date - print or set the system date and time

SYNOPSIS
        date [OPTION]... [+FORMAT]：显示
            formate：格式符号
                %D:
                    # date +%D
                    04/15/18
                %F:
                    # date +%F
                    2018-04-15
                %T:
                    # date +%T
                    19:58:47
                %H-%M-%S:
                    # date +%H-%M-%S
                    20-01-14
                %F-%H-%M-%S:
                    # date +%F-%H-%M-%S
                    2018-04-15-20-01-45
        date [-u|--utc|--universal] [MMDDhhmm[[CC]YY][.ss]]：设置
            MM：月份
            DD：日期
            hh：小时
            mm：分钟
            yy：两位年份
            CCYY：四位年份
            .ss：秒钟
```

- Linux的两种时钟：
    + 系统时钟：有Linux内核通过CPU的工作频率进行的计时；
    + 硬件时钟：BIOS 计时器，嵌在主板上的特殊的电路；
        * `hwclock`：显示硬件时钟：
            - `-s, --hctosys`
            - `-w, --systohc`
    + `cal`：日历
    ```
        # cal
             April 2018     
        Su Mo Tu We Th Fr Sa
         1  2  3  4  5  6  7
         8  9 10 11 12 13 14
        15 16 17 18 19 20 21
        22 23 24 25 26 27 28
        29 30

        # cal 2018
                                       2018                               

               January               February                 March       
        Su Mo Tu We Th Fr Sa   Su Mo Tu We Th Fr Sa   Su Mo Tu We Th Fr Sa
            1  2  3  4  5  6                1  2  3                1  2  3
         7  8  9 10 11 12 13    4  5  6  7  8  9 10    4  5  6  7  8  9 10
        14 15 16 17 18 19 20   11 12 13 14 15 16 17   11 12 13 14 15 16 17
        21 22 23 24 25 26 27   18 19 20 21 22 23 24   18 19 20 21 22 23 24
        28 29 30 31            25 26 27 28            25 26 27 28 29 30 31

                April                   May                   June        
        Su Mo Tu We Th Fr Sa   Su Mo Tu We Th Fr Sa   Su Mo Tu We Th Fr Sa
         1  2  3  4  5  6  7          1  2  3  4  5                   1  2
         8  9 10 11 12 13 14    6  7  8  9 10 11 12    3  4  5  6  7  8  9
        15 16 17 18 19 20 21   13 14 15 16 17 18 19   10 11 12 13 14 15 16
        22 23 24 25 26 27 28   20 21 22 23 24 25 26   17 18 19 20 21 22 23
        29 30                  27 28 29 30 31         24 25 26 27 28 29 30

                July                  August                September     
        Su Mo Tu We Th Fr Sa   Su Mo Tu We Th Fr Sa   Su Mo Tu We Th Fr Sa
         1  2  3  4  5  6  7             1  2  3  4                      1
         8  9 10 11 12 13 14    5  6  7  8  9 10 11    2  3  4  5  6  7  8
        15 16 17 18 19 20 21   12 13 14 15 16 17 18    9 10 11 12 13 14 15
        22 23 24 25 26 27 28   19 20 21 22 23 24 25   16 17 18 19 20 21 22
        29 30 31               26 27 28 29 30 31      23 24 25 26 27 28 29
                                                      30
               October               November               December      
        Su Mo Tu We Th Fr Sa   Su Mo Tu We Th Fr Sa   Su Mo Tu We Th Fr Sa
            1  2  3  4  5  6                1  2  3                      1
         7  8  9 10 11 12 13    4  5  6  7  8  9 10    2  3  4  5  6  7  8
        14 15 16 17 18 19 20   11 12 13 14 15 16 17    9 10 11 12 13 14 15
        21 22 23 24 25 26 27   18 19 20 21 22 23 24   16 17 18 19 20 21 22
        28 29 30 31            25 26 27 28 29 30      23 24 25 26 27 28 29
                                                      30 31
    ```

- 目录相关的命令：
    + 当前目录或工作目录
    + 主目录，家目录: HOME
        * root：/root
        * 普通用户：/home/USERNAME
            - `/home/tom`
        * `~`：用户的主目录
    + `cd`
        * `cd` 或 `cd ~`：切换至当前用户的主目录；
        * `cd ~USERNAME`：切换至指定用户的主目录（仅限 root 用户使用）；
        * `cd -`：在上一个目录与当前目录之间来回切换；
        * `.`：当前目录；
        * `..`：上一级目录；
        * 相关的环境变量：
            - `PWD`：保存了当前目录路径；
            - `OLDPWD`：上一次所在的工作目录路径；
            ```
                [root@Testserver ~]# cd /var/
                [root@Testserver var]# echo $PWD
                /var
                [root@Testserver var]# echo $OLDPWD
                /root
            ```
    + `pwd`：显示当前目录；
    ```
        [root@Testserver var]# pwd
        /var
    ```
    + `ls`：list，显示指定路径下的文件列表；
    ```
        ls [OPTION]... [DIR]...
            -a, --all：显示所有文件，包括以点(.)开头的隐藏文件；
            -l：长格式；
                -rw-r--r-- 1 root    root        8785 Sep  3  2015 install.log
                    -rw-r--r--：
                        最左侧的第一位：文件类型
                            -, d, l, b, c, p, s
                        后面的9位：访问权限，perm
                数字：文件被硬链接的次数；
                root：文件的owner；
                root：文件的group；
                8785：文件的size；
                Sep  3  2015：文件的最近一次被修改的时间；
            -h, --human-readable：单位换算；
            -d：显示目录自身的相关属性，通常要与 -l 一起使用；
            -r, --reverse：逆序显示；
            -R, --recursive：递归显示；
    ```
    + `stat`：获取指定文件的元数据信息；
    ```
        stat [OPTION]... FILE...

        ~]# stat install.log
          File: `install.log'
          Size: 8785            Blocks: 24         IO Block: 4096   regular file
        Device: 802h/2050d      Inode: 2293762     Links: 1
        Access: (0644/-rw-r--r--)  Uid: (    0/    root)   Gid: (    0/    root)
        Access: 2017-09-30 09:44:30.000000000 +0800
        Modify: 2015-09-03 05:59:36.000000000 +0800
        Change: 2015-09-03 05:59:39.000000000 +0800
    ```

- 文件查看的命令
    + `cat`：concatenate files and print on the standard output
    ```
        cat [OPTION]... [FILE]...
            -E：显示行结束符"$"；
                # cat -E /etc/issue
                CentOS release 6.7 (Final)$
                Kernel \r on an \m$
                $
            -n：对显示出的每一行进行编号；
                # cat -n /etc/issue
                 1  CentOS release 6.7 (Final)
                 2  Kernel \r on an \m
                 3
    ```
    + tac：concatenate and print files in reverse
    ```
        # tac /etc/issue

        Kernel \r on an \m
        CentOS release 6.7 (Final)
    ```

- 文件内容类型查看命令：
    + `file`：determine file type
    ```
        # ls -ld /etc/issue /etc /dev/sda /bin/cat
        -rwxr-xr-x.   1 root root 45224 Oct 15  2014 /bin/cat
        brw-rw----    1 root disk  8, 0 Apr 11 10:09 /dev/sda
        drwxr-xr-x. 101 root root 12288 Apr  8 13:29 /etc
        -rw-r--r--.   1 root root    47 Aug  4  2015 /etc/issue

        [root@Testserver ~]# file /etc/issue
        /etc/issue: ASCII text
        [root@Testserver ~]# file /etc/
        /etc/: directory
        [root@Testserver ~]# file /dev/sda
        /dev/sda: block special
        [root@Testserver ~]# file /bin/cat
        /bin/cat: ELF 64-bit LSB executable, x86-64, version 1 (SYSV), dynamically linked (uses shared libs), for GNU/Linux 2.6.18, stripped
    ```

- 回显命令：
    + `echo`
    ```
        [root@Testserver ~]# echo "how are you?"
        how are you?

        -n：禁止自动添加换行符号；
            [root@Testserver ~]# echo -n "how are you?"
            how are you?[root@Testserver ~]# 
        -e：允许使用转义符；
            \n：换行；
            \t：制表符；
            [root@Testserver ~]# echo "how \n are you"
            how \n are you
            [root@Testserver ~]# echo -e "how \n are you" 
            how 
             are you

        echo "$VAR_NAME"：变量会替换，双引号表示弱引用；
        echo '$VAR_NAME'：变量不会替换，强引用；
    ```

- 查找命令的命令
    + `which`：显示命令对应的程序文件路径；
    ```
        which [options] [--] programname [...]

            ~]# which ls
            alias ls='ls --color=auto'
                    /bin/ls

            --skip-alias：不予显示别名；
                ~]# which --skip-alias ls
                /bin/ls
    ```
    + `whereis`：locate the binary, source, and manual page files for a command；
    ```
        whereis [-bmsu] [-BMS directory...  -f] filename...

             ~]# whereis ls
            ls: /bin/ls /usr/share/man/man1p/ls.1p.gz /usr/share/man/man1/ls.1.gz

            -b：Search only for binaries.
                ~]# whereis -b ls
                ls: /bin/ls
            -m：Search only for manual sections.
                ~]# whereis -m ls
                ls: /usr/share/man/man1p/ls.1p.gz /usr/share/man/man1/ls.1.gz
    ```
    + `whatis`：search the whatis database for complete words.
    ```
        使用 makewhatis 命令可将当前系统上所有的帮助手册及与之对应的关键字创建为一个数据库；

        ~]# whatis read
        read                 (1p)  - read a line from standard input
        read                 (2)  - read from a file descriptor
        read                 (3p)  - read from a file
        read [builtins]      (1)  - bash built-in commands, see bash(1)
    ```

```
第 03 天 【linux文件系统及文件类型(01)】
```

- 系统管理类命令：
    + 关机：
        * halt：
            - `-f`：强制，不调用 shutdown 命令；
            - `-p`：切断电源；
        * poweroff
        * shutdown：
        ```
            shutdown [OPTION]...  TIME [MESSAGE]

            -r：reboot
            -h：halt
            -c：cancel

            TIME：
                now：立刻
                +m：相对时间表示法，从命令提交开始多久之后；例如 +3；
                hh:mm：绝对时间表示，指明具体时间；
        ```
        * init 0
    + 重启：
        * reboot
        * shutdown
        * init 6
    + 跟用户登录相关：
        * who：系统当前所有的登录会话；
        ```
            # who
            root     tty1         2017-11-01 15:49
            root     pts/0        2018-05-20 13:47 (114.89.64.142)
        ```
        * whoami：显示当前登录的有效用户；
        ```
            # whoami
            root
        ```
        * w：系统当前所有的登录会话及所做的操作；
        ```
            # w
             14:34:59 up 669 days, 20:16,  2 users,  load average: 0.08, 0.06, 0.01
            USER     TTY      FROM              LOGIN@   IDLE   JCPU   PCPU WHAT
            root     tty1     -                01Nov17 199days  0.01s  0.01s -bash
            root     pts/0    114.89.64.142    13:47    0.00s  0.04s  0.00s w
        ```

```
第03天 【bash基础特性及基础命令（02）】
```

### 目录管理类命令
---

#### 1、mkdir

创建目录.

```
mkdir [OPTION]... DIRECTORY...
    -p：当创建的目录已存在时不会报错，且可以自动创建所需的各目录；
        ~]# ls
        test
        ~]# mkdir test
        mkdir: 无法创建目录"test": 文件已存在
        ~]# mkdir -p test
        ~]# 

    -v：显示命令执行时的详细信息；
        ~]# mkdir test/d/e/f
        mkdir: 无法创建目录"test/d/e/f": 没有那个文件或目录
        ~]# mkdir -pv test/d/e/f
        mkdir: 已创建目录 "test/d"
        mkdir: 已创建目录 "test/d/e"
        mkdir: 已创建目录 "test/d/e/f"

    -m MODE：创建目录时直接指定权限；
        ~]# ls -ld test/d/e/f
        drwxr-xr-x+ 2 root root 4096 10月 27 07:56 test/d/e/f
        ~]# mkdir -pv -m 700  test/d/e/g 
        mkdir: 已创建目录 "test/d/e/g"
        ~]# ls -ld test/d/e/g
        drwx------+ 2 root root 4096 10月 27 07:58 test/d/e/g
```

#### 2、tree

树形格式显示指定目录下的内容。

```
tree [OPTION]... DIRECTORY...
    ~]# tree test/
    test
    ├── a
    │   ├── a.txt
    │   ├── b.txt
    │   └── test.sh
    ├── aa.txt
    ├── a.sh
    ├── a.tar.gz
    ├── a.txt
    ├── awk
    │   ├── 1.txt

    -d：只显示目录；
        # tree -d test/
        test/
        ├── a
        ├── awk
        ├── b
        ├── d
        │   └── e
        │       ├── f
        │       └── g
        └── section39

        8 directories

    -L level：指定显示的层级数目；
        ~]# tree -L 2 test/
        test/
        ├── a
        │   ├── a.txt
        │   ├── b.txt
        │   └── test.sh
        ├── aa.txt
        ├── a.sh
        ├── a.tar.gz
        ├── a.txt
        ├── awk
        │   ├── 1.txt
        │   ├── 2.txt

    -P pattern：只只列出那些与通配符模式匹配的文件；
        ~]# tree -P '*.sh' test/
        test/
        ├── a
        │   └── test.sh
        ├── a.sh
        ├── awk
        ├── b
        │   └── hello.sh
        ├── d
        │   └── e
        │       ├── f
        │       └── g
        ├── dirname.sh
        ├── files.sh
        ├── healthcheck.sh
        ├── pstree.sh
        ├── readlink_test.sh
        └── section39
            ├── color.sh
            ├── conf.sh
            ├── function.sh
            ├── report.sh
            └── run.sh

        8 directories, 13 files

    -F：为目录追加一个' / '，为套接字文件追加一个' = '，为可执行文件追加一个' * '，为FIFO '追加一个' | '，就像ls -F一样；
        ~]# tree -F test/
        test/
        ├── a/
        │   ├── a.txt
        │   ├── b.txt
        │   └── test.sh*
        ├── aa.txt
        ├── a.sh*

    -C：终端下按颜色颜色显示，可以结合 -F 一同使用，类似于 ls --color=auto -F；
        ~]# tree -FC test/
        test/
        ├── a/
        │   ├── a.txt
        │   ├── b.txt
        │   └── test.sh*
        ├── aa.txt
        ├── a.sh*
```

#### 3、rmdir

删除空目录。

```
rmdir [OPTION]... DIRECTORY...
    -v：显示执行过程；
        test]# rmdir -v d/e/f/
        rmdir: 正在删除目录 "d/e/f/"
```

#### 练习：

（1）如何创建 /tmp/x/y1, /tmp/x/y2, /tmp/x/y1/a, /tmp/x/y1/b, /tmp/x/y2/a, /tmp/x/y2/b；

（2）如何创建 x_m, y_m, x_n, y_n；

（3）如何创建 /tmp/bin, /tmp/sbin, /tmp/usr, /tmp/usr/bin, /tmp/usr/sbin；

使用bash的命令行自动展开功能创建：

```
（1）
    ~]# mkdir -pv /tmp/x/{y1,y2}/{a,b}
    mkdir: 已创建目录 "/tmp/x"
    mkdir: 已创建目录 "/tmp/x/y1"
    mkdir: 已创建目录 "/tmp/x/y1/a"
    mkdir: 已创建目录 "/tmp/x/y1/b"
    mkdir: 已创建目录 "/tmp/x/y2"
    mkdir: 已创建目录 "/tmp/x/y2/a"
    mkdir: 已创建目录 "/tmp/x/y2/b"

（2）
    ~]# mkdir -pv /tmp/{x,y}_{m,n}
    mkdir: 已创建目录 "/tmp/x_m"
    mkdir: 已创建目录 "/tmp/x_n"
    mkdir: 已创建目录 "/tmp/y_m"
    mkdir: 已创建目录 "/tmp/y_n"

（3）
    ~]# mkdir -pv /tmp/{bin,sbin,usr/{bin,sbin}}
    mkdir: 已创建目录 "/tmp/bin"
    mkdir: 已创建目录 "/tmp/sbin"
    mkdir: 已创建目录 "/tmp/usr"
    mkdir: 已创建目录 "/tmp/usr/bin"
    mkdir: 已创建目录 "/tmp/usr/sbin"
```

### 文本文件查看类命令
---

#### 1、more

```
more [OPTION...] [file ...]
    -d：显示翻页及退出提示；
```

#### 2、less

使用方法同`man`；

```
less [OPTION...] [file ...]
```

#### 3、head

获取文件的第一部分，默认前10行；

```
head [OPTION...] [file ...]
    -c #：指定获取前#字节；
        ~]# head -c 10 /etc/rc.d/rc.sysinit 
        #!/bin/bas
    -n #|[-#]：指定获取前#行；
        ~]# head -10 /etc/rc.d/rc.sysinit
        #!/bin/bash
        #
        # /etc/rc.d/rc.sysinit - run once at boot time
        #
        # Taken in part from Miquel van Smoorenburg's bcheckrc.
        #

        HOSTNAME=$(/bin/hostname)

        set -m
```

#### 4、tail

获取文件的最后一部分，默认最后10行；

```
tail [OPTION...] [file ...]
    -c #：指定获取后#字节；
        ~]# tail -c 10 /etc/rc.d/rc.sysinit
        sinit
        fi
    -n #|[-#]：指定获取前#行；
        ~]# tail -10 /etc/rc.d/rc.sysinit
        [ "$PROMPT" != no ] && plymouth --ignore-keystroke=Ii
        if strstr "$cmdline" confirm ; then
            touch /var/run/confirm
        fi

        # Let rhgb know that we're leaving rc.sysinit
        if [ -x /bin/plymouth ]; then
            /bin/plymouth --sysinit
        fi
    -f：跟踪显示文件新追加的内容
```


### 文件时间戳管理工具
---

文件由 metadata, data 组成，可以使用 stat 查看文件元数据；

```
~]# stat /etc/rc.d/rc.sysinit
  File: "/etc/rc.d/rc.sysinit"
  Size: 20097       Blocks: 40         IO Block: 4096   普通文件
Device: 802h/2050d  Inode: 26108270    Links: 1
Access: (0755/-rwxr-xr-x)  Uid: (    0/    root)   Gid: (    0/    root)
Access: 2018-10-28 03:39:59.000000000 +0800
Modify: 2015-07-24 16:14:47.000000000 +0800
Change: 2015-09-14 17:06:57.000000000 +0800
```

三个时间戳：

1、access time：访问时间，简写为atime，读取文件内容；

2、modify time：修改时间，简写为mtime，改变文件内容（数据）；

3、change time：改变时间，简写为ctime，元数据发生改变；

#### 1、touch

touch - change file timestamps.

```
touch [OPTION...] [file ...]
    -a: only atime
    -m: onle mtime
    -t STAMP: 指定时间
        [[CC]YY]MMDDhhmm[.ss]
    -c: 如果文件不存在，也不予创建；
```

```
第04天 【文件管理、命令别名和glob(01)】
```

#### 文件管理
---

#### 1、cp

复制文件和目录。

```
cp [OPTION]... SOURCE DEST
    1、SRC是文件：
        如果目标不存在，则新建DEST，并将SRC中内容填充至DEST中。
        如果目标存在：
            如果DEST是文件：将SRC中内容直接覆盖至DEST中；为防止误覆盖，此时建议为cp命令使用-i选项；
            如果DEST是目录：在DEST下新建与原文件同名的文件，并将SRC中内容填充至新文件中。
    2、SRC...：是多个文件：
         DEST必须存在，且为目录，其它情形均会出错；
    3、SRC是目录：
        此时使用选项：-r
        如果DEST不存在：则创建指定目录，并且复制SRC目录中所有文件至DEST中；
            ~]# ls /tmp/pam
            ls: 无法访问/tmp/pam: 没有那个文件或目录
            ~]# cp -r /etc/pam.d/ /tmp/pam
            ~]# ls /tmp/pam
            atd             cvs                  password-auth     remote             smtp.postfix   system-auth
            authconfig      fingerprint-auth     password-auth-ac  run_init           smtp.sendmail  system-auth-ac
            authconfig-gtk  fingerprint-auth-ac
        如果DEST存在：
            如果DEST是文件：报错；
            如果DEST是目录：在DEST新建与SRC同名的目录，并将SRC目录下的所有文件复制至新目录中。

常用选项：
    -i: 交互式，目标为文件时提示是否覆盖，默认root用户已经有别名启用此选项，普通用户则没有设置此别名；
    -r: 递归复制目录及内部的所有内容；
    -a: 归档，相当于-dR --preserve=all
        -d: --no-dereference --preserv=links
        --preserv[=ATTR_LIST]
            mode: 权限
            ownership: 属主数组
            timestamp: 时间戳
            links: 链接
            xattr: 扩展属性
            context: 安全上下文
            all: 所有
    -p: --preserv=mode,ownership,timestamp
    -v: --verbose
    -f: --force，强制覆盖，不提示；
```

#### 2、mv

移动文件。

```
mv [OPTION]... SOURCE DEST

1、如果SRC是单个文件：
    如果DEST不存在：则新建DEST，并将SRC中内容填充至DEST中。
    如果DEST存在：
        如果DEST是文件：将SRC中内容直接覆盖至DEST中；为防止误覆盖，此时建议为mv命令使用-i选项；
        如果DEST是目录：在DEST下新建与原文件同名的文件，并将SRC中内容填充至新文件中。
2、SRC...：是多个文件：
    DEST必须存在，且为目录，其它情形均会出错；
3、SRC是目录：
    如果DEST不存在：则创建指定目录，并且复制SRC目录中所有文件至DEST中；

注意：mv特性与cp特别像，只是移动目录时没有-r选项。

常用选项：
    -i: 交互式；
    -f: 强制覆盖；
```

#### 3、rm

删除文件和目录。

```
rm [OPTION]... FILE...

常用选项：
    -i：交互式；root用户默认使用alias启用，普通账户则没有；
        ~]# rm a.txt
        rm：是否删除普通空文件 "a.txt"？
    -f：强制删除；
        ~]# rm -f a.txt
        ~]# 
    -r：递归删除；
        ~]# rm test/
        rm: 无法删除"test/": 是一个目录

        ~]# rm test/
        rm: 无法删除"test/": 是一个目录
        ~]# rm -r test/
        rm：是否进入目录"test/"? y
        rm：是否删除普通文件 "test/hello.txt"？y
        rm：是否删除目录 "test/"？y
        ~]#
注意：禁止使用 'rm -rf /' 和 'rm -rf /*'。
'rm -rf /'命令会受到 --preserve-root 默认选项的制约，但是'rm -rf /*'命令不会受到任何制约，所以一定要小心。
```

#### 4、nano

全屏幕文本编辑器。

```
nano [OPTIONS] [[+LINE,COLUMN] FILE]...
    # nano /etc/fstab
```

```
第04天 【IO重定向、管道及文本处理工具(02)】
```

### 文件处理工具
---

#### 1、wc

print newline, word, and byte counts for each file.

```
wc [OPTION]... [FILE]...
    -l: lines
    -w: words
    -c: characters

~]# ifconfig | wc -l
92
```

#### 2、cut

以指定的分隔符切分指定文件的每一行数据，并以指定的字段输出显示。

```
cut OPTION... [FILE]...
    -d DELIMITER：指明分隔符；
    -d' '：表示以空格为分隔符；
    -f FILDES：指明要保留的字段；
        #：第#个字段；
        #,#[,#]：离散的多个字段，例如1,3,6
        #-#：连续的多个字段，例如1-6

        混合使用：1-3,7

    ~]# cut -d: -f1,7 /etc/passwd
    root:/bin/bash
    bin:/sbin/nologin
    daemon:/sbin/nologin
    adm:/sbin/nologin
    lp:/sbin/nologin

    --output-delimiter=STRING：指定输入显示的分隔符；

    ~]# cut -d: -f1,7 --output-delimiter=' ' /etc/passwd
    root /bin/bash
    bin /sbin/nologin
    daemon /sbin/nologin
    adm /sbin/nologin
    lp /sbin/nologin
```

#### 3、sort

对文本文件进行排序。

```
sort [OPTION]... [FILE]...
    -r：逆序；
    -f：忽略字符的大小写；
    -t DELIMITER：指定字段分隔符；
    -k #：以指字段为标准排序；
    -n：按数值大小排序，默认以ASCII码字符排序；
    -u：排序之后去重；

    ~]# sort -t: -k3 -n /etc/passwd
    root:x:0:0:root:/root:/bin/bash
    bin:x:1:1:bin:/bin:/sbin/nologin
    daemon:x:2:2:daemon:/sbin:/sbin/nologin
    adm:x:3:4:adm:/var/adm:/sbin/nologin
```

#### 4、uniq

报告或省略重复的行。

Note：连续且完全相同方为重复。

```
uniq [OPTION]... [INPUT [OUTPUT]]

    -i：忽略大小写
    -d：仅显示重复了的行；
    -u：仅显示不曾重复的行；
    -c：去重后显示每一行出现的次数；
```

练习：

1、以冒号分隔，取出/etc/passwd文件的第6至第10行的各自的第1个字段，并将这些信息按第三个字段的数值大小进行排序，最后仅显示各自的第一个字段。

```
~]# head /etc/passwd | tail -n 6 | sort -t: -k3 -n | cut -d':' -f1
lp
sync
shutdown
halt
mail
uucp
```

(完)