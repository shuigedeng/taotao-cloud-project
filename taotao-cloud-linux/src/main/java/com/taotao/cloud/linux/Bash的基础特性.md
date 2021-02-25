## Bash的基础特性：

    第03天 【bash基础特性及基础命令（02）】

- 命令历史：
    + history：
        * 环境变量：
        
        ```
            HISTSIZE：命令历史记录的条数；
            HISTFILE：~/.bash_history；
            HISTFILESIZE：命令历史文件记录历史的条数；
        ```

        * `-a`：手动追加当前会话缓冲区的命令历史至历史文件中；
        * `-d`：删除命令历史中指定的命令；
        
        ```
            # history -d 40
        ```

        * `-c`：清空命令历史；
        * `history #`：显示历史中最近的 # 条命令；
        
        ```
            # history 10
              994  whoami
              995  w
              996  exit
              997  ls
              998  ifconfig
              999  exit
             1000  echo $HISTSIZE
             1001  echo $HISTFILE
             1002  echo $HISTFILESIZE
             1003  history 10
        ```

        * 调用历史中的命令：
        
        ```
            !#: 调用命令历史中第 # 条命令；
                # !40
            !string: 调用命令历史中最近一个以 string 开头的命令；
                # !man
            !!: 重复运行上一条命令；
                # !!
        ```

        * 调用上一条命令的最后一次参数：
        
        ```
            !$：
                # ls /etc/fstab 
                /etc/fstab
                # ls -l !$
                ls -l /etc/fstab
                -rw-r--r-- 1 root root 850 7月   9 2017 /etc/fstab

            ESC,.：
                ls -l /etc/fstab
                -rw-r--r-- 1 root root 850 7月   9 2017 /etc/fstab
                # ls /etc/fstab
                /etc/fstab

            Alt+.：需要连接终端开启相关功能；
        ```

        * 控制命令历史的记录方式：
            - 环境变量：HISTCONTROL
            
            ```
                # echo $HISTCONTROL
                ignoredups

                ignoredups：默认值，忽略连续且重复的命令；
                ignorespace：忽略所有以空白字符开头的命令；
                ignoreboth：上述两者皆生效；

                修改环境变量值的方式：export 变量名="值"
                    变量赋值：把赋值符号后面的数据存储于变量名指向的内存空间；
            ```

        * [History（历史）命令用法 15 例](https://linuxtoy.org/archives/history-command-usage-examples.html)
        
- 命令补全：
    + bash 执行命令：
        * 内部命令：cd, help
        * 外部命令：bash 根据 PATH 环境变量定义的路径，自左而右在每个路径搜寻以给定命令名命名的文件，第一次找到的即为要执行的命令；
        * 直接补全：Tab,用户给定的字符串只有一条惟一对应的命令；
        * 以用户给定的字符串为开头对应的命令不唯一，再次Tab会给出命令列表；

- 路径补全：
    + 把用户给出的字符串当做路径开头，并在其指定上级目录下搜索以指定的字符串开头的文件名；
        * 如果唯一，则直接补全；
        * 否则，再次Tab，给出列表；

- 命令行展开：
    + `~`：展开为用户的主目录；
    + `~USERNAME`：展开为指定用户的主目录；
    + `{}`：可承载一个以逗号分隔的列表，并将其展开为多个路径；
    
    ```
        /tmp/{a,b} = /tmp/a, /tmp/b
        /tmp/{tom,jerry}/hi =/tmp/tom/hi, /tmp/jerry/hi
    ```

- 命令的执行结果状态：
    + 程序执行有两类结果；
        * 程序的返回值；
        * 程序的执行状态结果；
    + bash使用特殊变量$?保存最近一条命令的执行状态结果；
        * 0：成功
        * 1-255：失败

```
第04天 【文件管理、命令别名和glob(01)】
```

#### 命令别名（alias）

通过 alias 命令实现，alias 是 bash 的内建命令，可以将指定的字符串定义为用户想替换的命令或操作。

1、可以直接使用`alias `或`alias -p`命令显示当前shell进程中已定义的命令别名：

```
~]# alias
alias cp='cp -i'
alias l.='ls -d .* --color=auto'
alias ll='ls -l --color=auto'
alias ls='ls --color=auto'
alias mv='mv -i'
alias rm='rm -i'
alias which='alias | /usr/bin/which --tty-only --read-alias --show-dot --show-tilde'
```

2、定义：

```
alias [name[=value] ... ]

定义的别名name，期相当于执行命令value；

~]# alias cdnet='cd /etc/sysconfig/network-scripts/'
~]# pwd
/root
~]# cdnet
network-scripts]# pwd
/etc/sysconfig/network-scripts

注意：在命令行中定义的别名，仅对当前shell进程有效；如果想永久有效，要定义在配置文件中；
仅对当前用户有效：写入 ~/.bashrc
对所有用户有效：写入 /etc/bashrc

编辑配置文件给出的新配置不会立即生效；
bash进程重新读取配置文件：
    # source /path/to/config_file
    # . /path/to/config_file
```

3、撤销别名：unalias

```
unalias [-a] name [name ...]
    # unalias cdnet
    -a：撤销所有别名
```

注意：如果别名同原命令的名称，则如果要执行原命令，可使用`\COMMAND`。

```
test]# echo "First file" > first.txt
test]# echo "Second file" > second.txt
test]# cat first.txt second.txt 
First file
Second file

test]# alias
alias cdnet='cd /etc/sysconfig/network-scripts/'
alias cp='cp -i'
alias l.='ls -d .* --color=auto'
alias ll='ls -l --color=auto'
alias ls='ls --color=auto'
alias mv='mv -i'
alias rm='rm -i'
alias which='alias | /usr/bin/which --tty-only --read-alias --show-dot --show-tilde'

alias中定义了cp='cp -i'，所以此处覆盖会有提示：
test]# cp first.txt second.txt 
cp：是否覆盖"second.txt"？ 

此处使用 \cp 则原命令没有使用 -i 选项，覆盖不会有提示：
test]# \cp first.txt second.txt 
test]# cat first.txt second.txt 
First file
First file
```

#### glob (globbing)

bash 中用于实现文件/目录名“通配”。

通配符有：`*, ?, []`

man文档：`# man 7 glob`

1、`*`

任意长度的任意字符：

```
a*b:

glob]# ls
a12b  aab  ab  abc
glob]# ls a*b
a12b  aab  ab
```

2、`?`

任意单个字符。

```
a?b:

glob]# ls
a12b  aab  ab  abc
glob]# ls a?b
aab
glob]# ls a??b
a12b
```

3、`[]`

匹配指定范围的任意单个字符。

```
[0-9]: 所有数字
[a-z]: 所有字母，不区分大小写
[A-Z]: 所有大写字母

glob]# ls
a12b  a2b  aab  ab  abc  aMb
glob]# ls a[0-9]b
a2b
glob]# ls a[a-z]b
aab  aMb
glob]# ls a[A-Z]b
aMb
```

4、`^`

匹配指定范围之外的任意字符。

```
[^0-9]: 匹配数字范围之外的任意字符。

glob]# ls
a12b  a2b  aab  ab  a@b  abc  aMb
glob]# ls a[^0-9]b
aab  a@b  aMb
```

5、专用字符集合

```
glob]# ls
a12b  a2b  aab  ab  a@b  abc  aMb

[:digit:]: 任意数字，相当于0-9

glob]# ls a[[:digit:]]b
a2b

[:lower:]: 所有小写字母

glob]# ls a[[:lower:]]b
aab

[:upper:]: 任意大写字母，相当于A-Z

glob]# ls a[A-Z]b
aMb
glob]# ls a[[:upper:]]b
aMb

[:alpha:]: 任意大小写字母，相当于a-z

glob]# ls a[a-z]b
aab  aMb
glob]# ls a[[:alpha:]]b
aab  aMb

[:alnum:]: 任意数字或字母相当于0-9a-z

[:space:]: 空格字符

[:punct:]: 任意标点符号

glob]# ls a[[:punct:]]b
a@b
```

练习：

1、显示/var目录下所有以l开头，以一个小写字母结尾，且中间出现至少一位数字的文件或目录；

```
# ls -d /var/l*[0-9]*[[:lower:]]
```

2、显示/etc目录下，以任意一位数字开头，且以非数字结尾的文件或目录；

```
# ls -d /etc/[0-9]*[^0-9]
```

3、显示/etc目录下，以非字母开头，后面跟一个字母及其它任意长度任意字符的文件或目录；

```
# ls -d /etc/[^a-z][a-z]*
```

4、复制/etc目录下，所有以m开头，以非数字结尾的文件或目录至/tmp/mageedu目录中；

```
# cp -a /etc/m*[^0-9] /tmp/mageedu
```

5、复制/etc目录下，所有以.d结尾的文件或目录至/tmp/magedu.com目录中；

```
# cp -a /etc/*.d /tmp/magedu.com
```

6、复制/etc目录下，所有以.conf结尾，且以m,n,r,p开头的文件或目录至/tmp/mageedu.com目录中；

```
# cp -a /etc/[mnrp]*.conf /tmp/mageedu.com
或
# cp -a /etc/{m,n,r,p}*.conf /tmp/mageedu.com
```

```
第04天 【IO重定向、管道及文本处理工具(02)】
```

#### bash的快捷键

Ctrl + l：清屏，相当于clear命令；

Ctrl + a：跳转至命令行首；

Ctrl + e：跳转至命令结尾处；

Ctrl + c：取消命令的执行；

Ctrl + u：删除命令行首至光标所在处的所有内容；

Ctrl + k：删除光标所在处至命令行尾部的所有内容；


#### bash的I/O重定向及管道

程序=指令+数据，数据的处理：读入数据（Input)，输出数据（Output）。

Unix下一切皆文件，每一个打开的文件都有一个fd：file descriptor（文件描述符），用数字表示。

标准输入：keyboard，0；

标准输出：monitor，1；

标准错误输出：monitor，2；

I/O重定向：改变标准输入输出的位置。

输出重定向：

```
COMMAND > NEW_POS, COMMAND >> NEW_POS

>：覆盖重定向标准输出流，目标文件中的原有内容会被清除；
>>：追加重定向，新内容会追加至目标文件尾部；

# set -C：禁止将内容覆盖输出至已有文件中；
    强制覆盖：>|
# set +C：允许将内容覆盖输出至已有文件中；

2>：覆盖重定向错误输出数据流；
2>>：追加重定向错误输出数据流；

标准输出喝错误输出各自定向至不同位置：
COMMAND > /path/to/file.out 2> /path/to/error.out

合并标准输出和错误输出为同一个数据流进行重定向：
    &>：覆盖重定向；
    &>>：追加重定向；

COMMAND &> /path/to/file.out

COMMAND > /path/to/file.out 2> &1
COMMAND >> /path/to/file.out 2>> &1
```

输入重定向：`<`

tr命令：转换或删除字符；

```
tr [OPTION]... SET1 [SET2]

把输入数据中的，在SET1中的每个字符对应地替换为SET2中的每个字符；

~]# tr abc ABC
alpha
AlphA

-d：删除在输入的数据流中出现的属于SET1的每个对应的字符；

~]# echo "abc" | tr -d a
bc
```

```
~]# tr [a-z] [A-Z] < /etc/fstab

#
# /ETC/FSTAB
# CREATED BY ANACONDA ON THU SEP  3 05:54:39 2015
#
# ACCESSIBLE FILESYSTEMS, BY REFERENCE, ARE MAINTAINED UNDER '/DEV/DISK'
# SEE MAN PAGES FSTAB(5), FINDFS(8), MOUNT(8) AND/OR BLKID(8) FOR MORE INFO
#
UUID=4F038B28-09D2-44C2-BD35-476BEAED5725 /                       EXT3    DEFAULTS        1 1
UUID=558CC8EE-CFE3-4A67-A802-EEE94BD2B18A /BOOT                   EXT3    DEFAULTS        1 2
UUID=C20DCF7C-A003-4D6C-9A56-5BE3A8F18CBD SWAP                    SWAP    DEFAULTS        0 0
TMPFS                   /DEV/SHM                TMPFS   DEFAULTS        0 0
DEVPTS                  /DEV/PTS                DEVPTS  GID=5,MODE=620  0 0
SYSFS                   /SYS                    SYSFS   DEFAULTS        0 0
PROC                    /PROC                   PROC    DEFAULTS        0 0
/DEV/MYVG/MYDATA    /MYDATA         EXT4    DEFAULTS    0 0
```

HERE Documenttation：<<

此处生成文档，默认标准输出；

```
~]# cat << EOF
> how are you?
> hew old are you?
> EOF
how are you?
hew old are you?

~]# cat >> /tmp/test.out << EOF
> How are you?
> How old are you?
> EOF
[root@Testserver ~]# cat /tmp/test.out 
How are you?
How old are you?
```

管道：

COMMAND1 | COMMAND2 | COMMAND3 |...

Note：最后一个命令会在当前shell进程的子shell进程中执行；

```
~]# echo "$PATH" | tr [a-z] [A-Z]
/USR/JAVA/LATEST/BIN:/USR/LOCAL/SBIN:/USR/LOCAL/BIN:/SBIN:/BIN:/USR/SBIN:/USR/BIN:/USR/LOCAL/NODE//BIN:/U/HOME/ORACLE/DATABASE/11G/BIN:/ROOT/BIN

~]# echo "$PATH" | tr [a-z] [A-Z] | tr -d 'U'
/SR/JAVA/LATEST/BIN:/SR/LOCAL/SBIN:/SR/LOCAL/BIN:/SBIN:/BIN:/SR/SBIN:/SR/BIN:/SR/LOCAL/NODE//BIN://HOME/ORACLE/DATABASE/11G/BIN:/ROOT/BIN
```

tee命令：read from standard input and write to standard output and files.

```
tee [OPTION]... [FILE]...

~]# tee /tmp/tee.out
First line.
First line.
www.magedu.com
www.magedu.com
^C
[root@Testserver ~]# cat /tmp/tee.out
First line.
www.magedu.com
```

练习：

1、将 /etc/passwd 文件中的前5行内容转换为大写后保存至/tmp/passwd.out文件中；

```
~]# head -5 /etc/passwd | tr [a-z] [A-Z] | tee /tmp/passwd.out
ROOT:X:0:0:ROOT:/ROOT:/BIN/BASH
BIN:X:1:1:BIN:/BIN:/SBIN/NOLOGIN
DAEMON:X:2:2:DAEMON:/SBIN:/SBIN/NOLOGIN
ADM:X:3:4:ADM:/VAR/ADM:/SBIN/NOLOGIN
LP:X:4:7:LP:/VAR/SPOOL/LPD:/SBIN/NOLOGIN
~]# cat /tmp/passwd.out
ROOT:X:0:0:ROOT:/ROOT:/BIN/BASH
BIN:X:1:1:BIN:/BIN:/SBIN/NOLOGIN
DAEMON:X:2:2:DAEMON:/SBIN:/SBIN/NOLOGIN
ADM:X:3:4:ADM:/VAR/ADM:/SBIN/NOLOGIN
LP:X:4:7:LP:/VAR/SPOOL/LPD:/SBIN/NOLOGIN
```

2、将登录至当前系统上用户信息中的后3行为信息转换为大写后保存至/tmp/who.out文件中；

```
~]# who | tail -n 3 | tr 'a-z' 'A-Z' > /tmp/who.out
~]# cat /tmp/who.out
ROOT     TTY1         2017-11-01 15:49
ROOT     PTS/0        2018-11-04 16:36 (58.39.111.238)
```

（完）
    