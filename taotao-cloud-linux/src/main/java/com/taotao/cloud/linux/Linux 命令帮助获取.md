## Linux 命令帮助获取

    第 02 天 【Linux命令帮助的获取详解(03)】

### Linux 命令帮助的获取

- 格式：COMMAND [OPTIONS...] [ARGUMENTS..]
    + 内部命令：
        * `# help COMMAND`
            - hash 命令：Remember or display program locations.
                + shell 搜寻到的外部命令结果会缓存至 kv(key-value) 存储中；
                ```
                # help hash
                    Remember or display program locations.
                    -d                forget the remembered location of each NAME 
                    -r                forget all remembered locations
                # hash -d ls  // 从缓存中删除 ls 命令的缓存
                # hash -r     // 从缓存中删除所有命令的缓存
                ```
            - history 命令：管理命令历史
                + 登陆 shell 时，会读取命令历史文件中记录的命令：~/.bash_history
                + 登陆进 shell 后新执行的命令只会记录在缓存中；这些命令会在用户登出时“追加”至历史文件中；
                ```
                history:
                    -a: 追加本次会话新执行的命令历史列表至历史文件中；
                    -d: 删除命令历史中指定的命令；
                        # history -d 40
                    -c: 清空命令历史；

                    快捷操作：
                        !#: 调用命令历史中第 # 条命令；
                            # !40
                        !string: 调用命令历史中最近一个以 string 开头的命令；
                            # !man
                        !!: 重复运行上一条命令；
                            # !!
                ```
    + 外部命令：都有一个可执行程序，位于文件系统某目录下；
        * 可以使用`which`,`whereis`命令查找； 
        * shell 程序搜寻可执行程序文件的路径定义在 PATH 环境变量中；
            - `# echo $PATH`
            - 注意：自左至右查找
        * （1）`# COMMAND --help; # COMMAND -h`
        * （2）使用手册（manual）
            - `# man COMMAND`
            - 手册页: /usr/share/man/man1....man9
                ```
                man1: 用户命令
                man2: 系统调用
                man3: C 库调用
                man4: 设备文件及特殊文件
                man5: 配置文件格式
                man6: 游戏相关
                man7: 杂项
                man8: 管理类的命令
                ```
            - `whatis` 命令查看关键字在 man 的哪些章节中存在帮助手册；
            - 使用 `man # COMMAND` 查看指定 man 章节中的帮助手册；
            - man 命令的配置文件: /etc/man.config
                ```
                MANPATH /PATH/TO/SOMEWHERE: 指明新的手册文件搜索位置；

                # man -M /PATH/TO/SOMEWHERE COMMAND: 到指定位置下搜索 COMMAND 命令的手册页并显示之；
                ```
            - 帮助手册中的段落说明：
                + NAME: 程序名称及简要说明；
                + SYNOPSIS: 程序的简要使用帮助；
                    * []: 可选内容
                    * <>: 必选内容
                    * a|b: 二选一
                    * ...: 同一内容可出现多次
                    * {}: 辅助定界作用,其所包含的内容可以多选一
                + DESCRIPTION: 功能的详细描述；
                + OPTIONS: 可以使用的选项及说明；
                + EXAMPLES: 使用示例；
                + AUTHOR: 作者
                + REPORTING BUGS
                + SEE ALSO
            - man 命令的操作方法：
                ```
                Space，^V, ^f, ^F: 向文件尾部翻一屏；
                b, ^B: 向文件首部翻一屏；
                d, ^D: 向文件尾部翻半屏；
                u, ^U: 向文件首部翻半屏；
                ENTER or RETURN or ^N or e or ^E or j or ^J: 向文件尾部翻一行；
                y or ^Y or ^P or k or ^K: 向文件首部翻一行；
                q: 退出

                #: 跳转至第 # 行；
                1G: 回到文件首部；
                G: 跳转至文件尾部；

                /KEYWORD: 以 KEYWORD 指定的字符串为关键字，从当前位置向文件尾部搜索；不区分字符大小写；
                    n: 下一个
                    N: 上一个
                ?KEYWORD: 以 KEYWORD 指定的字符串为关键字，从当前位置向文件首部搜索；不区分字符大小写；
                    n: 跟搜索命令同方向，下一个
                    N: 跟搜索命令同方向，上一个
                ```
        * （3）信息页
            - `# info COMMAND`
        * （4）程序自身的帮助文档
            - 位置：`/usr/share/doc/COMMAND-VERSION`
            - README
            - INSTALL
            - ChangeLog
        * （5）程序官方文档
            - 官方站点：Documentation
        * （6）发行版的官方文档
            - http://www.redhat.com/docs
        * （7）Google

        *  (8) slideshare
            - http://www.slideshare.net
            
练习：date命令

1、显示今天为周几？
```
# date +%A
Tuesday
```

2、设定当前系统时间？

3、显示今天的日期，形如：08/15/15？
```
# date +%D
02/06/18
```

4、显示当前的时间，形如： 16:01:21？
```
# date +%T
03:27:23
```

    

