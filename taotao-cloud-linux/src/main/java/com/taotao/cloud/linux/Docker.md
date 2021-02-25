## Docker

    docker(1) 【docker 基础原理（01）】

- Namespace: 内核级别，环境隔离；
    + PID NameSpace: Linux 2.6.24 , PID隔离；
    + Network NameSpace: Linux 2.6.29 , 网络设备、网络栈、端口等网络资源隔离；
    + User NameSpace: Linux 3.8 , 用户和用户组资源隔离；
    + IPC NameSpace: Linux 2.6.19 , 信号量、消息队列和共享内存的隔离；
    + UTS NameSpace: Linux 2.16.19 , 主机名和域名的隔离；
    + Mount NameSpace: Linux 2.4.19 , 挂载点（文件系统）隔离；

    + API: clone(), setns(), unshare()

- CGroup: Linux Control Group, 控制组, Linux 2.6.24
    + 内核级别，限制，控制与分离一个进程族群的资源；

    + 资源：CPU, 内存, IO
    + 功能：
        * Resource limitation：资源限制；
        * Prioritization：优先级控制；
        * Accounting：审计和统计，主要为计费；
        * Control：挂起进程，恢复进程；
    + /sys/fs/cgroup
    + mount
    + lssubsys -m
    + CGroup 的子系统（subsystem）：
        * blkio：设定块设备的 IO 限制；
        * cpu：设定 CPU 的限制；
        * cpuacct：报告 cgroup 中所使用的 CPU 资源；
        * cpuset：为 cgroup 中的任务分配 CPU 和内存资源；
        * memory：设定内存的使用限制；
        * devices：控制cgroup中的任务对设备的访问；
        * freezer：挂起或恢复cgroup中的任务；
        * net_cls：（classid），使用等级级别标识符来标记网络数据包，以实现基于 tc 完成对 cgroup 中产生的流量的控制；
        * perf_event：使用后使 cgroup 总的任务可以进行统一的性能测试；
        * hugetlb：（Translation Look-aside Buffers）对 HugeTLB 系统进行限制；
    + CGroup 中的术语：
        * task（任务）：进程或线程；
        * cgroup：一个独立的资源控制单位，可以包含一个或多个子系统；
        * subsystem：子系统；
        * hierarchy：层级；

- AUFS：UnionFS
    + UnionFS：把不同的物理位置的目录合并到同一个目录中。
    + Another UFS, Alternative UFS, Advanced UFS

- Device mapper: Linux 2.6内核引入的最重要的技术之一，用于在内核中支持逻辑卷管理的通用设备映射机制；
    + Mapped Devide
    + Mapping Table
    + Target Device

### Docker:
    2013年，GO 语言开发，遵循 Apache 2.0 开源协议，由 dotCloud 开发。

- C/S架构：
    + Docker Client: 发起docker相关的请求；
    + Docker Server: 容器运行的节点；


>    docker(1) 【docker 使用入门（02）】    

- namespace, cgroup
    + 解决方案：
        * lxc, openvz
        * lxc: linux containers
        * libcontainer

- 核心组件：
    + docker client：docker的客户端工具，是用户使用docker的主要接口，docker client 与 docker daemon 通信并将结果返回给用户；
    + docker deamon：运行于宿主机上，Docker守护进程，用户可通过docker client 与其交互；
    + image：镜像文件是只读的；用来创建container，一个镜像可以运行多个container；镜像文件可以通过 Dockerfile 文件创建，也可以从 docker hub/registry 下载；
    + repository
        * 公共仓库：Docker hub/registry
        * 私有仓库：docker registry

    + docker container：docker的运行实例，容器是一个隔离环境；
    + 另外两个重要组件：
        * docker link：实现 docker container 之间互相通信；
        * docker volume：持久存储 container 修改的文件；
        
- 安装 docker：
<https://docs.docker.com/install/linux/docker-ce/centos/#install-docker-ce-1>

- docker 的常用命令：
    + 环境信息相关：
        * info
        * version
    + 系统维护相关：
        * images
        * inspect
        * build
        * commit
        * pause
        * unpause
        * ps
        * rm
        * rmi
        * run
        * start/stop/restart
        * top
    + 日志信息相关：
        * events
        * history
        * logs
    + Docker hub服务相关：
        * login
        * logout
        * pull
        * push
        * search

- 基本操作：
    + 获取映像：pull
    + 启动容器：run
        * -i, -t, --rm
        ```
            # docker container run -it --rm centos:iproute /bin/bash
        ```

- 总结：
    + 安装，启动服务；
    + docker hub注册账号；
    + 获取镜像；
    + 启动容器；

>    docker(1) 【docker 应用进阶（03）】

### 回顾
---
- container:
    + namespace：network,pid,pic,user,uts,mount
    + cgroups：子系统，cpu, memory, cpuset, cpuacct,...
    + lxc,openvz
- docker:
    + lxc
    + libcontainer
- 安装：
    + CentOS 6: epel
    + CentOS 7: extra
        * docker-engine
- C/S:
    + `systemctl start docker.service; docker daemon`
    + docker

## Docker:
---
- Docker 功能：
    + 隔离应用
    + 维护镜像
    + 创建易于分发的应用
    + 快速扩展
- Docker 组件：
    + 镜像
    + 容器
    + 链接
    + 数据卷
- Docker 应用：
    + 镜像：包含了启动Docker容器所需要的文件系统层级及其内容；基于UnionFS采用分层结构实现；
        * bootfs, rootfs
        * registry：保存docker镜像及镜像层次结构和元数据；
        * repository：由具有某个功能的镜像的所有相关版本构成的集合；
        * index：管理用户的账号、访问权限、镜像及镜像标签等相关信息；
        * graph：从registry中下载的Docker镜像需要保存在本地，此功能既由graph完成；`/var/lib/docker/graph`
        * 与镜像相关的命令：
            - images
            - search
            - pull
            - push
            - login
            - logout
            - 创建镜像：commit, build
            - 删除镜像：rmi
    + 容器：
        * 独立运行的一个或一组应用，以及他们运行的环境；
        * 命令：  
            - run, kill, stop, start, restart, logs, attach, export, import, rm
        * 启动方法：
            - 通过镜像创建一个新的容器：run
            - 启动一个处于停止状态的容器：start
        * run 命令：
            - `--name=                              Assign a name to the container`
            - `-i, --interactive                    Keep STDIN open even if not attached`
            - `-t, --tty                            Allocate a pseudo-TTY`
            - `--network string                 Connect a container to a network (default "default")`
            - `-d, --detach                         Run container in background and print container ID`
            - 步骤：
                + 检查本地是否存在指定的镜像，不存在则从registry下载；
                + 利用镜像启动容器；
                + 分配一个文件系统，并且在在只读的镜像层之外挂载一个可读写层；
                + 从宿主机配置的网桥接口中桥接一个虚拟接口给此容器；
                + 从地址池中分配一个地址给容器；
                + 执行用户指定的应用程序；
                + 程序执行完成后，容器即终止；
            - 对于交互式模式启动的容器，终止可使用exit命令或 ctrl+d 组合键；
        * logs 命令：
            - 获取一个容器的日志，获取其输出信息；
        * attach命令：
            - 附加至一个运行中的容器；

>    docker(1) 【docker 应用进阶（04）】

- Docker Hub：
    + registry由两种：
        * docker hub：<https://hub.docker.com/r/wangjian2018/centos/>
        * private registry：
        ```
            (1) 安装 docker-registry 程序包，使用 CentOS 7 extra 源
            (2) 启动服务器：
                systemctl start docker-registry.service

            (3) 建议使用 nginx 反代：使用 ssl， 基于 basic 做用户认证；
        ```
        * docker 端使用私有仓库：
        ```
            (1) 配置文件 /etc/sysconfig/docker
                ADD_REGISTRY='--add-registry 172.16.100.68:5000'
                INSECURE_REGISTRY='--insecure-registry 172.16.100.68:5000'

            (2) push镜像：
                (a) tag命令： 给要push到私有仓库的镜像打标签；
                    docker image tag IMAGE_ID REGISTRY_HOST:PORT/NAME[:TAG]
                (b) push命令：
                    docker push REGISTRY_HOST:PORT/NAME[:TAG]

            (3) pull 镜像：
                docker pull REGISTRY_HOST:PORT/NAME[:TAG]
        ```
        * 补充：
        ```
        2018/04/06
        <https://github.com/docker/docker-registry>
        安装：
            # yum install docker-registry  // 提示已经 obsoleted，已被 docker-distribution 替换。
            Package docker-registry is obsoleted by docker-distribution, trying to install docker-distribution-2.6.2-1.git48294d9.el7.x86_64 instead

        配置文件：/etc/docker-distribution/registry/config.yml
            # cat /etc/docker-distribution/registry/config.yml
            version: 0.1
            log:
              fields:
                service: registry
            storage:
                cache:
                    layerinfo: inmemory
                filesystem:
                    rootdirectory: /var/lib/registry
            http:
                addr: :5000

        启动：
            # systemctl start docker-distribution.service 

        打tag：
            # docker image tag centos:iproute localhost:5000/centos:iproute

        push 到本地仓库：
            # docker push localhost:5000/centos 

        默认保存在 /var/lib/registry/ 目录下：
            # ls /var/lib/registry/docker/registry/v2/repositories/

        ```

- Docker的数据卷：
    + Data Volume
        * 数据卷是供一个或多个容器使用的文件或目录，有多种特性：
            - 可以共享于多个容器之间；
            - 对数据卷的修改会立即生效；
            - 对数据卷的更新与镜像无关；
            - 数据卷会一直存在；
    + 使用方式：
        * `-v /MOUNT_POINT`：
            - 默认映射的宿主机路径：/var/lib/docker/volumes/
        * `-v /HOST/DIR:/CONTAINER/DIR`：
            - `/HOST/DIR`：宿主机路径
            - `/CONTAINER/DIR`：容器上的路径
        * 在Dockerfile中使用VOLUME指令定义；
    + 在容器之间共享卷：
        * `--volumes-from list              Mount volumes from the specified container(s)`
    + 删除卷：
        * `docker rm -v CONTAINER_NAME`：删除容器时同时删除卷；
        * `docker run --rm 选项`：表示容器关闭时会被自动删除，同时删除其卷（此容器为最后一个使用此卷的容器时）；
    + 备份和恢复：
        * 备份：
            - `docker run --rm --volumes-from vol_container -v $(pwd):/backup busybox:latest tar cvf /backup/data.tar /data`

- Docker的网络：
    + 支持的网络模式：
        * bridge模式
            - `--net=BRIDGE_NAME`
                + 创建一对儿虚拟网卡，一半在宿主机，一半在容器中；
        * host模式：
            - `--net=host`
        * container模式：
            - `--link`
        * none模式：不使用网络
    + 容器互联：
        * `--link`
