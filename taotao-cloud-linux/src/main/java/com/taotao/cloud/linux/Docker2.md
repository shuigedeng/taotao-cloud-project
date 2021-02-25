    docker(2) 【docker网络模型详解(01)】

### 回顾
---
- 容器技术
    + docker client/docker daemon
        * docker镜像；
        * docker registry；
        * docker container；
            - 单一程序运行环境；
- volume：
    + single host, multi host

集装箱：test.java

### Docker Network
---
- 容器的网络模型：
    + closed container：
       * 不参与网络通信，仅适用于无需网络通信的应用场景，例如：备份，程序调试等；
       * 仅有一个接口：loopback
       * `--net none`
    + bridged container：
        * 此类容器都有两个接口：
            - loopback
            - 以太网接口：桥接至docker daemon设定使用的桥，默认为 docker0；
        * `--net bridge`
        * `-h, --hostname HOSTNAME`
        * `--dns DNS_SERVER_IP`
        * `--add-host "HOSTNAME:IP`
    + docker0 NAT桥模型上的容器发布给外部网络访问：
        * `-p <containerPort`：仅给出了容器端口，表示将指定的容器指定端口映射至主机上的某随机端口；
        * `-p <containerPort>:<hostPort>`：将容器的指定端口映射至之主机上的指定端口；
        * `-p<hostIP>::<containerPort>`：将主机的`<hostIP>`上的某随机端口映射为容器的`<containerPort>`
        * `-p <hostIP>:<hostIP>:<containerPort>`：将主机的`<hostIP>`上的`<hostPort>`端口映射为容器的`<containerPort>`
        * `-P, --publish-all`：发布所有端口，跟`--expose`选项一起指明要暴露出外部的端口；
            - `-P --expose 80 --expose 8080 --expose 443`
        ```
            # docker container run -it --rm -P --expose 80 --expose 8080 --expose 443 --net bridge --name web busybox:latest /bin/sh
        ```
        * 如果不想启动容器时使用默认的docker0桥接口，需要在运行docker daemon命令时使用：
            - -b 选项：指定要使用的桥；
    + 联盟式容器：
        * 启动一个容器时，让其使用某个已经存在的容器的网络名称空间；
        * `--net container:CONTAINER_NAME`
        ```
            # docker run --rm  --name joined_web --net container:web busybox:latest ifconfig -a
            eth0      Link encap:Ethernet  HWaddr 02:42:AC:11:00:02  
                      inet addr:172.17.0.2  Bcast:172.17.255.255  Mask:255.255.0.0
                      UP BROADCAST RUNNING MULTICAST  MTU:1500  Metric:1
                      RX packets:8 errors:0 dropped:0 overruns:0 frame:0
                      TX packets:0 errors:0 dropped:0 overruns:0 carrier:0
                      collisions:0 txqueuelen:0 
                      RX bytes:648 (648.0 B)  TX bytes:0 (0.0 B)

            lo        Link encap:Local Loopback  
                      inet addr:127.0.0.1  Mask:255.0.0.0
                      UP LOOPBACK RUNNING  MTU:65536  Metric:1
                      RX packets:0 errors:0 dropped:0 overruns:0 frame:0
                      TX packets:0 errors:0 dropped:0 overruns:0 carrier:0
                      collisions:0 txqueuelen:1 
                      RX bytes:0 (0.0 B)  TX bytes:0 (0.0 B)
        ```
    + 开放式容器：
        * 容器使用Host的网络名称空间；
        * `--net host`
        ```
            # docker run -it --rm --net host --name open_web busybox:latest /bin/sh
        ```

- 容器间的依赖关系：
    + 链接机制：linking
    + `--link`

- 容器的资源限制：
    + run 命令选项：
        * `-m`
        * `--cpuset-cpus`
        * `--shm-size`

- docker的监控命令：
    + ps命令：
        * -a 
    + images命令：
        * 查看当前主机上的镜像信息；
    + stats命令：
        * 容器状态统计信息，实时监控容器的运行状态；
    + inspect命令：
        * 查看镜像或容器的底层信息;
        * `-f, --format`: {{.ke1.key2}}
            - `# docker inspect -f {{.State.Pid}} web `
    + top命令：
        * 用于查看正在运行的容器中的进程的运行状态；
        ```
            # docker top 4f4dc57630e8
            UID                 PID                 PPID                C                   STIME               TTY                 TIME                CMD
            root                31735               31720               0                   07:12               pts/0               00:00:00            /bin/sh -c node demos/01.js
            root                31757               31735               0                   07:12               pts/0               00:00:00            node demos/01.js
        ```
    + port命令：
        * 查看端口映射；
        ```
            # docker port web
            80/tcp -> 0.0.0.0:32770
            8080/tcp -> 0.0.0.0:32768
            443/tcp -> 0.0.0.0:32769
        ```

- 监控工具：
    + google/cadvisor镜像；

>       docker(2) 【dockerfile详解(02)】

## Dockerfile
---

- Docker Images:
    + docker commit
    + Dockerfile: 文本文件，镜像文件构建脚本；
- Dockerfile：由一系列用于根据基础镜像构建新的镜像文件的专用指令序列组成；
    + 指令：选定基础镜像，安装必要的程序、复制配置文件和数据文件、自动运行的服务以及要暴露的端口等；
    + 命令：`docker build`
    + 语法格式：指令行、注释行和空白行；
        * 指令行：由指令及指令参数构成；
            - 指令：其字符不区分大小写；约定俗成，要使用大写字符；
        * 注释行：# 开头的行，必须单独位于一行当中；
        * 空白行：会被忽略；

- 指令：
    + `FROM`指令：必须是第一个非注释行，用于指定所用到的基础镜像；
        * 语法格式：`FROM <image>[:<tag>]`或`FROM <image>@<digest>`
        ```
            FROM  busybox:latest
            FROM  centos:6.9
        注意：尽量不要在一个dockerfile文件中使用多个FROM指令；
        ```
    + `MAINTANIER`指令：用于提供信息的指令，用于让作者提供本人的信息；
        * 不限制其出现的位置，但建议紧跟在`FROM`之后；
        * 语法格式：`MAINTANIER <author's detail>`
        ```
        例如：
            MAINTANIER MageEdu Linux Operation and Maintance Institute <mage@magedu.com>
        ```
    + COPY指令：用于从 Docker 主机复制文件至正在创建的映像文件中；
        * 语法格式：
            - `COPY <src> ... <dest>`
            - `COPY ["<src>",... "<dest>"]` (文件名中有空白字符时使用此种格式)
                + `<src>`：要复制的源文件或目录，支持使用通配符；
                + `<dest>`：目标路径，正在创建的镜像文件的文件系统路径；建议使用绝对路径，否则，这相对于`WORKDIR`而言；
                + 所有新复制生成的目录文件的`UID`和`GID`均为`0`；
                ```
                例如：
                    COPY server.xml /etc/tomcat/server.xml
                    COPY *.conf /etc/httpd/conf.d/

                注意：
                    <src> 必须是 build 上下文中的路径，因此不能使用类似 "../some_dir/some_file" 类的路径；
                    <src> 如果是目录， 递归复制会自动运行；如果有多个<src>，包括在 <src> 上使用了通配符这种情形，此时<dest>必须是目录，而且得必须得以 “/” 结尾；
                    <dest> 如果事先不存在，它将被自动创建，包括其父目录；
                ```
    + ADD指令：类似于COPY指令，额外还支持复制 TAR 文件，以及 URL 路径；
        * 语法格式：
        ```
            COPY <src> ... <dest>
            COPY ["<src>",... "<dest>"]

            示例：
                ADD haproxy.cnf /etc/haproxy/haproxy.cfg
                ADD logstash_*.cnf /etc/logstash/
                ADD http://wwww.magedu.com/download/nginx/conf/nginx.conf /etc/nginx/

            注意：
            以 URL 格式指定的源文件，下载完成后其目标文件的权限为 600；

                <src> 必须是 build 上下文中的路径，因此不能使用类似 "../some_dir/some_file" 类的路径；

                如果<src>时 URL，且 <dest> 不以 “/” 结尾， 则 <src> 指定的文件将被下载并直接被创建为 <desr>；如果 <dest> 以 “/” 结尾，则 URL 指定的文件将被下载至 <dest> 中，并保留原名；

                如果 <src> 是一个 host 本地的文件系统上的 tar 格式的文件，它将被展开为一个目录，其行为类似于 `tar -x` 命令；但是，如果通过 URL 下载到的文件是 tar 格式的，是不会自动进行展开操作的；

                <src> 如果是目录， 递归复制会自动运行；如果有多个<src>，包括在 <src> 上使用了通配符这种情形，此时<dest>必须是目录，而且得必须得以 “/” 结尾；

                <dest> 如果事先不存在，它将被自动创建，包括其父目录；
        ```
    + ENV指令：定义环境变量，此变量可被当前 dockerfile 文件中的其它指令所调用，调用格式为 $variable_name 或 ${variable_name};
        * 语法格式：
        ```
            EVN <key> <value> 一次定义一个变量
            EVN <key>=<value> ... 一次可定义多个变量，如果<value>中有空白字符，要使用 \ 字符进行转义或加引号；

        例如：
            ENV myName="Obama Clark" myDog=Hello\ Dog \
                myCat=Garfield

            等同于：
                ENV myName Obama Clark
                ENV myDog Hello Dog
                ENV myCat Garfield

        注意：ENV定义的环境变量在镜像运行的整个过程中一直存在，因此，可以使用 `inspect` 命令查看，甚至也可以在 `docker run` 启动此镜像时，使用 `--evn` 选项来修改指定变量的值；
        ```
    + USER指令：指定运行镜像时，或运行 Dockerfile 文件中的任何 `RUN/CMD/ENTRYPOINT` 指令指定的程序时的用户名或 UID；
        * 语法格式：
        ```
            USER <UID>|<Username>

        注意：<UID> 应该使用 `/etc/passwd` 文件存在的用户的 UID， 否则，`docker run` 可能会出错；
        ```
    + WORKDIR指令：用于为 Dockerfile 文件中所有的 `RUN/CMD/ENTRYPOINT/COPY/ADD` 指令指定工作目录；
        * 语法格式：
        ```
            WORKDIR <dirpath>

        注意：WORKDIR 可出现多次，也可使用相对路径，此时表示相对于前一个 WORKDIR 指令指定的路径； WORKDIR 还可以调用由 ENV 定义的环境变量的值；

        例如：
            WORKDIR /var/log
            WORKDIR $STATEPATH
        ```
    + VOLUME指令：用于目标镜像文件中创建一个挂载点目录，用于挂载主机上的卷或其它容器的卷；
        * 语法格式：
        ```
            VOLUME <mountpoint>
            VOLUME ["<mountpoint>", ...]

        注意：
            如果 mountpoint 路径下事先有文件存在，docker run 命令会在挂载完成后将此前的文件复制到新挂载的卷中；
        ```
    + RUN指令：用于指定 `docker build` 过程中要运行的命令，而不是 `docker run` 此 dockerfile 构建成的镜像时运行；
        * 语法格式：
        ```
            RUN <command> 或
            RUN ["<executeable>", "<param1>", "<param2>", ...]

            RUN ["/bin/bash", "-c", "<executeable>", "<param1>", "<param2>", ...]

        例如：
            RUN yum install iproute nginx && yum clean all
        ```
    + CMD指令：类似于 RUN 指令，用于运行程序；但二者运行的时间点不同；CMD 在 `docker run` 时运行，而非 `docker build`；
        * CMD 指令的首要目的在于为启动的容器指定默认要运行的程序，程序运行结束，容器也就结束；不过，CMD 指令指定的程序可被 `docker run`命令行参数中指定要运行的程序所覆盖；
        * 语法格式：
        ```
            CMD <command> 或
            CMD ["<executeable>", "<param1>", "<param2>", ...]
            CMD ["<param1>", "<param2>", ...]   //为 ENTRYPOINT 指令指定程序提供默认参数；

        注意：
            如果 dockerfile 中存在多个 CMD 指令，仅最后一个生效；

        例如：
            CMD ["/usr/sbin/httpd", "-c", "/etc/httpd/conf/httpd.conf"]
        ```
    + ENTRYPOINT指令：类似于 CMD 指令， 但其不会被 docker run 的命令行参数指定的指令所覆盖，而且这些命令行参数会被当作参数送给 ENTRYPOINT 指令指定的程序；但是如果运行 docker run 时使用了 `--entrypoint` 选项，此选项的参数可当作要运行的程序覆盖 ENTRYPOINT 指令指定的程序；
        * 语法格式：
        ```
            ENTRYPOINT <command> 或
            ENTRYPOINT ["<executeable>", "<param1>", "<param2>", ...]  
            
        例如：
            CMD ["-c"]
            ENTRYPOINT ["top", "-b"]         
        ```
    + EXPOSE指令：用于为容器指定要暴露的端口；
        * 语法格式：
        ```
            EXPOSE <port>[/<protocol>] [<port>[/<protocol>]] ...

            <protocol> 为 tcp 或 udp 二者之一， 默认为 tcp;

        例如：
            EXPOSE 11211/tcp 11211/udp
        ```
    + ONBUILD指令：定义触发器；
        * 当前 dockerfile 构建出的镜像被用作基础镜像去构建其它镜像时，ONBUILD指令指定的操作才会被执行；
        * 语法格式
        ```
            ONBUILD <INSTRUCTION>

        例如：
            ONBUILD ADD my.cnf /etc/mysql/my.cnf

        注意：ONBUILD 不能自我嵌套，且不会触发 FROM 和 MAINTAINER 指令；
        ```


```
示例1: busybox 上启动 httpd

    # mkdir busybox
    # cd busybox/

    busybox]# vim Dockerfile 
    # MageEdu
    FROM busybox:latest
    MAINTAINER Magedu <mage@magedu.com>
    COPY index.html /web/html/index.html
    CMD ["httpd","-f","-h","/web/html"]
    EXPOSE 80

    busybox]# vim index.html
    <h1> From docker container(Dorkfile) </h1>

    busybox]# docker image build -t busybos_httpd:0.0.1 .
    Sending build context to Docker daemon  3.072kB
    Step 1/5 : FROM busybox:latest
     ---> 2716f21dc1e3
    Step 2/5 : MAINTAINER Magedu <mage@magedu.com>
     ---> Using cache
     ---> 3953702b6675
    Step 3/5 : COPY index.html /web/html/index.html
     ---> cb9960dd4f91
    Step 4/5 : CMD ["httpd","-f","-h","/web/html"]
     ---> Running in eb08fb1810ca
    Removing intermediate container eb08fb1810ca
     ---> a6bb453c9136
    Step 5/5 : EXPOSE 80
     ---> Running in c226a784cf75
    Removing intermediate container c226a784cf75
     ---> 2f7872c86adb
    Successfully built 2f7872c86adb
    Successfully tagged busybos_httpd:0.0.1

    # docker images
    REPOSITORY              TAG                 IMAGE ID            CREATED              SIZE
    busybos_httpd           0.0.1               2f7872c86adb        About a minute ago   1.15MB

    # docker container run -d --rm -p 80:80 busybos_httpd:0.0.1 
    918ed1a2d06dc76aeaf2430b7b9928b78a36893640e309262e07f090d39107ca

    # docker ps
    CONTAINER ID        IMAGE                 COMMAND                  CREATED             STATUS              PORTS                                                                    NAMES
    918ed1a2d06d        busybos_httpd:0.0.1   "httpd -f -h /web/ht…"   36 seconds ago      Up 35 seconds       0.0.0.0:80->80/tcp                                                       cocky_bhaskara

    wangjian$ curl http://192.168.2.139
    <h1> From docker container(Dorkfile) </h1>
```

练习：   
（1）构建一个基于 CentOS 的 httpd 容器，要求，其主目录路径为/web/htdocs，且主页存在，并以 apache 用户的身份运行，暴露8 0 端口；  
（2）进一步地，其页面文件为主机上卷；  
（3）进一步地，httpd支持解析 php 页面；
（4）构建一个基于 CentOS 的 maridb 镜像，让容器间可互相通信；  
（5）在httpd上部署wordpress； 


>       docker(2) 【dockerfile详解(03)】

- 容器导入和导出：
    + docker export
    + docker import
- 镜像的保存及装载：
    + `docker save -o /PATH/TO/SOMEFILE.TAR NAME[:TAG]`
    + `docker load -i /PATH/FROM/SOMEFILE.TAR`

### 回顾：
---

- Dockerfile 指令：
    + FROM, MAINTAINER
    + COPY, ADD
    + WORKDIR, ENV
    + USER
    + VOLUME
    + EXPOSE
    + RUN
    + CMD, ENTRYPOINT
    + ONBUILD

```
示例2: CentOS 上启动 httpd

    # mkdir docker-httpd
    # cd docker-httpd/

    docker-httpd]# vim httpd.df
    FROM centos:latest
    MAINTAINER MageEdu "<mage@magedu.com>"
    RUN sed -i -e 's@^mirrorlist.*repo=os.*$@baseurl=http://mirrors.aliyun.com/centos/$releasever/os/$basearch/@g' -e '/^mirrorlist.*repo=updates/a enabled=0' -e '/^mirrorlist.*repo=extras/a enabled=0' /etc/yum.repos.d/CentOS-Base.repo && \
            yum -y install httpd php php-mysql php-mbstring && \
            yum clean all && \
            echo -e '<?php\n\tphpinfo();\n?>' > /var/www/html/info.php
    EXPOSE 80/tcp
    CMD ["/usr/sbin/httpd","-f","/etc/httpd/conf/httpd.conf","-DFOREGROUND"]

    docker-httpd]# docker image build -t httpd:2.4 -f httpd.df ./
    Sending build context to Docker daemon   2.56kB
    Step 1/5 : FROM centos:latest
     ---> 2d194b392dd1
    Step 2/5 : MAINTAINER MageEdu "<mage@magedu.com>"
     ---> Using cache
     ---> e1939a249f94
    Step 3/5 : RUN sed -i -e 's@^mirrorlist.*repo=os.*$@baseurl=http://mirrors.aliyun.com/centos/$releasever/os/$basearch/@g' -e '/^mirrorlist.*repo=updates/a enabled=0' -e '/^mirrorlist.*repo=extras/a enabled=0' /etc/yum.repos.d/CentOS-Base.repo &&     yum -y install httpd php php-mysql php-mbstring &&     yum clean all &&     echo -e '<?php\n\tphpinfo();\n?>' > /var/www/html/info.php
     ---> Running in 8efab4b9c604
    Loaded plugins: fastestmirror, ovl
    Determining fastest mirrors
    Resolving Dependencies
    --> Running transaction check
    ---> Package httpd.x86_64 0:2.4.6-67.el7.centos will be installed
    --> Processing Dependency: httpd-tools = 2.4.6-67.el7.centos for package: httpd-2.4.6-67.el7.centos.x86_64
    --> Processing Dependency: system-logos >= 7.92.1-1 for package: httpd-2.4.6-67.el7.centos.x86_64
    --> Processing Dependency: /etc/mime.types for package: httpd-2.4.6-67.el7.centos.x86_64
    --> Processing Dependency: libaprutil-1.so.0()(64bit) for package: httpd-2.4.6-67.el7.centos.x86_64
    --> Processing Dependency: libapr-1.so.0()(64bit) for package: httpd-2.4.6-67.el7.centos.x86_64
    ---> Package php.x86_64 0:5.4.16-42.el7 will be installed
    --> Processing Dependency: php-common(x86-64) = 5.4.16-42.el7 for package: php-5.4.16-42.el7.x86_64
    --> Processing Dependency: php-cli(x86-64) = 5.4.16-42.el7 for package: php-5.4.16-42.el7.x86_64
    ---> Package php-mbstring.x86_64 0:5.4.16-42.el7 will be installed
    ---> Package php-mysql.x86_64 0:5.4.16-42.el7 will be installed
    --> Processing Dependency: php-pdo(x86-64) = 5.4.16-42.el7 for package: php-mysql-5.4.16-42.el7.x86_64
    --> Processing Dependency: libmysqlclient.so.18(libmysqlclient_18)(64bit) for package: php-mysql-5.4.16-42.el7.x86_64
    --> Processing Dependency: libmysqlclient.so.18()(64bit) for package: php-mysql-5.4.16-42.el7.x86_64
    --> Running transaction check
    ---> Package apr.x86_64 0:1.4.8-3.el7 will be installed
    ---> Package apr-util.x86_64 0:1.5.2-6.el7 will be installed
    ---> Package centos-logos.noarch 0:70.0.6-3.el7.centos will be installed
    ---> Package httpd-tools.x86_64 0:2.4.6-67.el7.centos will be installed
    ---> Package mailcap.noarch 0:2.1.41-2.el7 will be installed
    ---> Package mariadb-libs.x86_64 1:5.5.56-2.el7 will be installed
    ---> Package php-cli.x86_64 0:5.4.16-42.el7 will be installed
    --> Processing Dependency: libedit.so.0()(64bit) for package: php-cli-5.4.16-42.el7.x86_64
    ---> Package php-common.x86_64 0:5.4.16-42.el7 will be installed
    --> Processing Dependency: libzip.so.2()(64bit) for package: php-common-5.4.16-42.el7.x86_64
    ---> Package php-pdo.x86_64 0:5.4.16-42.el7 will be installed
    --> Running transaction check
    ---> Package libedit.x86_64 0:3.0-12.20121213cvs.el7 will be installed
    ---> Package libzip.x86_64 0:0.10.1-8.el7 will be installed
    --> Finished Dependency Resolution

    Dependencies Resolved

    ================================================================================
     Package            Arch         Version                       Repository  Size
    ================================================================================
    Installing:
     httpd              x86_64       2.4.6-67.el7.centos           base       2.7 M
     php                x86_64       5.4.16-42.el7                 base       1.4 M
     php-mbstring       x86_64       5.4.16-42.el7                 base       505 k
     php-mysql          x86_64       5.4.16-42.el7                 base       101 k
    Installing for dependencies:
     apr                x86_64       1.4.8-3.el7                   base       103 k
     apr-util           x86_64       1.5.2-6.el7                   base        92 k
     centos-logos       noarch       70.0.6-3.el7.centos           base        21 M
     httpd-tools        x86_64       2.4.6-67.el7.centos           base        87 k
     libedit            x86_64       3.0-12.20121213cvs.el7        base        92 k
     libzip             x86_64       0.10.1-8.el7                  base        48 k
     mailcap            noarch       2.1.41-2.el7                  base        31 k
     mariadb-libs       x86_64       1:5.5.56-2.el7                base       757 k
     php-cli            x86_64       5.4.16-42.el7                 base       2.7 M
     php-common         x86_64       5.4.16-42.el7                 base       564 k
     php-pdo            x86_64       5.4.16-42.el7                 base        98 k

    Transaction Summary
    ================================================================================
    Install  4 Packages (+11 Dependent packages)

    Total download size: 31 M
    Installed size: 55 M
    Downloading packages:
    warning: /var/cache/yum/x86_64/7/base/packages/apr-util-1.5.2-6.el7.x86_64.rpm: Header V3 RSA/SHA256 Signature, key ID f4a80eb5: NOKEY
    Public key for apr-util-1.5.2-6.el7.x86_64.rpm is not installed
    --------------------------------------------------------------------------------
    Total                                              5.4 MB/s |  31 MB  00:05     
    Retrieving key from file:///etc/pki/rpm-gpg/RPM-GPG-KEY-CentOS-7
    Importing GPG key 0xF4A80EB5:
     Userid     : "CentOS-7 Key (CentOS 7 Official Signing Key) <security@centos.org>"
     Fingerprint: 6341 ab27 53d7 8a78 a7c2 7bb1 24c6 a8a7 f4a8 0eb5
     Package    : centos-release-7-4.1708.el7.centos.x86_64 (@CentOS)
     From       : /etc/pki/rpm-gpg/RPM-GPG-KEY-CentOS-7
    Running transaction check
    Running transaction test
    Transaction test succeeded
    Running transaction
      Installing : apr-1.4.8-3.el7.x86_64                                      1/15 
      Installing : apr-util-1.5.2-6.el7.x86_64                                 2/15 
      Installing : httpd-tools-2.4.6-67.el7.centos.x86_64                      3/15 
      Installing : centos-logos-70.0.6-3.el7.centos.noarch                     4/15 
      Installing : 1:mariadb-libs-5.5.56-2.el7.x86_64                          5/15 
      Installing : libzip-0.10.1-8.el7.x86_64                                  6/15 
      Installing : php-common-5.4.16-42.el7.x86_64                             7/15 
      Installing : php-pdo-5.4.16-42.el7.x86_64                                8/15 
      Installing : libedit-3.0-12.20121213cvs.el7.x86_64                       9/15 
      Installing : php-cli-5.4.16-42.el7.x86_64                               10/15 
      Installing : mailcap-2.1.41-2.el7.noarch                                11/15 
      Installing : httpd-2.4.6-67.el7.centos.x86_64                           12/15 
      Installing : php-5.4.16-42.el7.x86_64                                   13/15 
      Installing : php-mysql-5.4.16-42.el7.x86_64                             14/15 
      Installing : php-mbstring-5.4.16-42.el7.x86_64                          15/15 
      Verifying  : apr-1.4.8-3.el7.x86_64                                      1/15 
      Verifying  : httpd-2.4.6-67.el7.centos.x86_64                            2/15 
      Verifying  : mailcap-2.1.41-2.el7.noarch                                 3/15 
      Verifying  : php-cli-5.4.16-42.el7.x86_64                                4/15 
      Verifying  : libedit-3.0-12.20121213cvs.el7.x86_64                       5/15 
      Verifying  : httpd-tools-2.4.6-67.el7.centos.x86_64                      6/15 
      Verifying  : apr-util-1.5.2-6.el7.x86_64                                 7/15 
      Verifying  : php-pdo-5.4.16-42.el7.x86_64                                8/15 
      Verifying  : php-5.4.16-42.el7.x86_64                                    9/15 
      Verifying  : libzip-0.10.1-8.el7.x86_64                                 10/15 
      Verifying  : 1:mariadb-libs-5.5.56-2.el7.x86_64                         11/15 
      Verifying  : php-mysql-5.4.16-42.el7.x86_64                             12/15 
      Verifying  : php-mbstring-5.4.16-42.el7.x86_64                          13/15 
      Verifying  : php-common-5.4.16-42.el7.x86_64                            14/15 
      Verifying  : centos-logos-70.0.6-3.el7.centos.noarch                    15/15 

    Installed:
      httpd.x86_64 0:2.4.6-67.el7.centos       php.x86_64 0:5.4.16-42.el7           
      php-mbstring.x86_64 0:5.4.16-42.el7      php-mysql.x86_64 0:5.4.16-42.el7     

    Dependency Installed:
      apr.x86_64 0:1.4.8-3.el7                                                      
      apr-util.x86_64 0:1.5.2-6.el7                                                 
      centos-logos.noarch 0:70.0.6-3.el7.centos                                     
      httpd-tools.x86_64 0:2.4.6-67.el7.centos                                      
      libedit.x86_64 0:3.0-12.20121213cvs.el7                                       
      libzip.x86_64 0:0.10.1-8.el7                                                  
      mailcap.noarch 0:2.1.41-2.el7                                                 
      mariadb-libs.x86_64 1:5.5.56-2.el7                                            
      php-cli.x86_64 0:5.4.16-42.el7                                                
      php-common.x86_64 0:5.4.16-42.el7                                             
      php-pdo.x86_64 0:5.4.16-42.el7                                                

    Complete!
    Loaded plugins: fastestmirror, ovl
    Cleaning repos: base
    Cleaning up everything
    Maybe you want: rm -rf /var/cache/yum, to also free up space taken by orphaned data from disabled or removed repos
    Cleaning up list of fastest mirrors
    Removing intermediate container 8efab4b9c604
     ---> a3301bca2336
    Step 4/5 : EXPOSE 80/tcp
     ---> Running in c871b2925620
    Removing intermediate container c871b2925620
     ---> 92236d8a1d1c
    Step 5/5 : CMD ["/usr/sbin/httpd","-f","/etc/httpd/conf/httpd.conf","-DFOREGROUND"]
     ---> Running in 785912fcd328
    Removing intermediate container 785912fcd328
     ---> a2eaeef7cc0c
    Successfully built a2eaeef7cc0c
    Successfully tagged httpd:2.4

    # docker images
    REPOSITORY              TAG                 IMAGE ID            CREATED              SIZE
    httpd                   2.4                 a2eaeef7cc0c        About a minute ago   271MB

    # docker container run -d --rm -p 8080:80 httpd:2.4
    69bb0f9620b5fc6865cb375f563257efcaf7b3a938047ce1335d69cfa5c809e2

    # docker ps
    CONTAINER ID        IMAGE                 COMMAND                  CREATED              STATUS              PORTS                                                                    NAMES
    69bb0f9620b5        httpd:2.4             "/usr/sbin/httpd -f …"   About a minute ago   Up About a minute   0.0.0.0:8080->80/tcp                                                     mystifying_archimedes

    测试：
        使用浏览器访问 localhost:8080，会出现 Apache test 123 页面；
        使用浏览器访问 localhost:8080/info.php，会出现 php 信息；
```

（完）
