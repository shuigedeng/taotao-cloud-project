
    第 33 天 【ansible补充(01)】

- 运维工具：
    + OS Provisioning: PXE, Cobbler(repository, distritution, profile)
        * PXE: dhcp, tftp, (http, ftp)
            - dnsmasq: dhcp, dns
    + OS Config:
        * puppet, saltstack, func
    + Task Excute:
        * fabric, func, saltstack
    + Deployment:
        * fabric

    + 分类：
        * agent: puppet, func
        * agetnless: ansible, fabric
            - ssh service

- ansible的核心组件：
    + ansible core
    + host inventroy
    + core modules
    + custom modules
    + playbook (yaml, jinjia2)
    + connect plugin

- ansible的特性：
    + 基于Python语言实现，由Paramiko, PyYAML和Jinjia2三个关键模块；
    + 部署简单，agentless；
    + 默认使用SSH协议；
        * （1）基于秘钥认证；
        * （2）在inventory文件中指定账号和密码；
    + 主从模式:
        * mater: ansible, ssh client；
        * slave: ssh server；
    + 支持自定义模块：支持各种编程语言
    + 支持Playbook
    + 基于“模块”

- 安装依赖于epel源
    + 配置文件：`/etc/ansible/ansible.cfg`
    + Inventory: `/etc/ansible/hosts`
    ```
        # cd /etc/ansible/
        # cp hosts host.original
        # vim hosts
        [webserver]
        192.168.122.61
        192.168.122.62

        [dbserver]
        192.168.122.72
        192.168.122.71
    ```

- 配置基于秘钥认证：
```
    # ssh-kegen -t rsa -P ''
    # ssh-copy-id -i ~/.ssh/id_rsa.pub root@192.168.122.61

    测试：
        # ssh 192.168.122.61 'ifconfig'
```

- 如何查看模块帮助：
```
    # ansible-doc -l
    # ansible-doc -s MODULE_NAME
```

- ansible命令应用基础：
```
    语法： ansible <host-pattern> [-f forks] [-m module_name] [-a args]
            -f FORKS, --forks=FORKS               specify number of parallel processes to use (default=5)
            -a MODULE_ARGS, --args=MODULE_ARGS    module arguments
            -m MODULE_NAME, --module-name=MODULE_NAME   module name to execute (default=command)
```

- 常见模块：
    + command：Executes a command on a remote node，默认模块；
    ```
        # ansible 192.168.122.61 -m command -a 'date'
        192.168.122.61 | SUCCESS | rc=0 >>
        Sun Apr  8 16:20:01 CST 2018

        # ansible all -a 'date'
    ```
    + cron 命令：Manage cron.d and crontab entries.
        * state:
            - present: 安装
            ```
                # ansible 192.168.122.62 -m cron -a 'name="ntp cron job" minute="*/10" job="/usr/sbin/ntpdate  ntp1.aliyun.com &> /dev/null" state=present'
                192.168.122.62 | SUCCESS => {
                    "changed": true, 
                    "envs": [], 
                    "jobs": [
                        "ntp cron job"
                    ]
                }                
            ```
            - absent：移除
            ```
                # ansible 192.168.122.62 -m cron -a 'name="ntp cron job" state=absent'
                192.168.122.62 | SUCCESS => {
                    "changed": true, 
                    "envs": [], 
                    "jobs": []
                }
            ```
    + user模块：Manage user accounts
        * `name=`：指明用户名
        ```
            # ansible webserver -m user -a 'name=mysql uid=306 system=yes group=mysql state=absent'
        ```
    + group模块：Add or remove groups
        ```
            # ansible webserver -m group -a 'name=mysql gid=306 system=yes'
        ```
    + copy模块：Copies files to remote locations
        * `src=`：定义本地源文件路径 ；
        * `des=`：定义远程目标文件路径，必须为绝对路径；
        * `owner= mode=`：属主和权限；
        * `content=`：取代 src，表示直接用此处指定的信息生成为目标文件内容；
        ```
            # ansible webserver -m copy -a 'src=/etc/fstab dest=/tmp/fstab.ansible owner=root mode=640'
            # ansible webserver -m copy -a 'content="Hello Ansible\nHi Magedu\n" dest=/tmp/hello.ansible owner=root mode=640'
        ```
    + file模块：Sets attributes of files
        * `path=`：指定文件路径，可以使用 name 或 dest 来替换；
        ```
        修改文件属主属组和权限：
            # ansible webserver -m file -a 'owner=mysql group=mysql mode=644 path=/tmp/fstab.ansible'
        创建文件的符号链接：
            src=：指明源文件；
            path=：指明符号链接文件路径；
            # ansible webserver -m file -a 'path=/tmp/fstab.link src=/tmp/fstab.ansible state=link'
        ```
    + ping模块：Try to connect to host, verify a usable python and return `pong' on success
        ```
            # ansible all -m ping
            192.168.122.71 | SUCCESS => {
                "changed": false, 
                "ping": "pong"
            }
            192.168.122.62 | SUCCESS => {
                "changed": false, 
                "ping": "pong"
            }
            192.168.122.72 | SUCCESS => {
                "changed": false, 
                "ping": "pong"
            }
            192.168.122.61 | SUCCESS => {
                "changed": false, 
                "ping": "pong"
            }
        ```
    + service模块：Manage services
        * `enabled={true|false}`：是否开机自动启动；
        * `name=`：服务名称；
        * `state=`：状态，取值有started, stopped, restarted;
        ```
            # ansible webserver -m service -a 'enabled=true name=httpd state=started'
            192.168.122.62 | SUCCESS => {
                "changed": true, 
                "enabled": true, 
                "name": "httpd", 
                "state": "started"
            }
            192.168.122.61 | SUCCESS => {
                "changed": true, 
                "enabled": true, 
                "name": "httpd", 
                "state": "started"
            }
        ```

    + shell模块：Execute commands in nodes
        * 与 command 模块类似，但是可以一次执行多个命令，命令之间用分号(;)隔开；
        * 尤其是用到管道等功能的复杂命令时使用；
        ```
            # ansible webserver -m shell -a 'ifconfig; ls -l'
            # ansible webserver -m shell -a 'echo centos | passwd --stdin centos'
        ```

    + script模块：Runs a local script on a remote node after transferring it
        ```
            # vim hello.sh
            #!/bin/bash
            echo "Hello ansible form scirpt" > /tmp/script.ansible

            # ansible webserver -m script -a '/root/hello.sh'
            192.168.122.62 | SUCCESS => {
                "changed": true, 
                "rc": 0, 
                "stderr": "Shared connection to 192.168.122.62 closed.\r\n", 
                "stdout": "", 
                "stdout_lines": []
            }
            192.168.122.61 | SUCCESS => {
                "changed": true, 
                "rc": 0, 
                "stderr": "Shared connection to 192.168.122.61 closed.\r\n", 
                "stdout": "", 
                "stdout_lines": []
            }

            # ansible webserver -a 'cat /tmp/script.ansible'
            192.168.122.61 | SUCCESS | rc=0 >>
            Hello ansible form scirpt

            192.168.122.62 | SUCCESS | rc=0 >>
            Hello ansible form scirpt
        ```

    + yum模块：Manages packages with the `yum' package manager
        * `name=`：指明要安装的程序包，可以带上版本号；
        * `state={present,latest|absent}`：present,latest表示安装，absent表示卸载；
        ```
            # ansible webserver -m yum -a 'name=httpd state=latest'
        ```
    + setup：收集远程主机的facts(设备管理控制与时间调度程序；Facilities Administration Control and Time Schedule)
        * 每个被管理节点在被接收并运行管理命令之前，会将自己主机相关信息，如操作系统版本，IP地址等报告给远程的andible主机；
        ```
            # ansible 192.168.122.61 -m setup
        ```

    第 33 天 【ansible补充(02)】
    
Ansible.txt: [ansible.txt](ansible.txt)

- Ansible中使用的YAML基础元素：
    + 变量
    + Inventory
    + 条件测试
    + 迭代

- playbook的组成结构：
    + Inventory
    + Modules
    + Ad Hoc Commands
    + Playbooks
        * Tasks：任务，即调用模块完成的某操作；
        * Variables：变量；
        * Templates：模板；
        * Handlers：处理器，由某事件触发执行的操作；
        * Roles：角色；
    - 基本结构：
    ```
        - host: websrvs
          remote_user:
            tasks:
             - task1
               module_name: module_args
             - task2

        一个简单示例1：
            ---
            # A simple task examples.

            - hosts: webserver
              remote_user: root
              tasks:
               - name: Create nginx group
                 group: name=nginx system=yes gid=208
               - name: Create nginx user
                 user: name=nginx uid=208 group=nginx system=yes

            - hosts: dbserver
              remote_user: root
              tasks:
               - name: copy file to dbserver
                 copy: src=/etc/inittab dest=/tmp/inittab.ansible

        一个简单示例2：
            ---
            - hosts: webserver
              remote_user: root
              tasks:
               - name: Install httpd packge
                 yum: name=httpd state=latest 
               - name: Install configuration file for httpd
                 copy: src=/root/ansible_work/conf/httpd.conf dest=/etc/httpd/conf/httpd.conf
               - name: Start httpd service
                 service: enabled=true name=httpd state=started
    ```
    - 条件测试：
        + `when: `
        ```
        简单示例：
            ---
            - hosts: all
              remote_user: root
              vars:
               - username: user10
              tasks:
               - name: create {{ username }} user
                 user: name={{ username }}
                 when: ansible_fqdn == "node1.quantacn.com"
        ```

    - 迭代：重复执行同类tasks时使用
        + 调用：`item`
        + 定义循环列表：`with_items`
        ```
            ---
            - hosts: webserver
              remote_user: root
              tasks:
               - name: Install some softer
                 yum: name={{ item }} state=latest
                 with_items: 
                  - httpd
                  - php
                  - php-mysql
        ```
        + 注意：`with_items`中的列表值也可以是字典，但引用时要使用item.KEY
        ```
            ---
            - hosts: webserver
              remote_user: root
              tasks:
               - name: Add several users
                 user: name={{ item.name }} state=present groups={{ item.groups }}
                 with_items:
                  - { name: 'testuser1', groups: 'wheel' }
                  - { name: 'testuser2', groups: 'root' }
        ```
    - tags: 在playbook可以为某个或某些任务定义一个“标签”，在执行此playbook时，通过为ansible-playbook命令使用--tags选项能实现仅允许指定的tasks，而非所有的。
    ```
        ---
        tasks:
         - hosts: install configuration file for httpd
           template: src/root/templates/httpd.conf.j2 dest=/etc/httpd/conf/httpd.conf
           tags:
            - conf

        # ansible-playbook apache.yml --tags conf 
    ```
    - roles:
        + 目录名同角色名；
        + 目录结构有有固定格式：
            * files：静态文件；
            * templates: Jinjia2模板文件；
            * tasks: 至少有main.yml文件，定义各tasks;
            * handlers: 至少有一个main.yml文件，定义各handlers;
            * meta: 定义依赖关系等信息；
        + site.yml中定义playbook，额外也可以有其它的yml文件；

---
以上为视频笔记，但是我感觉马哥讲的不够详细，也不够深入，下面有一些链接，需要我们个人自己去探索。

参考链接：<br>
[官网](https://docs.ansible.com)<br>
[Ansible中文权威指南](http://ansible-tran.readthedocs.io/en/latest/index.html)<br>
[Ansible 教程](http://docs.linux.xyz/show/4)


### (完)